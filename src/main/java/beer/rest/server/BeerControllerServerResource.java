package beer.rest.server;

import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import beer.gpio.controller.BeerController;

public class BeerControllerServerResource extends AbstractServerResource {

	private static final Logger LOG = Logger.getLogger(BeerControllerServerResource.class.getName());
	
	private	JSONObject jsonObj;
	private String input;

	private String attribute;

	@Override
	public void doInit() throws ResourceException {
		attribute = getAttribute("attribute");
	}

	/* Override so we can see exceptions in the log */
	@Override
	protected Representation doHandle() throws ResourceException {
		try {
			return super.doHandle();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	@Get
	public JsonRepresentation representation() {
		final String JSON_VALUE = BeerController.KEY + "." + attribute;
		final JSONObject jsonObj = new JSONObject();

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
	public void store(String input) {
		this.input = input;
		
		if ( ! validJSONString()) return;
		if ( ! validJSONObject()) return;

		switch (attribute) {
		case "sleepinterval":
			setSleepInterval();
			break;

		case "basetemp":
			setBaseTemp();
			break;

		case "tolerance":
			setTolerance();
			break;

		default:
			setClientError("Attribute (" + attribute + ") does not exist");
		}
	}

	private void setSleepInterval() {
		final String value = validateJSONInput("BeerController.sleepinterval");
		if (value == null) return;
		
		try {
			getConfig().setSleepInterval(Integer.parseInt(value));
		} catch (NumberFormatException ex) {
			setClientError("Invalid Input: " + input + " does not contain a valid Integer.");
		}
	}
	
	private void setBaseTemp() {
		final String value = validateJSONInput("BeerController.basetemp");
		if (value == null) return;
		
		try {
			getConfig().setBaseTemperature(Float.parseFloat(value));
		} catch (NumberFormatException ex) {
			setClientError("Invalid Input: " + input + " does not contain a valid Float.");
		}
	}
	
	private void setTolerance() {
		final String value = validateJSONInput("BeerController.tolerance");
		if (value == null) return;
		
		try {
			getConfig().setTolerance(Float.parseFloat(value));
		} catch (NumberFormatException ex) {
			setClientError("Invalid Input: " + input + " does not contain a valid Float.");
		}
	}

	private void setClientError(String errorMessage) {
		LOG.severe(errorMessage);
		getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, errorMessage);
	}
	
	private boolean validJSONObject() {
		try {
			jsonObj = (JSONObject) JSONValue.parse(input);
		} catch (ClassCastException ex) {
			setClientError("Invalid Input: " + input + " is not valid JSON.");
			return false;
		}
		return true;
	}

	private boolean validJSONString() {
		try {
			new JSONParser().parse(this.input);
		} catch (ParseException pe) {
			setClientError("Invalid Input: " + input + ". JSON Validation Error: " + pe);
			return false;
		}
		return true;
	}

	private String validateJSONInput(String jsonKey) {
		final String value = (String) jsonObj.get(jsonKey);
		if (value == null) {
			String errorMessage = "Invalid Input: " + input + " does not contain " + jsonKey + " as JSON key.";
			LOG.severe(errorMessage);
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, errorMessage);
		}
		return value;
	}

}
