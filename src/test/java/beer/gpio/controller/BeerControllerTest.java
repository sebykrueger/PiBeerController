package beer.gpio.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testTempHigh() throws TemperatureSensorException, PowerSwitchException, InterruptedException {		
		when(tempSensor.readTemperature()).thenReturn(20f);
		
		BeerController beerController = new BeerController(powerSwitch, tempSensor);
		beerController.setSleepInterval(10);
		Thread t = new Thread(beerController);
		t.start();
		pause(5);
		beerController.shutdown();
		t.join();
		
		verify(powerSwitch).setValue(State.OFF);
	}
	
	@Test
	public void testTempLow() throws TemperatureSensorException, PowerSwitchException, InterruptedException { 
		when(tempSensor.readTemperature()).thenReturn(16f);
		
		BeerController beerController = new BeerController(powerSwitch, tempSensor);
		beerController.setSleepInterval(10);
		Thread t = new Thread(beerController);
		t.start();
		pause(5);
		beerController.shutdown();
		t.join();
		
		verify(powerSwitch).setValue(State.ON);
	}
	
	@Test
	public void testTempHighThenLow() throws TemperatureSensorException, PowerSwitchException, InterruptedException {
		when(tempSensor.readTemperature()).thenReturn(20f, 16f);
		
		BeerController beerController = new BeerController(powerSwitch, tempSensor);
		beerController.setSleepInterval(10);
		Thread t = new Thread(beerController);
		t.start();
		pause(20);
		beerController.shutdown();
		t.join();
		
		verify(powerSwitch).setValue(State.OFF);
		verify(powerSwitch).setValue(State.ON);
	}
	
	// TODO: Need to test all the exceptions!
	
	private void pause(int i) {
        try {
			Thread.sleep(i);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
