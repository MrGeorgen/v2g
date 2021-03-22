package de.mrgeorgen.v2g;

public class car {
	private int battery;
    	private final int fullBattery;
	private int chargeLock;
	private final int chargeSpeed;
	private final String model;
	private final int id;

	public car(String model, int fullBattery, int chargeSpeed, int id) { // id can be the same for different models
		this.fullBattery = 1000 * fullBattery; // convert kWh to Wh
		this.chargeSpeed = chargeSpeed * 1000; // convert kW to W
		this.setChargeLock(0.6);
		this.id = id;
		this.model = model;
		this.battery = this.fullBattery / 2;
	}
	public void charge() {
		int chargeAmmount = Math.abs(powerGrid.energieAvailable) < this.chargeSpeed ? powerGrid.energieAvailable : powerGrid.energieAvailable > 0 ? this.chargeSpeed : -1 * this.chargeSpeed;
		if(this.battery + chargeAmmount <= this.chargeLock && chargeAmmount < 0) chargeAmmount = (this.battery - this.chargeLock) * -1; // do not charge if the car gets under the charge lock
		if(this.battery + chargeAmmount > this.fullBattery) chargeAmmount = this.fullBattery - this.battery; // prevent the battery from overcharging
		if(this.battery + chargeAmmount < 0) chargeAmmount = this.battery * -1; // prevent the battery from dischargingunder 0%
		if(chargeAmmount != 0) System.out.println(this.model + " nr. " + this.id + " is " + (chargeAmmount < 0 ? "dis" : "") + "charging with " + (double)Math.abs(chargeAmmount) / 1000 +  " kW. battery: " + (double)this.battery / 1000 + "/" + (double)this.fullBattery / 1000 + " kWh (" + Math.round(getBatteryRelativ() * 100) + "%)");
		this.battery += chargeAmmount;
		powerGrid.energieAvailable -= chargeAmmount;
		if(chargeAmmount < 0) powerGrid.savedEnergie += Math.abs(chargeAmmount);
    	}
	private void setChargeLock(double chargeLock) {
		this.chargeLock = (int)(this.fullBattery * chargeLock);
	}
	public double getBatteryRelativ() {
		return (double)this.battery / this.fullBattery;
	}
}
