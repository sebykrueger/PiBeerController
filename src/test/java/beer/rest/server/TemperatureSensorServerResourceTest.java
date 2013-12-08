package beer.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

import beer.gpio.device.TemperatureSensor;
import beer.gpio.exception.TemperatureSensorException;

@RunWith(MockitoJUnitRunner.class)
public class TemperatureSensorServerResourceTest {
	
	@Mock
	private TemperatureSensor tempSensor;
	
	@Test
	public void testGetLastReading() throws TemperatureSensorException {
		// Arrange
		doReturn(20f).when(tempSensor).getLastReading();
		
		final TemperatureSensorServerResource resource = new TemperatureSensorServerResource();
		
		final Request request = new Request(Method.GET, "/");
		final Response response = new Response(request);
		
		final Context context = new Context();
		context.getAttributes().put(TemperatureSensor.KEY, tempSensor);

		// Act
		resource.init(context, request, response);
		resource.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("{\"TemperatureSensor.temperature\":\"20.0\"}", response.getEntityAsText());
	}
}
