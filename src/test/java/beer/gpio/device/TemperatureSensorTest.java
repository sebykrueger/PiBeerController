package beer.gpio.device;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TemperatureSensorTest {

	@Test
	public void testReadTemperatureSuccess() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
						 "26 01 4b 46 7f ff 0a 10 5c t=18375";
		StringReader reader = new StringReader(fixture);
		Float result = new TemperatureSensor().readTemperature(reader);
		assertEquals(result, new Float("18.375"));	
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=TemperatureSensorException.class)
	public void testReadTemperatureFailure_FileNotFoundException() throws TemperatureSensorException, IOException {
		FileReader fixture = mock(FileReader.class);
		when(fixture.read(any(char[].class), anyInt(), anyInt())).thenThrow(FileNotFoundException.class);
		new TemperatureSensor().readTemperature(fixture);	
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testReadTemperatureFailure_CRCCheckFailed() throws TemperatureSensorException {
		String fixture = "fe ff ff ff ff ff ff ff ff : crc=8a NO\n" +
						  "28 01 4b 46 7f ff 08 10 4c t=-125";
		StringReader reader = new StringReader(fixture);
		new TemperatureSensor().readTemperature(reader);

	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testReadTemperatureFailure_TemperatureDeltaTooLarge() throws TemperatureSensorException {
		String fixture1 = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
				  "26 01 4b 46 7f ff 0a 10 5c t=18375";
		StringReader reader = new StringReader(fixture1);
		TemperatureSensor sensor = new TemperatureSensor();
		sensor.readTemperature(reader);
		
		String fixture2 = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
				  "26 01 4b 46 7f ff 0a 10 5c t=0";
		reader = new StringReader(fixture2);
		sensor.readTemperature(reader);
	}
	
	@Test
	public void testGetLastReading() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + 
				 "26 01 4b 46 7f ff 0a 10 5c t=18375";
		StringReader reader = new StringReader(fixture);
		TemperatureSensor sensor = new TemperatureSensor();
		sensor.readTemperature(reader);
		assertEquals(new Float(18.375), sensor.getLastReading());	
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testBogusString1() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES";   // missing second line
		StringReader reader = new StringReader(fixture);
		new TemperatureSensor().readTemperature(reader);
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testBogusString2() throws TemperatureSensorException {
		String fixture = "26 01 4b 46 7f ff 0a 10 5c : crc=5c YES\n" + // invalid second line 
						 "rubbish";
		StringReader reader = new StringReader(fixture);
		new TemperatureSensor().readTemperature(reader);
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testBogusString3() throws TemperatureSensorException {
		String fixture =  "rubbish";                                   // invalid first line
		StringReader reader = new StringReader(fixture);
		new TemperatureSensor().readTemperature(reader);
	}
	
	@Test(expected=TemperatureSensorException.class)
	public void testNullInput() throws TemperatureSensorException {
		new TemperatureSensor().readTemperature(null);
	}
	
}
