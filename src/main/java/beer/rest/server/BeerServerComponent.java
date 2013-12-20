package beer.rest.server;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.TemperatureSensor;

public class BeerServerComponent extends Component {

	public BeerServerComponent(final PowerSwitch powerSwitch, final TemperatureSensor tempSensor,
			final BeerController beerController, final Configuration config) {
		setName("BeerController");
		setDescription("Control the process of brewing beer.");
		setOwner("Krueger Brewing Corp");
		setAuthor("Sebastian Krueger");

		Server server = getServers().add(Protocol.HTTPS, 8138);
//		Series<Parameter> parameters = server.getContext().getParameters();
//		parameters.add("keystorePath", "......");
//		parameters.add("keystorePassword","password");
//		parameters.add("keystoreType","JKS");
//		parameters.add("keyPassword","password");
		// TODO Need to configure the SSL keystores
		
		server.getContext().getParameters().set("tracing", "true");
		
		Context childContext = getContext().createChildContext();
		childContext.getAttributes().put(PowerSwitch.KEY, powerSwitch);
		childContext.getAttributes().put(TemperatureSensor.KEY, tempSensor);
		childContext.getAttributes().put(BeerController.KEY, beerController);
		childContext.getAttributes().put(Configuration.KEY, config);
		
		getDefaultHost().attachDefault(new BeerServerApplication(childContext));
	}

}
