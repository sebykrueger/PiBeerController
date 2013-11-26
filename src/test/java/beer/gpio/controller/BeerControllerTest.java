package beer.gpio.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;
import beer.gpio.exception.PowerSwitchException;
import beer.gpio.exception.TemperatureSensorException;

public class BeerControllerTest {
	
	@Mock
	private PowerSwitch powerSwitch;
	
	@Mock
	private TemperatureSensor tempSensor;
	
	private BeerController beerController;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		
		beerController = new BeerController(powerSwitch, tempSensor);
	}

	@Test
	public void testTempHigh() throws TemperatureSensorException, PowerSwitchException, InterruptedException {		
		when(tempSensor.readTemperature()).thenReturn(20f);
		
		beerController.setSleepInterval(10);
		runBeerControllerForSpecifiedPeriodThenShutdown(5);
		
		verify(powerSwitch).setValue(State.OFF);
	}
	
	@Test
	public void testTempLow() throws TemperatureSensorException, PowerSwitchException, InterruptedException { 
		when(tempSensor.readTemperature()).thenReturn(16f);
		
		beerController.setSleepInterval(10);
		runBeerControllerForSpecifiedPeriodThenShutdown(5);
		
		verify(powerSwitch).setValue(State.ON);
	}
	
	@Test
	public void testTempHighThenLow() throws TemperatureSensorException, PowerSwitchException, InterruptedException {
		when(tempSensor.readTemperature()).thenReturn(20f, 16f);
		
		beerController.setSleepInterval(10);
		runBeerControllerForSpecifiedPeriodThenShutdown(20);
		
		InOrder inOrder = Mockito.inOrder(powerSwitch);
		inOrder.verify(powerSwitch).setValue(State.OFF);
		inOrder.verify(powerSwitch).setValue(State.ON);
	}
	
	@Test
	public void testMaxRetries() throws TemperatureSensorException, InterruptedException, PowerSwitchException {
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error"));
		
		beerController.setSleepInterval(10);
		Thread t = new Thread(beerController);
		t.start();
		t.join();
		
		int maxRetries = beerController.getMaxRetries();
		verify(powerSwitch, times(maxRetries)).setValue(State.OFF);
	}
	
	@Test
	public void testMaxRetriesWithFailingPowerSwitch() throws TemperatureSensorException, InterruptedException, PowerSwitchException {
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error"));
		doThrow(new PowerSwitchException("Error")).when(powerSwitch).setValue((State) any());
		
		beerController.setSleepInterval(10);
		Thread t = new Thread(beerController);
		t.start();
		t.join();
		
		int maxRetries = beerController.getMaxRetries();
		verify(powerSwitch, times(maxRetries)).setValue(State.OFF);
	}
	
	@Test
	public void testOneRetry() throws TemperatureSensorException, InterruptedException, PowerSwitchException {
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error")).thenReturn(16f);
		
		beerController.setSleepInterval(10);
		runBeerControllerForSpecifiedPeriodThenShutdown(15);
		
		InOrder inOrder = Mockito.inOrder(powerSwitch);
		inOrder.verify(powerSwitch).setValue(State.OFF);
		inOrder.verify(powerSwitch).setValue(State.ON);
	}
	
	@Test
	public void testOneRetryThenPowerSwitchFailure() throws TemperatureSensorException, InterruptedException, PowerSwitchException {
		when(tempSensor.readTemperature()).thenThrow(new TemperatureSensorException("Error")).thenReturn(16f);
		doNothing().doThrow(new PowerSwitchException("Error")).when(powerSwitch).setValue((State) any());
		
		beerController.setSleepInterval(10);
		Thread t = new Thread(beerController);
		t.start();
		t.join();
		
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
