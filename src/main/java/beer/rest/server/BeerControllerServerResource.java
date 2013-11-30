package beer.rest.server;

import java.util.logging.Logger;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

public class BeerControllerServerResource extends AbstractServerResource {
	
	private static final Logger LOG = Logger.getLogger(BeerControllerServerResource.class.getName());
	
	private String attribute;
	
	@Override
	public void doInit() throws ResourceException {
		attribute = getAttribute("attribute");
	}

	@Get("txt")
	public String representation() {
		switch (attribute) {
		case "sleepinterval":
			return getConfig().getSleepInterval().toString();
			
		case "basetemp":
			return getConfig().getBaseTemperature().toString();
			
		case "tolerance":
			return getConfig().getTolerance().toString();

		default:
			LOG.severe("Attribute (" + attribute + ") does not exist");
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Attribute (" + attribute + ") does not exist");
			return "";
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
