package beer.gpio.controller;

import java.io.FileReader;
import java.io.IOException;

import beer.gpio.model.PowerSwitch;
import beer.gpio.model.TemperatureSensor;
import beer.gpio.model.TemperatureSensorException;
import beer.gpio.util.PiGPIO;
import beer.gpio.util.PowerState;

public class BeerController {
	
	private int sleepInterval;
	
	private float baseTemperature = 18;
	private float tolerance = 0.5f;
	
	private PowerSwitch powerSwitch;
	private TemperatureSensor tempSensor;
	
	private final String BUS_BASE_DIR 	= "/sys/bus/w1/devices";
	private final String TEMP_DEVICE_DIR 	= BUS_BASE_DIR + "/28-000004e55923";
	private final String TEMP_DEVICE_FILE 	= TEMP_DEVICE_DIR + "/w1_slave";
	
	private static BeerController world;
	
	private BeerController() {
		// singleton
	}
	
	public static BeerController getInstance() {
		if (world == null) {
			world = new BeerController();
		} 
		return world;
	}

	public static void main(String[] args) {
		getInstance().start();
	}

	public void start() {
		setDefaults();
		
		powerSwitch = new PowerSwitch(PiGPIO.PIN_7);
		powerSwitch.setup();
		
		tempSensor = new TemperatureSensor();
		
		while (true) { // TODO should run in separate thread!
			try {
				try (FileReader fr = new FileReader(TEMP_DEVICE_FILE)) { 
					/* TODO: catch FileNotFoundException. If this happens,
					 * then try and reload the kernel modules (ie, reset the temperature sensor).
					 * Send a txt message the mobile app that an issue has occurred.
					 * If reloading kernel modules still fails, then... 
					 * Turn off the PowerSwitch!!! 
					 * --> java.io.FileNotFoundException: /sys/bus/w1/devices/28-000004e55923/w1_slave (No such file or directory)
					 */
					Float temp = tempSensor.readTemperature(fr);
					
					if (tooLow(temp)) {
						powerSwitch.applyState(PowerState.ON);
					} else if (tooHigh(temp)) {
						powerSwitch.applyState(PowerState.OFF);
					}
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			} catch (TemperatureSensorException ex) {
				System.out.println(ex.getMessage());
			}
			
			pause(sleepInterval);
		}
	}
	
	public void stop() {
		System.exit(0);
	}
	
	private void setDefaults() {
		// set some defaults
		sleepInterval = 60000;
		baseTemperature = 18;
		tolerance = 0.5f;
	}
	
	private boolean tooLow(Float temp) {
		return temp < baseTemperature;
	}

	private boolean tooHigh(Float temp) {
		return temp > (baseTemperature + tolerance);
	}
	
	private void pause(int i) {
        try {
			java.lang.Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public PowerSwitch getPowerSwitch() {
		return powerSwitch;
	}

	public TemperatureSensor getTempSensor() {
		return tempSensor;
	}

	public int getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(int sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

	public float getBaseTemperature() {
		return baseTemperature;
	}

	public void setBaseTemperature(float baseTemperature) {
		this.baseTemperature = baseTemperature;
	}

	public float getTolerance() {
		return tolerance;
	}

	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}
	
}
