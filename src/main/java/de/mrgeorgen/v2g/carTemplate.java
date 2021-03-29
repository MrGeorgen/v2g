package de.mrgeorgen.v2g;

public class carTemplate {
	public final String model;
	public final int chargeSpeed;
	public final int fullBattery;
	public final int range;

	public carTemplate(String model, int fullBattery, int chargeSpeed, int range) {
		this.fullBattery = 1000 * fullBattery; // convert kWh to Wh
		this.chargeSpeed = chargeSpeed * 1000; // convert kW to W
		this.model = model;
		this.range = range;
	}
}
