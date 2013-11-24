package beer.gpio.device;

import java.util.logging.Logger;

public class PowerSwitch extends BasePin {
	
	private static Logger log = Logger.getLogger(PowerSwitch.class.getName());
    
    public PowerSwitch() throws TemperatureSensorException {
            super(7, Direction.OUT);
            log.info("Created PowerSwitch on Pin 7");
    }
    
    public void setValue(PinState state) throws TemperatureSensorException {
    	log.fine("PowerSwitch setValue: " + state);
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