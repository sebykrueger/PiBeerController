package beer.gpio.exception;

public class TemperatureSensorException extends Exception {
	
	private static final long serialVersionUID = -6264155918363945522L;


	public TemperatureSensorException(String message, Throwable cause) {
		super(message, cause);
	}


	public TemperatureSensorException(String message) {
		super(message);
	}

}
