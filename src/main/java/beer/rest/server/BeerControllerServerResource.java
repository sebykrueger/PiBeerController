package beer.rest.server;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

public class BeerControllerServerResource extends AbstractServerResource {
	
	private String attribute;
	
	@Override
	public void doInit() throws ResourceException {
		attribute = getAttribute("attribute");
	}

	@Get ("txt")
	public String representation() {
		switch (attribute) {
		case "sleepinterval":
			return getConfig().getSleepInterval().toString();
			
		case "basetemp":
			return getConfig().getBaseTemperature().toString();
			
		case "tolerance":
			return getConfig().getTolerance().toString();

		default:
			throw new RuntimeException();
		}
	}
	
	@Put ("txt") 
	public void store(String value) {
		switch (attribute) {
		case "sleepinterval":
			getConfig().setSleepInterval(Integer.parseInt(value));
			
		case "basetemp":
			getConfig().setBaseTemperature(Float.parseFloat(value));
			
		case "tolerance":
			getConfig().setTolerance(Float.parseFloat(value));

		default:
			throw new RuntimeException();
		}
	}
	
}
