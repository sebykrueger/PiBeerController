package raspberry.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import raspberry.util.PiGPIO;
import raspberry.util.PowerState;

public class PowerSwitch {
	
	private final String PS_CHANNEL_BASEDIR;
	private final String PS_CHANNEL_DIRECTION_FILE;
	private final String PS_CHANNEL_COMMAND_FILE;
	
	private final PiGPIO POWERSWITCH_CHANNEL;

	public PowerSwitch(PiGPIO powerSwitchChannel) {
		PS_CHANNEL_BASEDIR 			= PiGPIO.BASE_DIR + "/gpio" + powerSwitchChannel;
		PS_CHANNEL_DIRECTION_FILE 	= PS_CHANNEL_BASEDIR + "/direction";
		PS_CHANNEL_COMMAND_FILE 	= PS_CHANNEL_BASEDIR + "/value";
		POWERSWITCH_CHANNEL 		= powerSwitchChannel;
	}
	
	public void setup() {
		unexportChannel();
		exportChannel();
		setChannelDirection(PiGPIO.OUT);
	}
	
	private void unexportChannel() {
		try (FileWriter writer = new FileWriter(PiGPIO.UNEXPORT_FILE.getValue())){
			File channelBaseDir = new File(PS_CHANNEL_BASEDIR);
            if (channelBaseDir.exists()) {
            	System.out.println("Reset Pin " + POWERSWITCH_CHANNEL);
                writer.write(POWERSWITCH_CHANNEL.getValue());
                writer.flush();
            } else {
            	System.out.println("Pin " + POWERSWITCH_CHANNEL + " not setup. Skipping unexport.");
            }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void exportChannel() {
		try (FileWriter writer = new FileWriter(PiGPIO.EXPORT_FILE.getValue())){
            writer.write(POWERSWITCH_CHANNEL.getValue());
            writer.flush();	
		} catch (IOException ex) {
			ex.printStackTrace();
		}		
	}
	
	private void setChannelDirection(PiGPIO direction) {
		try (FileWriter writer = new FileWriter(PS_CHANNEL_DIRECTION_FILE)) {
			writer.write(direction.getValue());
            writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void applyState(PowerState state) {
		try {
			switch (state) {
			case ON:
				try (FileWriter writer = new FileWriter(PS_CHANNEL_COMMAND_FILE)) {
					writer.write(PiGPIO.ON.getValue());
					writer.flush();
					System.out.println("Set PowerSwitch to ON.");
				}
				break;
			case OFF:
				try (FileWriter writer = new FileWriter(PS_CHANNEL_COMMAND_FILE)) {
					writer.write(PiGPIO.OFF.getValue());
					writer.flush();
					System.out.println("Set PowerSwitch to OFF.");
				}
				break;
			default:
				break;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
