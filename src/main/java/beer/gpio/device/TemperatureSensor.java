package beer.gpio.device;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import beer.gpio.exception.TemperatureSensorException;
import beer.gpio.util.FilePaths;

public class TemperatureSensor {
	
	public static final String KEY = "TemperatureSensor";

	private Float newReading = null;
	private Float oldReading = null;
	
	private static int MAX_DELTA = 2;
	
	private String tempLine = null;
	private String crcLine = null;
	
	private Reader source = null;
	
	private FilePaths filePaths = new FilePaths();
	
	private String busID = null;
	
	private static Logger log = Logger.getLogger(TemperatureSensor.class.getName());
	
	public TemperatureSensor() throws TemperatureSensorException {
		validateBusPath();
		validateAndSetBusID();
		
		log.info("Created Temperature Sensor at BusID: " + busID);
	}

	private void validateBusPath() throws TemperatureSensorException {
		boolean busPathExists = new File(getFilePaths().getBusPath()).exists();
		if (! busPathExists) {
			String message = getFilePaths().getBusPath() + " does not exist";
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
		
		boolean busPathIsDirectory = new File(getFilePaths().getBusPath()).isDirectory();
		if (! busPathIsDirectory) {
			String message = getFilePaths().getBusPath() + " is not a directory";
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
	}
	
	private void validateAndSetBusID() throws TemperatureSensorException {
		File[] sensors = new File(getFilePaths().getBusPath()).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.equals("w1_bus_master1")) {
					return false;
				} else {
					return true;
				}
			}
		});
		
		if (sensors == null || sensors.length == 0) {
			String message = "No temperature sensor found";
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
		
		if (sensors.length > 1) {
			String message = "More than 1 temperature sensor found";
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
		
		File sensor = sensors[0];
		busID = sensor.getName();
	}

	public Float readTemperature() throws TemperatureSensorException {
		try {
			return readTemperature(new FileReader(getFilePaths().get1WireBusPath(busID)));
		} catch (FileNotFoundException ex) {
			String message = "Temperature file not found"; 
			log.severe(message);
			throw new TemperatureSensorException(message, ex);
		}
	}
	
	/* Package scope for
	 * test to allow inserting of mock Reader object.
	 */
	Float readTemperature(Reader source) throws TemperatureSensorException {
		if (source == null) {
			String message = "Source is null";
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
		this.source = source;
		
		loadFromSource();
		validateInput();
		checkCRC();
		parseTemperature();
		checkDeltaNotTooBig();
		
		log.fine("readTemperature -> " + newReading);
		return newReading;
	}

	private void validateInput() throws TemperatureSensorException {
		if (tempLine == null) {
			String message = "Invalid Input. Temperature is null.";
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
		String regexCRC = ".. .. .. .. .. .. .. .. .. : crc=.. (YES|NO)";
		boolean crcValid = crcLine.matches(regexCRC);
		
		String regexTemp = ".. .. .. .. .. .. .. .. .. t=\\d{1,5}";
		boolean tempValid = tempLine.matches(regexTemp);
		
		if (!crcValid || !tempValid) {
			String message = "Invalid Input. CRC Valid (" + crcValid + "), Temp Valid (" + tempValid + ")"; 
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
	}

	private void loadFromSource() throws TemperatureSensorException {
		try (BufferedReader br = new BufferedReader(source)) {
			crcLine = br.readLine();  
			tempLine = br.readLine();
		} catch (FileNotFoundException ex) {
			String message = "Temperature File Not Found";
			log.severe(message);
			throw new TemperatureSensorException(message, ex);
		} catch (IOException ex) {
			String message = "Unknown IO exception";
			log.severe(message);
			throw new TemperatureSensorException(message, ex);
		}
	}

	private void checkCRC() throws TemperatureSensorException {
		String crc = crcLine.split("crc=")[1].split(" ")[1];
		if (crc.equals("NO")) {
			String message = "CRC check failed: " + crcLine;
			log.severe(message);
			throw new TemperatureSensorException(message);
		}
	}
	
	private void parseTemperature() {
		float temp = Integer.parseInt(tempLine.split("t=")[1]);
		oldReading = newReading;
		newReading = temp / 1000;
	}

	private void checkDeltaNotTooBig() throws TemperatureSensorException {
		if (newReading != null && oldReading != null) {
			Float delta = newReading - oldReading;
			delta = Math.abs(delta);
			if (delta > MAX_DELTA) {
				String message = "Temperature delta larger than MAX_DELTA(" + MAX_DELTA + ")";
				log.severe(message);
				throw new TemperatureSensorException(message);
			}	
		}
	}
	
	public Float getLastReading() {
		return newReading == null ? Float.NaN : newReading;
	}
	
	public FilePaths getFilePaths() {
		return filePaths;
	}
}
