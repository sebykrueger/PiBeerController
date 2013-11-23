package beer.gpio.device;

public class TemperatureSensorException extends Exception {

	private static final long serialVersionUID = 7670098575620550128L;
	
	
	public TemperatureSensorException(String message, Throwable cause) {
		super(message, cause);
	}


	public TemperatureSensorException(String message) {
		super(message);
	}

}
