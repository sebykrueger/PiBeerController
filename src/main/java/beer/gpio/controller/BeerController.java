package beer.gpio.controller;

import java.util.logging.Logger;

import beer.gpio.device.BasePin.PinState;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.TemperatureSensor;
import beer.gpio.device.TemperatureSensorException;
import beer.rest.server.BeerServerComponent;

public class BeerController extends Thread {
	
	private int sleepInterval = 60000;	
	private float baseTemperature = 18;
	private float tolerance = 0.5f;
	private int MAX_RETRIES = 10;
	
	private PowerSwitch powerSwitch;
	private TemperatureSensor tempSensor;

	private static Logger log = Logger.getLogger(BeerController.class.getName());
	
	private static BeerController instance;
	
	public static BeerController getInstance() {
		return instance;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("    ____                    ______            __             ____         ");
		System.out.println("   / __ )___  ___  _____   / ____/___  ____  / /__________  / / /__  _____");
		System.out.println("  / __  / _ \\/ _ \\/ ___/  / /   / __ \\/ __ \\/ __/ ___/ __ \\/ / / _ \\/ ___/");
		System.out.println(" / /_/ /  __/  __/ /     / /___/ /_/ / / / / /_/ /  / /_/ / / /  __/ /    ");
		System.out.println("/_____/\\___/\\___/_/      \\____/\\____/_/ /_/\\__/_/   \\____/_/_/\\___/_/     ");
		System.out.println();
		System.out.println("Copyright \u00a9 Sebastian Krueger, November 2013.");
		System.out.println(); 
		                                                           
		log.info("BeerController starting GPIO...");
		instance = new BeerController();
		instance.start();
		
		log.info("BeerController starting REST Server...");
		new BeerServerComponent().start();
	}

	@Override
	public void run() {		
		try {
			powerSwitch = new PowerSwitch();
			tempSensor = new TemperatureSensor();
		} catch (TemperatureSensorException ex) {
			ex.printStackTrace();
			shutdown();
		}
		
		int retries = 0;
		while (true) { 
			try {
				Float temp = tempSensor.readTemperature();
			
				if (tooLow(temp)) {
					log.info("Temperature too low: " + temp + ". Setting PowerSwitch to ON.");
					powerSwitch.setValue(PinState.ON);
				} else if (tooHigh(temp)) {
					log.info("Temperature too high: " + temp + ". Setting PowerSwitch to OFF.");
					powerSwitch.setValue(PinState.OFF);
				}			
			} catch (TemperatureSensorException ex) {
				ex.printStackTrace();
				if (++retries > MAX_RETRIES) {
					shutdown();
				} else {
					log.severe("Try " + retries + " of " + MAX_RETRIES);
					attemptPowerSwitchOff();
				}
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
			sleep(i);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public void shutdown() {
		log.info("BeerController stopping...");
		attemptPowerSwitchOff();
		System.exit(0);
	}
	
	private void attemptPowerSwitchOff() {
		try {
			powerSwitch.setValue(PinState.OFF);
		} catch (TemperatureSensorException ex) {
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
}
