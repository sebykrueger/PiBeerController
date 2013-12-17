package beer.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

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

import beer.gpio.controller.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class BeerControllerServerResourceTest {
	
	private static final int SLEEP_INTERVAL_MILLIS = 100;
	private static final float TOLERANCE = 0.5f;
	private static final int MAX_RETRIES = 10;
	private static final float BASE_TEMP = 18f;
	
	@Mock
	private Configuration config;
	
	private BeerControllerServerResource sut;
	
	private Map<String, Object> requestAttributes;
	private Request request;
	private Context context;
	private Response response;
	
	@Before
	public void setUp() {
		doReturn(BASE_TEMP).when(config).getBaseTemperature();
		doReturn(SLEEP_INTERVAL_MILLIS).when(config).getSleepInterval();
		doReturn(MAX_RETRIES).when(config).getMaxRetries();
		doReturn(TOLERANCE).when(config).getTolerance();
		
		requestAttributes = new HashMap<String, Object>();
		
		sut = new BeerControllerServerResource();
		
		context = new Context();
		context.getAttributes().put(Configuration.KEY, config);
	}
	
	@Test
	public void testGetBaseTemp() {
		// Arrange
		request = new Request(Method.GET, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "basetemp");
		request.setAttributes(requestAttributes);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("{\"BeerController.basetemp\":\"" + BASE_TEMP + "\"}", response.getEntityAsText());			
	}
	
	@Test
	public void testGetTolerance() {
		// Arrange
		request = new Request(Method.GET, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "tolerance");
		request.setAttributes(requestAttributes);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("{\"BeerController.tolerance\":\"" + TOLERANCE + "\"}", response.getEntityAsText());		
	}
	
	@Test
	public void testGetError() {
		// Arrange
		request = new Request(Method.GET, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "doesnotexist");
		request.setAttributes(requestAttributes);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertEquals(response.getStatus().getCode(), 404);
		assertEquals(response.getStatus().getDescription(), "Attribute (doesnotexist) does not exist");
	}
	
	@Test
	public void testGetSleepInterval() {
		// Arrange
		request = new Request(Method.GET, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "sleepinterval");
		request.setAttributes(requestAttributes);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("{\"BeerController.sleepinterval\":\"" + SLEEP_INTERVAL_MILLIS + "\"}", response.getEntityAsText());
	}
	
	@Test
	public void testPutSleepInterval() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "sleepinterval");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.sleepinterval\":\"" + SLEEP_INTERVAL_MILLIS + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(config).setSleepInterval(SLEEP_INTERVAL_MILLIS);
	}
	
	@Test
	public void testPutSleepInterval_Fail_NotJSON() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "sleepinterval");
		request.setAttributes(requestAttributes);
		request.setEntity("20", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutSleepInterval_Fail_NotInteger() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "sleepinterval");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.sleepinterval\":\"" + SLEEP_INTERVAL_MILLIS + ".0\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutSleepInterval_Fail_InvalidJSON() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "sleepinterval");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.sleepinterval\":\"" + SLEEP_INTERVAL_MILLIS, MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutSleepInterval_Fail_InvalidJSONKey() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "sleepinterval");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.doesnotexist\":\"" + SLEEP_INTERVAL_MILLIS + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutBaseTemp() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "basetemp");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.basetemp\":\"" + BASE_TEMP + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(config).setBaseTemperature(BASE_TEMP);		
	}
	
	@Test
	public void testPutBaseTemp_Fail_NotFloat() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "basetemp");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.basetemp\":\"" + BASE_TEMP + "x\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutBaseTemp_Fail_InvalidJSONKey() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "basetemp");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.doesnotexist\":\"" + BASE_TEMP + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutTolerance() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "tolerance");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.tolerance\":\"" + TOLERANCE + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(config).setTolerance(TOLERANCE);		
	}
	
	@Test
	public void testPutTolerance_Fail_NotFloat() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "tolerance");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.tolerance\":\"" + TOLERANCE + "x\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutTolerance_Fail_InvalidJSONKey() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "tolerance");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.doesnotexist\":\"" + TOLERANCE + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertTrue(response.getStatus().isClientError());
	}
	
	@Test
	public void testPutError() {
		// Arrange
		request = new Request(Method.PUT, "/");
		response = new Response(request);
		
		requestAttributes.put("attribute", "doesnotexist");
		request.setAttributes(requestAttributes);
		request.setEntity("{\"BeerController.tolerance\":\"" + TOLERANCE + "\"}", MediaType.TEXT_PLAIN);
		
		// Act
		sut.init(context, request, response);
		sut.handle();
		
		// Assert
		assertEquals(response.getStatus().getCode(), 404);
		assertEquals(response.getStatus().getDescription(), "Attribute (doesnotexist) does not exist");
	}
}
