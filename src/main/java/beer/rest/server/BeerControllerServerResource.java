package beer.rest.server;

import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import beer.gpio.controller.BeerController;

public class BeerControllerServerResource extends AbstractServerResource {
	
	private static final Logger LOG = Logger.getLogger(BeerControllerServerResource.class.getName());
	
	private String attribute;
	
	@Override
	public void doInit() throws ResourceException {
		attribute = getAttribute("attribute");
	}

	@SuppressWarnings("unchecked")
	@Get
	public JsonRepresentation representation() {
		final String JSON_VALUE = BeerController.KEY + "." + attribute;
		JSONObject jsonObj = new JSONObject();
		
		switch (attribute) {
		case "sleepinterval":
			jsonObj.put(JSON_VALUE, getConfig().getSleepInterval().toString());
			return new JsonRepresentation(jsonObj);
			
		case "basetemp":
			jsonObj.put(JSON_VALUE, getConfig().getBaseTemperature().toString());
			return new JsonRepresentation(jsonObj);
			
		case "tolerance":
			jsonObj.put(JSON_VALUE, getConfig().getTolerance().toString());
			return new JsonRepresentation(jsonObj);

		default:
			LOG.severe("Attribute (" + attribute + ") does not exist");
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Attribute (" + attribute + ") does not exist");
			return new JsonRepresentation(new JSONObject());
		}
	}
	
	@Put("txt") 
	public void store(String value) {
		switch (attribute) {
		case "sleepinterval":
			getConfig().setSleepInterval(Integer.parseInt(value));
			break;
			
		case "basetemp":
			getConfig().setBaseTemperature(Float.parseFloat(value));
			break;
			
		case "tolerance":
			getConfig().setTolerance(Float.parseFloat(value));
			break;

		default:
			LOG.severe("Attribute (" + attribute + ") does not exist");
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Attribute (" + attribute + ") does not exist");
		}
	}
	
}
