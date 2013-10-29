package beer.gpio.controller;

import beer.gpio.model.PowerSwitch;
import beer.gpio.model.TemperatureSensor;
import beer.gpio.util.PiGPIO;
import beer.gpio.util.PowerState;

public class BeerController {
	
	private final int SLEEP_INTERVAL = 60000;
	
	private final float BASE_TEMP = 18;
	private final float TOLERANCE = 0.5f;
	
	private PowerSwitch powerSwitch;
	private TemperatureSensor tempSensor;
	
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
		powerSwitch = new PowerSwitch(PiGPIO.PIN_7);
		powerSwitch.setup();
		
		tempSensor = new TemperatureSensor();
		
		while (true) { // TODO should run in separate thread!
			float temp = tempSensor.read();
			
			if (tooLow(temp)) {
				powerSwitch.applyState(PowerState.ON);
			} else if (tooHigh(temp)) {
				powerSwitch.applyState(PowerState.OFF);
			}
			
			pause(SLEEP_INTERVAL);
		}
	}

	private boolean tooLow(Float temp) {
		return temp < BASE_TEMP; // TODO what if temp == null
	}

	private boolean tooHigh(Float temp) {
		return temp > (BASE_TEMP + TOLERANCE); // TODO what if temp == null
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
	
}
