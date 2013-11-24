package beer.rest.server;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import beer.gpio.controller.BeerController;

public class EternalActionsServerResource extends ServerResource{

	@Get
	public void shutdown() {
		BeerController.getInstance().shutdown();
	}
}
