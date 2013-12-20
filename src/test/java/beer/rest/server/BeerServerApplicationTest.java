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

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;
import beer.rest.RestletTestCase;

@RunWith(MockitoJUnitRunner.class)
public class BeerServerApplicationTest extends RestletTestCase {
	
	@Mock
	private TemperatureSensor temperatureSensor;
	
	@Mock
	private PowerSwitch powerSwitch;
	
	@Mock
	private Configuration config;
	
	@Mock
	private BeerController beerController;
	
	private BeerServerApplication sut;
	private Context context;
	
	@Before
	public void setUp() {
		context = new Context();
		context.getAttributes().put(TemperatureSensor.KEY, temperatureSensor);	
		context.getAttributes().put(PowerSwitch.KEY, powerSwitch);
		
		sut = new BeerServerApplication(context);
	}
	
	@Test
	public void testGetTemperature() {
		// Arrange
		doReturn(17.25f).when(temperatureSensor).getLastReading();
		
		Request request = new Request(Method.GET, "/garage/beer/fermenter/temperature");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals("{\"TemperatureSensor.temperature\":\"17.25\"}", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
	
	@Test
	public void testGetHeatbeltStatus() {
		// Arrange
		doReturn(State.OFF).when(powerSwitch).getPinState();
		
		Request request = new Request(Method.GET, "/garage/beer/heatbelt/status");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals("{\"PowerSwitch.status\":\"OFF\"}", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
}
