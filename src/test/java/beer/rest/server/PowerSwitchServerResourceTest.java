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

import beer.gpio.device.PowerSwitch;
import beer.gpio.device.PowerSwitch.State;

@RunWith(MockitoJUnitRunner.class)
public class PowerSwitchServerResourceTest {

	@Mock
	private PowerSwitch powerSwitch;
	
	@Test
	public void testPinStateOn() {
		// Arrange
		doReturn(State.ON).when(powerSwitch).getPinState();
		
		final PowerSwitchServerResource resource = new PowerSwitchServerResource();
		
		final Request request = new Request(Method.GET, "/");
		final Response response = new Response(request);
		
		final Context context = new Context();
		context.getAttributes().put(PowerSwitch.KEY, powerSwitch);
		
		// Act
		resource.init(context, request, response);
		resource.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		assertEquals("ON", response.getEntityAsText());
	}
	
}
