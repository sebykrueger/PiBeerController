import java.util.logging.Logger;

import beer.gpio.controller.BeerController;
import beer.gpio.controller.Configuration;
import beer.gpio.device.PowerSwitch;
import beer.gpio.device.TemperatureSensor;
import beer.rest.server.BeerServerComponent;


public class Main {
	
	private static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws Exception {
		System.out.println("    ____                    ______            __             ____         ");
		System.out.println("   / __ )___  ___  _____   / ____/___  ____  / /__________  / / /__  _____");
		System.out.println("  / __  / _ \\/ _ \\/ ___/  / /   / __ \\/ __ \\/ __/ ___/ __ \\/ / / _ \\/ ___/");
		System.out.println(" / /_/ /  __/  __/ /     / /___/ /_/ / / / / /_/ /  / /_/ / / /  __/ /    ");
		System.out.println("/_____/\\___/\\___/_/      \\____/\\____/_/ /_/\\__/_/   \\____/_/_/\\___/_/     ");
		System.out.println();
		System.out.println("Copyleft \u00a9 Sebastian Krueger, November 2013.");
		System.out.println(); 
		                                                           
		log.info("BeerController starting GPIO...");
		PowerSwitch powerSwitch = null;
		TemperatureSensor tempSensor = null;
		try {
			powerSwitch = new PowerSwitch();
			tempSensor = new TemperatureSensor();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.severe("Game Over!");
			System.exit(0);
		}
		
		final Configuration config = new Configuration();
		final BeerController beerController = new BeerController(powerSwitch, tempSensor, config);
		new Thread(beerController).start();
		
		
		log.info("BeerController starting REST Server...");
		new BeerServerComponent(powerSwitch, tempSensor, beerController, config).start();
	}

}
