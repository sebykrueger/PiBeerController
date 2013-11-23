package beer.gpio.device;

public class PowerSwitch extends BasePin {
    
    public PowerSwitch() throws TemperatureSensorException {
            super(7, Direction.OUT);
    }
    
    public void setValue(PinState state) throws TemperatureSensorException {
		switch (state) {
		case ON:
			writeFile(getFilePaths().getValuePath(pinNumber), "1");
			this.state = state;
			break;
		case OFF:
			writeFile(getFilePaths().getValuePath(pinNumber), "0");
			this.state = state;
			break;
		default:
			break;
		}
	}
}