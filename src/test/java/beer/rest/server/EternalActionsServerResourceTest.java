package beer.rest.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

import beer.gpio.controller.BeerController;

@RunWith(MockitoJUnitRunner.class)
public class EternalActionsServerResourceTest {
	
	@Mock
	private BeerController beerController;
	
	@Test
	public void testShutdown() {
		// Arrange
		final EternalActionsServerResource resource = new EternalActionsServerResource();
		
		final Request request = new Request(Method.GET, "/");
		final Response response = new Response(request);
		
		final Context context = new Context();
		context.getAttributes().put(BeerController.KEY, beerController);
		
		// Act
		resource.init(context, request, response);
		resource.handle();
		
		// Assert
		assertTrue(response.getStatus().isSuccess());
		verify(beerController).shutdown();
	}
}
