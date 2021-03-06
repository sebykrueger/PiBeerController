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

public class BeerControllerConfigurationServerResource extends AbstractServerResource {

	private static final Logger LOG = Logger.getLogger(BeerControllerConfigurationServerResource.class.getName());
	
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

		case "maxretries":
			jsonObj.put(JSON_VALUE, getConfig().getMaxRetries().toString());
			return new JsonRepresentation(jsonObj);
			
		default:
			setClientError("Attribute (" + attribute + ") does not exist");
			return new JsonRepresentation(new JSONObject());
		}
	}

	@Put("txt")
	public void store(String input) {
		this.input = input;

		switch (attribute) {
		case "sleepinterval":
			if (! validInput()) break;
			setSleepInterval();
			break;

		case "basetemp":
			if (! validInput()) break;
			setBaseTemp();
			break;

		case "tolerance":
			if (! validInput()) break;
			setTolerance();
			break;

		case "maxretries":
			if (! validInput()) break;
			setMaxRetries();
			break;
			
		default:
			setClientError("Attribute (" + attribute + ") does not exist");
		}
	}

	private boolean validInput() {
		if ( ! validateInputNotNull()) return false;
		if ( ! validJSONString()) return false;
		if ( ! validJSONObject()) return false;
		return true;
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
	
	private void setMaxRetries() {
		final String value = validateJSONInput("BeerController.maxretries");
		if (value == null) return;
		
		try {
			getConfig().setMaxRetries(Integer.parseInt(value));
		} catch (NumberFormatException ex) {
			setClientError("Invalid Input: " + input + " does not contain a valid Integer.");
		}
	}

	private void setClientError(String errorMessage) {
		LOG.severe(errorMessage);
		getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, errorMessage);
	}
	
	private boolean validateInputNotNull() {
		if (input == null) {
			setClientError("No JSON input provided.");
			return false;
		}
		return true;
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
