package beer.rest.server;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import beer.gpio.controller.BeerController;

public class BeerControllerServerResource extends ServerResource {
	
	/**
	 * one of:
	 * 	- sleepinterval
	 * 	- basetemp
	 * 	- tolerance
	 */
	private String attribute;
	
	@Override
	public void doInit() throws ResourceException {
		attribute = getAttribute("attribute");
	}

	@Get ("txt")
	public String representation() {
		switch (attribute) {
		case "sleepinterval":
			return getSleepInterval();
			
		case "basetemp":
			return getBaseTemperature();
			
		case "tolerance":
			return getTolerance();

		default:
			throw new RuntimeException();
		}
	}
	
	@Put ("txt") 
	public void store(String value) {
		switch (attribute) {
		case "sleepinterval":
			setSleepInterval(value);
			
		case "basetemp":
			setBaseTemperature(value);
			
		case "tolerance":
			setTolerance(value);

		default:
			throw new RuntimeException();
		}
	}
	
	private String getSleepInterval() {
		return BeerController.getInstance().getSleepInterval() + "";
	}

	private void setSleepInterval(String sleepInterval) {
		BeerController.getInstance().setSleepInterval(Integer.parseInt(sleepInterval));
	}

	private String getBaseTemperature() {
		return BeerController.getInstance().getBaseTemperature() + "";
	}

	private void setBaseTemperature(String baseTemperature) {
		BeerController.getInstance().setBaseTemperature(Float.parseFloat(baseTemperature));
	}

	private String getTolerance() {
		return BeerController.getInstance().getTolerance() + "";
	}

	private void setTolerance(String tolerance) { 
		BeerController.getInstance().setTolerance(Float.parseFloat(tolerance));
	}
}
