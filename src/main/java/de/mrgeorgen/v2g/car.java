package de.mrgeorgen.v2g;

public class car {
	private final int id;
	private final carTemplate carModel;
	private int battery;
	private int chargeLock;

	public car(carTemplate carModel, int id) { // id can be the same for different models
		this.carModel = carModel;
		this.setChargeLock(0.6);
		this.id = id;
		this.battery = this.carModel.fullBattery / 2;
	}
	public void charge() {
		int chargeAmmount = Math.abs(powerGrid.energieAvailable) < this.carModel.chargeSpeed ? powerGrid.energieAvailable : powerGrid.energieAvailable > 0 ? this.carModel.chargeSpeed : -1 * this.carModel.chargeSpeed;
		if(this.battery + chargeAmmount <= this.chargeLock && chargeAmmount < 0) chargeAmmount = (this.battery - this.chargeLock) * -1; // do not charge if the car gets under the charge lock
		if(this.battery + chargeAmmount > this.carModel.fullBattery) chargeAmmount = this.carModel.fullBattery - this.battery; // prevent the battery from overcharging
		if(this.battery + chargeAmmount < 0) chargeAmmount = this.battery * -1; // prevent the battery from dischargingunder 0%
		if(chargeAmmount != 0) System.out.println(this.carModel.model + " nr. " + this.id + " is " + (chargeAmmount < 0 ? "dis" : "") + "charging with " + (double)Math.abs(chargeAmmount) / 1000 +  " kW. battery: " + (double)this.battery / 1000 + "/" + (double)this.carModel.fullBattery / 1000 + " kWh (" + Math.round(getBatteryRelativ() * 100) + "%)");
		this.battery += chargeAmmount;
		powerGrid.energieAvailable -= chargeAmmount;
		if(chargeAmmount < 0) powerGrid.savedEnergie += Math.abs(chargeAmmount);
    	}
	private void setChargeLock(double chargeLock) {
		this.chargeLock = (int)(this.carModel.fullBattery * chargeLock);
	}
	public double getBatteryRelativ() {
		return (double)this.battery / this.carModel.fullBattery;
	}
}
