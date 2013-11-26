package beer.gpio.controller;

import java.util.logging.Logger;

import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;
import beer.gpio.exception.PowerSwitchException;
import beer.gpio.exception.TemperatureSensorException;

public class BeerController implements Runnable {
	
	private int sleepInterval = 60000;	
	private float baseTemperature = 18;
	private float tolerance = 0.5f;
	private int maxRetries = 10;
	
	private boolean running = true;
	
	private PowerSwitch powerSwitch;
	private TemperatureSensor tempSensor;

	private static Logger log = Logger.getLogger(BeerController.class.getName());
	
	private static BeerController instance;
	
	public static BeerController getInstance() {
		return instance;
	}
	
	public BeerController(PowerSwitch powerSwitch, TemperatureSensor tempSensor) {
		this.powerSwitch = powerSwitch;
		this.tempSensor = tempSensor;
	}
	
	@Override
	public void run() {
		instance = this;
		
		int retries = 1;
		while (running) { 
			try {
				Float temp = tempSensor.readTemperature();
			
				if (tooLow(temp)) {
					log.info("Temperature too low: " + temp + ". Setting PowerSwitch to ON.");
					powerSwitch.setValue(State.ON);
				} else if (tooHigh(temp)) {
					log.info("Temperature too high: " + temp + ". Setting PowerSwitch to OFF.");
					powerSwitch.setValue(State.OFF);
				}			
			} catch (TemperatureSensorException ex) {
				ex.printStackTrace();
				attemptPowerSwitchOff();
				if (++retries > maxRetries) {
					shutdown();
				} else {
					log.severe("Try " + retries + " of " + maxRetries);
				}
			} catch (PowerSwitchException ex) {
				ex.printStackTrace();
				shutdown();
			}
			pause(sleepInterval);
		}
	}

	private boolean tooLow(Float temp) {
		return temp < baseTemperature;
	}

	private boolean tooHigh(Float temp) {
		return temp > (baseTemperature + tolerance);
	}
	
	private void pause(int i) {
        try {
			Thread.sleep(i);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public void shutdown() {
		log.info("BeerController stopping...");
		running = false;
	}
	
	private void attemptPowerSwitchOff() {
		try {
			powerSwitch.setValue(State.OFF);
		} catch (PowerSwitchException ex) {
			ex.printStackTrace();
			log.severe("Failure setting PowerSwitch to OFF");
		}
	}

	// Getters and Setters
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
	
	public int getMaxRetries() {
		return maxRetries;
	}
	
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
}
