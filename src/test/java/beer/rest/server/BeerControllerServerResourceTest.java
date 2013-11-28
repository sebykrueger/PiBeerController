package beer.rest.server;

import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.TemperatureSensor;

@RunWith(MockitoJUnitRunner.class)
public class BeerControllerServerResourceTest {
	
	private static final int SLEEP_INTERVAL_MILLIS = 10;
	private static final float TOLERANCE = 0.5f;
	private static final int MAX_RETRIES = 10;
	private static final float BASE_TEMP = 18f;
	
	@Mock
	private PowerSwitch powerSwitch;
	
	@Mock
	private TemperatureSensor tempSensor;
	
	@Mock
	private Configuration config;
	
	@InjectMocks
	private BeerController beerController;
	
	@Before
	public void setUp() {
		doReturn(BASE_TEMP).when(config).getBaseTemperature();
		doReturn(SLEEP_INTERVAL_MILLIS).when(config).getSleepInterval();
		doReturn(MAX_RETRIES).when(config).getMaxRetries();
		doReturn(TOLERANCE).when(config).getTolerance();
	}

	@Test
	public void testGetSleepInterval() {
		// Arrange
		
		// Act
		
		// Assert
		
	}
	
	@Test
	public void testGetBaseTemp() {
		// Arrange
		
		// Act
				
		// Assert
						
	}
	
	@Test
	public void testGetTolerance() {
		// Arrange
		
		// Act
				
		// Assert
				
	}
	
	@Test
	public void testGetError() {
		// Arrange
		
		// Act
				
		// Assert
				
	}
	
	@Test
	public void testPutSleepInterval() {
		// Arrange
		
		// Act
				
		// Assert
				
	}
	
	@Test
	public void testPutBaseTemp() {
		// Arrange
		
		// Act
				
		// Assert
				
	}
	
	@Test
	public void testPutTolerance() {
		// Arrange
		
		// Act
				
		// Assert
				
	}
	
	@Test
	public void testPutError() {
		// Arrange
		
		// Act
				
		// Assert
				
	}
}
