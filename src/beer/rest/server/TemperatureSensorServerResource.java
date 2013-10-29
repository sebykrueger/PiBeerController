package beer.rest.server;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import beer.gpio.controller.BeerController;

public class TemperatureSensorServerResource extends ServerResource {

	@Get ("txt")
	public String getTemperature() {
		return BeerController.getInstance().getTempSensor().getLastReading().toString();
	}
}
