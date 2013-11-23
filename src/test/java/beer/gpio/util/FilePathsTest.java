package beer.gpio.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import beer.gpio.util.FilePaths;

public class FilePathsTest {
	
	private FilePaths fixture = new FilePaths();

	@Test
	public void testGetDirectionPath() {
		String actual = fixture.getDirectionPath(2);
		String expected = "/sys/class/gpio/gpio2/direction";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetExportPath() {
		String actual = fixture.getExportPath();
		String expected = "/sys/class/gpio/export";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetGpioPath() {
		String actual = fixture.getGpioPath();
		String expected = "/sys/class/gpio";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetUnexportPath() {
		String actual = fixture.getUnexportPath();
		String expected = "/sys/class/gpio/unexport";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetValuePath() {
		String actual = fixture.getValuePath(3);
		String expected = "/sys/class/gpio/gpio3/value";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetBusPath() {
		String actual = fixture.getBusPath();
		String expected = "/sys/bus/w1/devices";
		assertEquals(expected, actual);
	}
	
	@Test 
	public void testGet1WireBusPath() {
		String actual = fixture.get1WireBusPath("28-000004e55923");
		String expected = "/sys/bus/w1/devices/28-000004e55923/w1_slave";
		assertEquals(expected, actual);
	}
}
