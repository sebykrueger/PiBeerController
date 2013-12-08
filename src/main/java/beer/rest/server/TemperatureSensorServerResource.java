package beer.rest.server;

import org.json.simple.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import beer.gpio.device.TemperatureSensor;

public class TemperatureSensorServerResource extends AbstractServerResource {

	private static final String JSON_VALUE = TemperatureSensor.KEY + ".temperature";
	
	@SuppressWarnings("unchecked")
	@Get
	public JsonRepresentation getTemperature() {
		final JSONObject jsonObj = new JSONObject();
		jsonObj.put(JSON_VALUE, getTemperatureSensor().getLastReading().toString());
		
		return new JsonRepresentation(jsonObj);
	}
}
