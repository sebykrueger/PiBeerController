package beer.gpio.controller;

import java.util.logging.Logger;

import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;
import beer.gpio.exception.PowerSwitchException;
import beer.gpio.exception.TemperatureSensorException;

public class BeerController implements Runnable {
	
	public static final String KEY = "BeerController";
	private static final Logger LOG = Logger.getLogger(BeerController.class.getName());
	
	private boolean running = true;
	
	private PowerSwitch powerSwitch;
	private TemperatureSensor tempSensor;
	private Configuration config;

	public BeerController(final PowerSwitch powerSwitch, final TemperatureSensor tempSensor, final Configuration config) {
		this.powerSwitch = powerSwitch;
		this.tempSensor = tempSensor;
		this.config = config;
	}

	@Override
	public void run() {
		int retries = 1;
		while (running) { 
			try {
				Float temp = tempSensor.readTemperature();
			
				if (tooLow(temp)) {
					LOG.info("Temperature too low: " + temp + ". Setting PowerSwitch to ON.");
					powerSwitch.setValue(State.ON);
				} else if (tooHigh(temp)) {
					LOG.info("Temperature too high: " + temp + ". Setting PowerSwitch to OFF.");
					powerSwitch.setValue(State.OFF);
				}
				retries = 1;
			} catch (TemperatureSensorException ex) {
				attemptPowerSwitchOff();
				if (++retries > config.getMaxRetries()) {
					shutdown();
				} else {
					LOG.severe("Try " + retries + " of " + config.getMaxRetries() + " failed.");
				}
			} catch (PowerSwitchException ex) {
				shutdown();
			}
			pause(config.getSleepInterval());
		}
	}

	private boolean tooLow(Float temp) {
		return temp < config.getBaseTemperature();
	}

	private boolean tooHigh(Float temp) {
		return temp > (config.getBaseTemperature() + config.getTolerance());
	}
	
	private void pause(int i) {
        try {
			Thread.sleep(i);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public void shutdown() {
		LOG.info("BeerController stopping...");
		running = false;
	}
	
	private void attemptPowerSwitchOff() {
		try {
			powerSwitch.setValue(State.OFF);
		} catch (PowerSwitchException ex) {
			LOG.severe("Failure setting PowerSwitch to OFF");
		}
	}

	// Getters and Setters
	public PowerSwitch getPowerSwitch() {
		return powerSwitch;
	}

	public TemperatureSensor getTempSensor() {
		return tempSensor;
	}

}
