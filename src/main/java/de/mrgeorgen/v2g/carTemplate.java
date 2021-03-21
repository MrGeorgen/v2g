package de.mrgeorgen.v2g;

public class carTemplate {
	public final String model;
	public final int chargeSpeed;
	public final int fullBattery;
	public final int range;

	public carTemplate(String model, int fullBattery, int chargeSpeed, int range) {
		this.model = model;
		this.chargeSpeed = chargeSpeed;
		this.fullBattery = fullBattery;
		this.range = range;
	}
}
