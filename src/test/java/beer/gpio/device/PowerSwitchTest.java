package beer.gpio.device;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import beer.gpio.device.PowerSwitch.State;
import beer.gpio.exception.PowerSwitchException;
import beer.gpio.util.FilePaths;

public class PowerSwitchTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void testPowerSwitch() throws IOException, PowerSwitchException {
		// setup
		File gpioDir = tempFolder.newFolder("gpio");
		File unexportFile = new File(gpioDir.getAbsolutePath() + "/unexport");
		File exportFile = new File(gpioDir.getAbsolutePath() + "/export");
		unexportFile.createNewFile();
		exportFile.createNewFile();
		
		File gpioDir1 = tempFolder.newFolder("gpio/gpio7");
		File directionFile = new File(gpioDir1.getAbsolutePath() + "/direction");
		File valueFile = new File(gpioDir1.getAbsolutePath() + "/value");
		directionFile.createNewFile();
		valueFile.createNewFile();
		
		// test
		PowerSwitch powerSwitch = new PowerSwitchForUnitTest();
		assertEquals(State.UNKNOWN, powerSwitch.getPinState());		
		String value = FileUtils.readFileToString(valueFile);
		assertEquals("", value);
		
		powerSwitch.setValue(State.ON);
		assertEquals(State.ON, powerSwitch.getPinState());
		value = FileUtils.readFileToString(valueFile);
		assertEquals("1", value);
		
		powerSwitch.setValue(State.OFF);
		assertEquals(State.OFF, powerSwitch.getPinState());
		value = FileUtils.readFileToString(valueFile);
		assertEquals("0", value);
	}
	
	private class PowerSwitchForUnitTest extends PowerSwitch  {

		public PowerSwitchForUnitTest() throws PowerSwitchException {
			super();
		}

		@Override
		public FilePaths getFilePaths() {
			return new FilePathsForUnitTest(tempFolder.getRoot());
		}
	}
}
