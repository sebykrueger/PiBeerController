package beer.util;

import java.util.Arrays;

import beer.gpio.controller.Configuration;

public class ConfigurationFactory {

	public static Configuration fromCLI(String[] args) {
		System.out.println(Arrays.toString(args));
//		[-tolerance=1.5, -basetemp=25, -maxretries=5, -sleepinterval=10000]
		return new Configuration();
	}
}
