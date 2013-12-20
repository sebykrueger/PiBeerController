package beer.gpio.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;
import beer.gpio.exception.PowerSwitchException;
import beer.gpio.exception.TemperatureSensorException;

@RunWith(MockitoJUnitRunner.class)
public class BeerControllerTest {
	
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
	public void testTempHigh() throws Exception {		
		// Arrange
		doReturn(20f).when(tempSensor).readTemperature();
		
		// Act
		runBeerControllerForSpecifiedPeriodThenShutdown(5);
		
		// Assert
		verify(powerSwitch).setValue(State.OFF);
	}
	
	@Test
	public void testTempLow() throws Exception {
		// Arrange
		when(tempSensor.readTemperature()).thenReturn(16f);
		
		// Act
		runBeerControllerForSpecifiedPeriodThenShutdown(1);
		
		// Assert
		verify(powerSwitch).setValue(State.ON);
	}
	
//	@Test
	public void testTempHighThenLow() throws Exception {
		// Arrange
		when(tempSensor.readTemperature()).thenReturn(20f, 16f);

		// Act
		runBeerControllerForSpecifiedPeriodThenShutdown(20);
		
		// Assert
		InOrder inOrder = Mockito.inOrder(powerSwitch);
		inOrder.verify(powerSwitch).setValue(State.OFF);
		inOrder.verify(powerSwitch).setValue(State.ON);
	}
	
	@Test
	public void testMaxRetries() throws Exception {
		// Arrange
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error"));

		// Act
		Thread t = new Thread(beerController);
		t.start();
		t.join();
		
		// Assert
		verify(powerSwitch, times(MAX_RETRIES)).setValue(State.OFF);
	}
	
	@Test
	public void testMaxRetriesWithFailingPowerSwitch() throws Exception {
		// Arrange
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error"));
		doThrow(new PowerSwitchException("Error")).when(powerSwitch).setValue((State) any());
		
		// Act
		Thread t = new Thread(beerController);
		t.start();
		t.join();
		
		// Assert
		verify(powerSwitch, times(MAX_RETRIES)).setValue(State.OFF);
	}
	
//	@Test
	public void testOneRetry() throws Exception {
		// Arrange
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error")).thenReturn(16f);
		
		// Act
		runBeerControllerForSpecifiedPeriodThenShutdown(15);
		
		// Assert
		InOrder inOrder = Mockito.inOrder(powerSwitch);
		inOrder.verify(powerSwitch).setValue(State.OFF);
		inOrder.verify(powerSwitch).setValue(State.ON);
	}
	
	@Test
	public void testOneRetryThenPowerSwitchFailure() throws Exception {
		// Arrange
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error")).thenReturn(16f);
		doNothing().doThrow(new PowerSwitchException("Error")).when(powerSwitch).setValue((State) any());
		
		// Act
		Thread t = new Thread(beerController);
		t.start();
		t.join();
		
		// Assert
		InOrder inOrder = Mockito.inOrder(powerSwitch);
		inOrder.verify(powerSwitch).setValue(State.OFF);
		inOrder.verify(powerSwitch).setValue(State.ON);
	}

	private void runBeerControllerForSpecifiedPeriodThenShutdown(int pauseTime) throws InterruptedException {
		Thread t = new Thread(beerController);
		t.start();
		pause(pauseTime);
		beerController.shutdown();
		t.join();
	}
	
	private void pause(int i) {
        try {
			Thread.sleep(i);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
