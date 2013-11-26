package beer.rest.server;

import org.restlet.resource.ServerResource;

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.TemperatureSensor;

public class AbstractServerResource extends ServerResource {

	protected Configuration getConfig() {
		return getContextAttribute(Configuration.KEY);
	}

	protected PowerSwitch getPowerSwitch() {
		return getContextAttribute(PowerSwitch.KEY);
	}

	protected TemperatureSensor getTemperatureSensor() {
		return getContextAttribute(TemperatureSensor.KEY);
	}
	
	protected BeerController getBeerController() {
		return getContextAttribute(BeerController.KEY);
	}

	@SuppressWarnings("unchecked")
	private <T> T getContextAttribute(final String key) {
		return (T) getContext().getAttributes().get(key);
	}

}
