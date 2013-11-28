package beer.gpio.controller;

public class Configuration {

	public static final String KEY = "Configuration";
	
	private volatile Integer sleepInterval = 60000;
	private volatile Float baseTemperature = 18f;
	private volatile Float tolerance = 0.5f;
	private volatile Integer maxRetries = 10;

	public Integer getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(final Integer sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

	public Float getBaseTemperature() {
		return baseTemperature;
	}

	public void setBaseTemperature(final Float baseTemperature) {
		this.baseTemperature = baseTemperature;
	}

	public Float getTolerance() {
		return tolerance;
	}

	public void setTolerance(final Float tolerance) {
		this.tolerance = tolerance;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(final Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

}
