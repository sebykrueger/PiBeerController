package beer.rest.server;

import org.restlet.resource.Get;

public class PowerSwitchServerResource extends AbstractServerResource {

	@Get ("txt")
	public String getState() {
		return getPowerSwitch().getPinState().name();
	}
}
