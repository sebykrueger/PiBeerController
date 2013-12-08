package beer.rest.server;

import org.json.simple.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import beer.gpio.device.PowerSwitch;

public class PowerSwitchServerResource extends AbstractServerResource {
	
	private static final String JSON_VALUE = PowerSwitch.KEY + ".status"; 

	@SuppressWarnings("unchecked")
	@Get
	public JsonRepresentation getState() {
		final JSONObject jsonObj = new JSONObject();
		jsonObj.put(JSON_VALUE, getPowerSwitch().getPinState().name());
		
		return new JsonRepresentation(jsonObj);
	}
}
