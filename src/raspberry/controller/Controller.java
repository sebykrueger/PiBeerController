package raspberry.controller;

import raspberry.model.PowerSwitch;
import raspberry.model.TemperatureSensor;
import raspberry.util.PiGPIO;
import raspberry.util.PowerState;

public class Controller {
	
	private final int SLEEP_INTERVAL = 5000;
	
	private final float BASE_TEMP = 18;
	private final float TOLERANCE = 0.5f;
	
	public static void main(String[] args) {
		Controller world = new Controller();
		world.run();
	}

	private void run() {
		PowerSwitch powerSwitch = new PowerSwitch(PiGPIO.PIN_7);
		powerSwitch.setup();
		
		TemperatureSensor tempSensor = new TemperatureSensor();
		
		while (true) {
			float temp = tempSensor.read();
			
			if (tooLow(temp)) {
				powerSwitch.applyState(PowerState.ON);
			} else if (tooHigh(temp)) {
				powerSwitch.applyState(PowerState.OFF);
			}
			
			pause(SLEEP_INTERVAL);
		}
	}

	private boolean tooLow(float temp) {
		return temp < BASE_TEMP;
	}

	private boolean tooHigh(float temp) {
		return temp > (BASE_TEMP + TOLERANCE);
	}
	
	private void pause(int i) {
        try {
			java.lang.Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
