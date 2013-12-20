package beer.rest;

import org.junit.After;
import org.junit.Before;
import org.restlet.engine.Engine;

import beer.BeerTestCase;

public abstract class RestletTestCase extends BeerTestCase {

    @Before
    public void setUpEngine() {
        Engine.clearThreadLocalVariables();

        // Restore a clean engine
        org.restlet.engine.Engine.register();

        // Prefer the internal connectors
        Engine.getInstance()
                .getRegisteredServers()
                .add(0, new org.restlet.engine.connector.HttpServerHelper(null));
        Engine.getInstance()
                .getRegisteredClients()
                .add(0, new org.restlet.engine.connector.HttpClientHelper(null));
    }

    @After
    public void tearDownEngine() {
        Engine.clearThreadLocalVariables();
    }
}