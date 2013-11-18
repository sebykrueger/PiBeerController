package beer.rest.server;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import beer.gpio.controller.BeerController;

public class PowerSwitchServerResource extends ServerResource {

	@Get ("txt")
	public String getState() {
		return BeerController.getInstance().getPowerSwitch().getState().name();
	}
}
