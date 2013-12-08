package beer.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;
import beer.gpio.device.TemperatureSensor;

@RunWith(MockitoJUnitRunner.class)
public class BeerServerApplicationTest {
	
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
		context.getAttributes().put(Configuration.KEY, config);
		context.getAttributes().put(BeerController.KEY, beerController);
		
		sut = new BeerServerApplication(context);
		
//		doReturn(17.25f).when(temperatureSensor).getLastReading();
//		doReturn(State.OFF).when(powerSwitch).getPinState();
//		doReturn(35).when(config).getSleepInterval();
//		doReturn(25f).when(config).getBaseTemperature();
//		doReturn(26f).when(config).getTolerance();
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
	
	@Test
	public void testGetControllerAttribute_SleepInterval() {
		// Arrange
		doReturn(35).when(config).getSleepInterval();
		
		Request request = new Request(Method.GET, "/garage/beer/controller/sleepinterval");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals("{\"BeerController.sleepinterval\":\"35\"}", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
	
	@Test
	public void testPutControllerAttribute_SleepInterval() {
		// Arrange
		Request request = new Request(Method.PUT, "/garage/beer/controller/sleepinterval");
		request.setEntity("20", MediaType.TEXT_PLAIN);
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(config).setSleepInterval(20);
	}
	
	@Test
	public void testGetControllerAttribute_BaseTemp() {
		// Arrange
		doReturn(25f).when(config).getBaseTemperature();
		
		Request request = new Request(Method.GET, "/garage/beer/controller/basetemp");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals("{\"BeerController.basetemp\":\"25.0\"}", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
	
	@Test
	public void testPutControllerAttribute_BaseTemp() {
		// Arrange
		Request request = new Request(Method.PUT, "/garage/beer/controller/basetemp");
		request.setEntity("20", MediaType.TEXT_PLAIN);
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(config).setBaseTemperature(20f);
	}
	
	@Test
	public void testGetControllerAttribute_Tolerance() {
		// Arrange
		doReturn(26f).when(config).getTolerance();
		
		Request request = new Request(Method.GET, "/garage/beer/controller/tolerance");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals("{\"BeerController.tolerance\":\"26.0\"}", response.getEntityAsText());
		assertTrue(response.getStatus().isSuccess());
	}
	
	@Test
	public void testPutControllerAttribute_Tolerance() {
		// Arrange
		Request request = new Request(Method.PUT, "/garage/beer/controller/tolerance");
		request.setEntity("20", MediaType.TEXT_PLAIN);
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(config).setTolerance(20f);
	}
	
	@Test
	public void testGetControllerAttribute_Error() {
		// Arrange
		Request request = new Request(Method.GET, "/garage/beer/controller/doesnotexist");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals(response.getStatus().getCode(), 404);
		assertEquals(response.getStatus().getDescription(), "Attribute (doesnotexist) does not exist");
	}
	
	@Test
	public void testPutControllerAttribute_Error() {
		// Arrange
		Request request = new Request(Method.PUT, "/garage/beer/controller/doesnotexist");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertEquals(response.getStatus().getCode(), 404);
		assertEquals(response.getStatus().getDescription(), "Attribute (doesnotexist) does not exist");
	}
	
	@Test
	public void testShutdown() {
		// Arrange
		Request request = new Request(Method.GET, "/garage/beer/shutdown");
		Response response = new Response(request);
		
		// Act
		sut.handle(request, response);
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(beerController).shutdown();
	}
}
