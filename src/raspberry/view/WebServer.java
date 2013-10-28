package raspberry.view;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import raspberry.model.TemperatureSensor;

@WebService
public class WebServer {

	public static void main(String[] args) {
		Endpoint.publish("http://192.168.1.58:9999/ws/hello", new WebServer());
		System.out.println("WebServer is ready!");
	}

	@WebMethod
	public float getTempInGarage() {
		return new TemperatureSensor().read();
	}
}
