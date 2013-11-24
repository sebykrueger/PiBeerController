package beer.gpio.device;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import beer.gpio.util.FilePaths;

public class TemperatureSensorTest {
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	private File busDir;
	private File tempSensorDir;
	
	@Before
	public void setup() {
		busDir = tempFolder.newFolder("bus");
		tempSensorDir = tempFolder.newFolder("bus/28-000004e55923");
	}

	@Test
	public void testReadTemperatureSuccess() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
						 "26 01 4b 46 7f ff 0a 10 5c t=18375";
		StringReader reader = new StringReader(fixture);
		Float result = new TemperatureSensorForUnitTest().readTemperature(reader);
		assertEquals(result, new Float("18.375"));	
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=TemperatureSensorException.class)
	public void testReadTemperatureFailure_FileNotFoundException() throws IOException, TemperatureSensorException {
		FileReader fixture = mock(FileReader.class);
		when(fixture.read(any(char[].class), anyInt(), anyInt())).thenThrow(FileNotFoundException.class);
		try {
			new TemperatureSensorForUnitTest().readTemperature(fixture);
		} catch (TemperatureSensorException e) {
			assertEquals("Temperature File Not Found", e.getMessage());
			throw e;
		}	
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testReadTemperatureFailure_CRCCheckFailed() throws TemperatureSensorException {
		String fixture = "fe ff ff ff ff ff ff ff ff : crc=8a NO\n" +
						  "28 01 4b 46 7f ff 08 10 4c t=-125";
		StringReader reader = new StringReader(fixture);
		try {
			new TemperatureSensorForUnitTest().readTemperature(reader);
		} catch (TemperatureSensorException ex) {
			assertEquals("Invalid Input", ex.getMessage());
			throw ex;
		}

	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testReadTemperatureFailure_TemperatureDeltaTooLarge() throws TemperatureSensorException {
		String fixture1 = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
				  "26 01 4b 46 7f ff 0a 10 5c t=18375";
		StringReader reader = new StringReader(fixture1);
		TemperatureSensorForUnitTest sensor = new TemperatureSensorForUnitTest();
		sensor.readTemperature(reader);
		
		String fixture2 = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
				  "26 01 4b 46 7f ff 0a 10 5c t=0";
		reader = new StringReader(fixture2);
		try {
			sensor.readTemperature(reader);
		} catch (TemperatureSensorException ex) {
			assertEquals("Temperature delta larger than MAX_DELTA(2)", ex.getMessage());
			throw ex;
		}
	}
	
	@Test
	public void testGetLastReading() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
				 "26 01 4b 46 7f ff 0a 10 5c t=18375";
		StringReader reader = new StringReader(fixture);
		TemperatureSensorForUnitTest sensor = new TemperatureSensorForUnitTest();
		sensor.readTemperature(reader);
		assertEquals(new Float(18.375), sensor.getLastReading());	
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testBogusString1() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES";   // missing second line
		StringReader reader = new StringReader(fixture);
		try {
			new TemperatureSensorForUnitTest().readTemperature(reader);
		} catch (TemperatureSensorException ex) {
			assertEquals("Invalid Input. Temperature is null.", ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testBogusString2() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + // invalid second line 
						 "rubbish";
		StringReader reader = new StringReader(fixture);
		try {
			new TemperatureSensorForUnitTest().readTemperature(reader);
		} catch (TemperatureSensorException ex) {
			assertEquals("Invalid Input", ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testBogusString3() throws TemperatureSensorException {
		String fixture =  "rubbish";                                   // invalid first line
		StringReader reader = new StringReader(fixture);
		try {
			new TemperatureSensorForUnitTest().readTemperature(reader);
		} catch (TemperatureSensorException ex) {
			assertEquals("Invalid Input. Temperature is null.", ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testNullInput() throws TemperatureSensorException {
		try {
			new TemperatureSensorForUnitTest().readTemperature(null);
		} catch (TemperatureSensorException ex) {
			assertEquals("Source is null", ex.getMessage());
			throw ex;
		}
	}
	
	@Test
	public void testConstructorSuccess() throws TemperatureSensorException {
		new TemperatureSensorForUnitTest();
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testConstructorException_BusPathMissing() throws TemperatureSensorException {
		// setup
		tempSensorDir.delete();
		busDir.delete();
		
		try {
			new TemperatureSensorForUnitTest();
		} catch (TemperatureSensorException ex) {
			assertEquals(busDir.getAbsolutePath() + " does not exist", ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testConstructorException_BusPathNotDirectory() throws TemperatureSensorException, IOException {
		// setup
		tempSensorDir.delete();
		busDir.delete();
		busDir.createNewFile();
		
		try {
			new TemperatureSensorForUnitTest();
		} catch (TemperatureSensorException ex) {
			assertEquals(busDir.getAbsolutePath() + " is not a directory", ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testConstructorException_NoSensors() throws TemperatureSensorException {
		// setup
		tempSensorDir.delete();
		
		try {
			new TemperatureSensorForUnitTest();
		} catch (TemperatureSensorException ex) {
			assertEquals("No temperature sensor found", ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testConstructorException_TwoSensors() throws TemperatureSensorException {
		// setup
		tempSensorDir = tempFolder.newFolder("bus/28-000004e55924");
		
		try {
			new TemperatureSensorForUnitTest();
		} catch (TemperatureSensorException ex) {
			assertEquals("More than 1 temperature sensor found", ex.getMessage());
			throw ex;
		}
	}
	
	private class TemperatureSensorForUnitTest extends TemperatureSensor {

		public TemperatureSensorForUnitTest() throws TemperatureSensorException {
			super();
		}
		
		@Override
		public FilePaths getFilePaths() {
			return new FilePathsForUnitTest(tempFolder.getRoot());
		}
	}
}
