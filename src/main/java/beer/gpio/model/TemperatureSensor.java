package beer.gpio.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class TemperatureSensor {

	private Float lastTemperatureReading = null;
	
	public Float readTemperature(Reader source) throws TemperatureSensorException {
		String tempLine = null;
		String crcLine = null;
		try (BufferedReader br = new BufferedReader(source)) {
			crcLine = br.readLine();  
			tempLine = br.readLine(); 
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println(crcLine);
		System.out.println(tempLine);
		
		String crc = crcLine.split("crc=")[1].split(" ")[1];
		if (crc.equals("NO")) {
			throw new TemperatureSensorException("CRC check failed: " + crcLine);
		}
		
		float temp = Integer.parseInt(tempLine.split("t=")[1]);
		lastTemperatureReading = temp / 1000;
		
		return lastTemperatureReading;
	}
	
	public Float getLastReading() {
		return lastTemperatureReading;
	}
}
