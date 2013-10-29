package beer.gpio.util;

public enum PiGPIO {
	OUT	("out"),
	ON	("1"),
	OFF	("0"),	
	PIN_7 ("7"),
	BASE_DIR ("/sys/class/gpio"),
	EXPORT_FILE ("/sys/class/gpio/export"),
	UNEXPORT_FILE("/sys/class/gpio/unexport");
	
	private final String value;
	
	private PiGPIO(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return value;
	}
}
