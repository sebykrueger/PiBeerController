package raspberry.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TemperatureSensor {
	
	private final String BUS_BASE_DIR 	= "/sys/bus/w1/devices";
	private final String TEMP_DEVICE_DIR 	= BUS_BASE_DIR + "/28-000004e55923";
	private final String TEMP_DEVICE_FILE 	= TEMP_DEVICE_DIR + "/w1_slave";

	public float read() {
		String tempLine = null;
		try (FileReader fr = new FileReader(TEMP_DEVICE_FILE)) {
			try (BufferedReader br = new BufferedReader(fr)) {
				br.readLine(); // remove header
				tempLine = br.readLine();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		float temp = Integer.parseInt(tempLine.split("t=")[1]);
		float tempCelsius = temp / 1000;
		System.out.println("The temperature is " + tempCelsius + " Celsius");
		return tempCelsius;
	}
}
