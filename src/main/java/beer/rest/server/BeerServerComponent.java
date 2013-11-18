package beer.rest.server;

import org.restlet.Component;
import org.restlet.data.Protocol;

import beer.gpio.controller.BeerController;

public class BeerServerComponent extends Component{

	public BeerServerComponent() {
		setName("BeerController");
		setDescription("Control the process of brewing beer.");
		setOwner("Krueger Corp");
		setAuthor("Sebastian Krueger");
		
		getServers().add(Protocol.HTTP, 8111);
		getDefaultHost().attachDefault(new BeerServerApplication());
	}
	
	public static void main(String[] args) throws Exception {
		new BeerServerComponent().start();
		BeerController.getInstance().start();
	}
}
