package beer.rest.server;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class BeerServerComponent extends Component{

	public BeerServerComponent() {
		setName("BeerController");
		setDescription("Control the process of brewing beer.");
		setOwner("Krueger Brewing Corp");
		setAuthor("Sebastian Krueger");
		
		getServers().add(Protocol.HTTP, 8111);
		getDefaultHost().attachDefault(new BeerServerApplication());
	}
}
