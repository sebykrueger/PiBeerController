package beer.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;

@RunWith(MockitoJUnitRunner.class)
public class BeerServerApplicationTest {
	
	@Mock
	private TemperatureSensor temperatureSensor;
	
	@Mock
	private PowerSwitch powerSwitch;
	
	private BeerServerApplication sot;
	private Context context;
	
	@Before
	public void setUp() {
		context = new Context();
		context.getAttributes().put(TemperatureSensor.KEY, temperatureSensor);	
		context.getAttributes().put(PowerSwitch.KEY, powerSwitch);
		
		sot = new BeerServerApplication(context);
	}
	
	@Test
	public void testGetTemperature() {
		// Arrange		
		doReturn(17.25f).when(temperatureSensor).getLastReading();
		
		Request request = new Request(Method.GET, "/garage/beer/fermenter/temperature");
		Response response = new Response(request);
		
		// Act
		sot.handle(request, response);
		
		// Assert
		assertEquals("17.25", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
	
	@Test
	public void testGetHeatbeltStatus() {
		// Arrange
		doReturn(State.OFF).when(powerSwitch).getPinState();
		
		Request request = new Request(Method.GET, "/garage/beer/heatbelt/status");
		Response response = new Response(request);
		
		// Act
		sot.handle(request, response);
		
		// Assert
		assertEquals("OFF", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
	
	@Test
	public void testGetControllerAttribute() {
		
	}
	
	@Test
	public void testShutdown() {
		
	}
}
