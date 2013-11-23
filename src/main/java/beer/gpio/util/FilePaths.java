package beer.gpio.util;

public class FilePaths {

	public String getGpioPath() {
		return "/sys/class/gpio";
	}

	public String getExportPath() {
		return getGpioPath() + "/export";
	}

	public String getUnexportPath() {
		return getGpioPath() + "/unexport";
	}

	public String getDirectionPath(int pinNumber) {
		String devicePath = getGpioPath() + "/gpio%d";
		String directionPath = devicePath + "/direction";
		
		return String.format(directionPath, pinNumber);
	}

	public String getValuePath(int pinNumber) {
		String devicePath = getGpioPath() + "/gpio%d";
		String valuePath = devicePath + "/value";
		return String.format(valuePath, pinNumber);
	}

	public String getBusPath() {
		return "/sys/bus/w1/devices";
	}

	public String get1WireBusPath(String address) {
		String path = getBusPath() + "/%s/w1_slave";
		return String.format(path, address);
	}
}
