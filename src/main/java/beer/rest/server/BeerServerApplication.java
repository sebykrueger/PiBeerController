package beer.rest.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BeerServerApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
	    Router router = new Router(getContext());
	    router.attach("/garage/beer/fermenter/temperature", 	TemperatureSensorServerResource.class);
	    router.attach("/garage/beer/heatbelt/status", 			PowerSwitchServerResource.class);
	    router.attach("/garage/beer/controller/{attribute}", 	BeerControllerServerResource.class);
	    router.attach("/garage/beer/shutdown", 					EternalActionsServerResource.class);
	    
	    return router;
	}
}
