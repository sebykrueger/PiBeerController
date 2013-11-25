package beer.gpio.device;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import beer.gpio.exception.PowerSwitchException;
import beer.gpio.util.FilePaths;

public class PowerSwitch {
	
	protected final int pinNumber;
	protected State state = State.UNKNOWN;
	private FilePaths filePaths = new FilePaths();
	
	private static Logger log = Logger.getLogger(PowerSwitch.class.getName());
    
    public PowerSwitch() throws PowerSwitchException {
            this.pinNumber = 7;
    		
    		if (alreadyExported()) {
    			writeFile(getFilePaths().getUnexportPath(), pinNumber + "");
    		}
    		writeFile(getFilePaths().getExportPath(), pinNumber + "");
    		writeFile(getFilePaths().getDirectionPath(pinNumber), PinDirection.OUT.getValue());
            log.info("Created PowerSwitch on Pin 7");
    }
    
    public void setValue(State state) throws PowerSwitchException {
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
    
	private boolean alreadyExported() {
		return new File(getFilePaths().getDirectionPath(pinNumber)).exists();
	}

	public FilePaths getFilePaths() {
		return filePaths;
	}

	public int getPinNumber() {
		return pinNumber;
	}

	public State getPinState() {
		return state;
	}

	protected void writeFile(String fileName, String value) throws PowerSwitchException {
		log.fine("writeFile: " + fileName + ", " + value); 
		try {
			// TODO: doesn't throw an exception if the file is not there!
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(value.getBytes());
			fos.close();
		} catch (IOException ex) {
			log.severe("Could not write to GPIO file");
			throw new PowerSwitchException("Could not write to GPIO file: ", ex);
		}
	}
    
	private enum PinDirection {
		IN("in"), OUT("out");

		private String value;

		private PinDirection(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	public enum State {
		ON, OFF, UNKNOWN;
	}
}