package beer.gpio.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TemperatureSensor {
	
	private final String BUS_BASE_DIR 	= "/sys/bus/w1/devices";
	private final String TEMP_DEVICE_DIR 	= BUS_BASE_DIR + "/28-000004e55923";
	private final String TEMP_DEVICE_FILE 	= TEMP_DEVICE_DIR + "/w1_slave";

	private Float lastTemperatureReading = null;
	
	public Float read() {
		String tempLine = null;
		String crcLine = null;
		try (FileReader fr = new FileReader(TEMP_DEVICE_FILE)) {
			try (BufferedReader br = new BufferedReader(fr)) {
				crcLine = br.readLine();  // 26 01 4b 46 7f ff 0a 10 5c : crc=5c YES
				tempLine = br.readLine(); // 26 01 4b 46 7f ff 0a 10 5c t=18375
										  // http://datasheets.maximintegrated.com/en/ds/DS18B20.pdf
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println(crcLine); // TODO if CRC invalid, return null
		System.out.println(tempLine); 
		
		float temp = Integer.parseInt(tempLine.split("t=")[1]);
		lastTemperatureReading = temp / 1000;
		System.out.println("The temperature is " + lastTemperatureReading + " Celsius");
		return lastTemperatureReading;
	}
	
	public Float getLastReading() {
		return lastTemperatureReading;
	}
}
