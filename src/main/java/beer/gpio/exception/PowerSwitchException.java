package beer.gpio.exception;

public class PowerSwitchException extends Exception {
	
	private static final long serialVersionUID = 8821520190064145612L;


	public PowerSwitchException(String message, Throwable cause) {
		super(message, cause);
	}


	public PowerSwitchException(String message) {
		super(message);
	}

}
