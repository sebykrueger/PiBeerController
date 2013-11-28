package beer.rest.server;

import org.restlet.resource.Get;

public class TemperatureSensorServerResource extends AbstractServerResource {

	@Get ("txt")
	public String getTemperature() {
		return getTemperatureSensor().getLastReading().toString();
	}
}
