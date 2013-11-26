package beer.rest.server;

import org.restlet.resource.Get;

public class EternalActionsServerResource extends AbstractServerResource {

	@Get
	public void shutdown() {
		getBeerController().shutdown();
	}
}
