package beer.rest.server;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BeerServerApplication extends Application {
	
	public BeerServerApplication(final Context ctx) {
		super(ctx);
	}

	@Override
	public Restlet createInboundRoot() {
	    Router router = new Router(getContext());
	    router.attach("/garage/beer/fermenter/temperature", 	TemperatureSensorServerResource.class);
	    router.attach("/garage/beer/heatbelt/status", 			PowerSwitchServerResource.class);
	    router.attach("/garage/beer/controller/{attribute}", 	BeerControllerConfigurationServerResource.class);
	    router.attach("/garage/beer/shutdown", 					EternalActionsServerResource.class);
	    // TODO: need a command to start and stop a batch and store all temperature readings with batch name
	    
	    return router;
	}
}
