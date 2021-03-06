package beer.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;
import beer.rest.RestletTestCase;

@RunWith(MockitoJUnitRunner.class)
public class BeerServerComponentTest extends RestletTestCase {

	@Mock
	private PowerSwitch powerSwitch;
	
	@Mock
	private TemperatureSensor tempSensor;
	
	@Mock
	private BeerController beerController;
	
	@Mock
	private Configuration config;
	
	@InjectMocks
	private BeerServerComponent component;
	
	@Before
	public void setUp() throws Exception {
//		Engine.setLogLevel(Level.ALL);
//		Engine.setRestletLogLevel(Level.ALL);
		component.start();
	}
	
	@After
	public void tearDown() throws Exception {
		component.stop();
	}
	
	@Test
	public void testGetTemperature() {
		// Arrange
		doReturn(17.5f).when(tempSensor).getLastReading();
		
		Request request = new Request(Method.GET, "https://localhost:8111/garage/beer/fermenter/temperature");
		
		// Act
		Client client = new Client(Protocol.HTTP);
		Response response = client.handle(request);
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("{\"TemperatureSensor.temperature\":\"17.5\"}", response.getEntityAsText());
	}
	
	@Test
	public void testGetHeatbeltStatus() {
		// Arrange
		doReturn(State.OFF).when(powerSwitch).getPinState();
		
		Request request = new Request(Method.GET, "https://localhost:8111/garage/beer/heatbelt/status");
		
		// Act
		Client client = new Client(Protocol.HTTP);
		Response response = client.handle(request);
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("{\"PowerSwitch.status\":\"OFF\"}", response.getEntityAsText());
	}
}
