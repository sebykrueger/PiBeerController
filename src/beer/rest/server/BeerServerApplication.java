package beer.rest.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BeerServerApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
	    Router router = new Router(getContext());
	    router.attach("/garage/beer/fermenter/temperature", TemperatureSensorServerResource.class);	// get
	    router.attach("/garage/beer/heatbelt/status", PowerSwitchServerResource.class); 			// get
	    router.attach("/garage/beer/controller/sleepinterval", BeerControllerServerResource.class); // get | put
	    router.attach("/garage/beer/controller/basetemp", BeerControllerServerResource.class); 		// get | put
	    router.attach("/garage/beer/controller/tolerance", BeerControllerServerResource.class);		// get | put
	    router.attach("/garage/beer/shutdown", PowerSwitchServerResource.class); 					// put
	    
	    return router;
	}
}
