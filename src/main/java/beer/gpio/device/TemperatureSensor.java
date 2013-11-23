package beer.gpio.device;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import beer.gpio.util.FilePaths;

public class TemperatureSensor {

	private Float newReading = null;
	private Float oldReading = null;
	
	private static int MAX_DELTA = 2;
	
	private String tempLine = null;
	private String crcLine = null;
	
	private Reader source = null;
	
	private FilePaths filePaths = new FilePaths();
	
	public Float readTemperature() throws TemperatureSensorException {
		try {
			// TODO Remove hard-coding.
			return readTemperature(new FileReader(filePaths.get1WireBusPath("28-000004e55923")));
		} catch (FileNotFoundException ex) {
			throw new TemperatureSensorException("Temperature File Not Found.", ex);
		}
	}
	
	/* Package scope for
	 * test to allow inserting of mock Reader object.
	 */
	Float readTemperature(Reader source) throws TemperatureSensorException {
		if (source == null) {
			throw new TemperatureSensorException("Invalid Input");
		}
		this.source = source;
		
		loadFromSource();
		validateInput();
		checkCRC();
		parseTemperature();
		checkDeltaNotTooBig();
		
		return newReading;
	}

	private void validateInput() throws TemperatureSensorException {
		if (tempLine == null) {
			throw new TemperatureSensorException("Invalid Input");
		}
		String regexCRC = ".. .. .. .. .. .. .. .. .. : crc=.. (YES|NO)";
		boolean crcValid = crcLine.matches(regexCRC);
		
		String regexTemp = ".. .. .. .. .. .. .. .. .. t=\\d{5}";
		boolean tempValid = tempLine.matches(regexTemp);
		
		if (!crcValid || !tempValid) {
			throw new TemperatureSensorException("Invalid Input"); 
		}
	}

	private void loadFromSource() throws TemperatureSensorException {
		try (BufferedReader br = new BufferedReader(source)) {
			crcLine = br.readLine();  
			tempLine = br.readLine();
		} catch (FileNotFoundException ex) {
			throw new TemperatureSensorException("Temperature File Not Found.", ex);
		} catch (IOException ex) {
			throw new TemperatureSensorException("Unknown Error", ex);
		}
	}

	private void checkCRC() throws TemperatureSensorException {
		String crc = crcLine.split("crc=")[1].split(" ")[1];
		if (crc.equals("NO")) {
			throw new TemperatureSensorException("CRC check failed: " + crcLine);
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
				throw new TemperatureSensorException("Temperature Delta Too Large");
			}	
		}
	}
	
	public Float getLastReading() {
		return newReading;
	}
}
