package beer.gpio.controller;

import beer.gpio.device.BasePin.PinState;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.TemperatureSensor;
import beer.gpio.device.TemperatureSensorException;

public class BeerController extends Thread {
	
	private int sleepInterval = 60000;	
	private float baseTemperature = 18;
	private float tolerance = 0.5f;
	
	private PowerSwitch powerSwitch;
	private TemperatureSensor tempSensor;

	public static void main(String[] args) {
		new BeerController().start();
	}

	@Override
	public void run() {
		try {
			powerSwitch = new PowerSwitch();
		} catch (TemperatureSensorException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		tempSensor = new TemperatureSensor();
		
		while (true) { 
			try {
				Float temp = tempSensor.readTemperature();
			
				if (tooLow(temp)) {
					powerSwitch.setValue(PinState.ON);
				} else if (tooHigh(temp)) {
					powerSwitch.setValue(PinState.OFF);
				}			
			} catch (TemperatureSensorException e) {
				e.printStackTrace();
				System.exit(0);
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
			java.lang.Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
