package beer.gpio.device;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import beer.gpio.device.BasePin.Direction;
import beer.gpio.util.FilePaths;

public class BasePinTest {
	
	// global fixture
	private int pin = 1;
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test(expected=TemperatureSensorException.class)
	public void testConstructorException_FileNotFound() throws TemperatureSensorException {
		new BasePinForUnitTest(pin, Direction.OUT);
	}

	@Test
	public void testConstructureSuccess() throws IOException, TemperatureSensorException {
		// setup
		File gpioDir = tempFolder.newFolder("gpio");
		File unexportFile = new File(gpioDir.getAbsolutePath() + "/unexport");
		File exportFile = new File(gpioDir.getAbsolutePath() + "/export");
		unexportFile.createNewFile();
		exportFile.createNewFile();
		
		File gpioDir1 = tempFolder.newFolder("gpio/gpio1");
		File directionFile = new File(gpioDir1.getAbsolutePath() + "/direction");
		directionFile.createNewFile();
		
		assertThat(new File(gpioDir.getAbsolutePath() + "/unexport").exists(), is(true));
		assertThat(new File(gpioDir.getAbsolutePath() + "/export").exists(), is(true));
		assertThat(new File(gpioDir1.getAbsolutePath() + "/direction").exists(), is(true));
		
		new BasePinForUnitTest(pin, Direction.OUT);
		
		String direction = FileUtils.readFileToString(directionFile);
		String export = FileUtils.readFileToString(exportFile);
		String unexport = FileUtils.readFileToString(unexportFile);
		
		assertEquals("out", direction);
		assertEquals(pin + "", unexport);
		assertEquals(pin + "", export);
	}
	
	private class BasePinForUnitTest extends BasePin  {
		
		public BasePinForUnitTest(int pinNumber, Direction pinDirection)
				throws TemperatureSensorException {
			super(pinNumber, pinDirection);
		}

		@Override
		public FilePaths getFilePaths() {
			return new FilePathsForUnitTest(tempFolder.getRoot());
		}
	}
}
