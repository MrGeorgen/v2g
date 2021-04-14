package de.mrgeorgen.v2g;

public class car {
	private final int id;
	public final carTemplate carModel;
	public int battery;
	private int chargeLock;

	public car(carTemplate carModel) { // id can be the same for different models
		this.carModel = carModel;
		setChargeLock(0.6);
		id = powerGrid.idCounter++;
		battery = carModel.fullBattery / 2;
	}
	public int charge(int maxCharge) {
		int chargeAmmount = Math.abs(maxCharge) < carModel.chargeSpeed ? maxCharge : maxCharge > 0 ? carModel.chargeSpeed : -1 * carModel.chargeSpeed;
		if(battery + chargeAmmount <= chargeLock && chargeAmmount < 0) chargeAmmount = (battery - chargeLock) * -1; // do not charge if the car gets under the charge lock
		if(battery + chargeAmmount > carModel.fullBattery) chargeAmmount = carModel.fullBattery - battery; // prevent the battery from overcharging
		if(battery + chargeAmmount < 0) chargeAmmount = battery * -1; // prevent the battery from dischargingunder 0%
		if(powerGrid.logLevel >= 3 && chargeAmmount != 0) System.out.println(carModel.model + " id. " + id + " is " + (chargeAmmount < 0 ? "dis" : "") + "charging with " + (double)Math.abs(chargeAmmount) / 1000 +  " kW. battery: " + (double)battery / 1000 + "/" + (double)carModel.fullBattery / 1000 + " kWh (" + Math.round(getBatteryRelativ() * 100) + "%)");
		battery += chargeAmmount;
		if(chargeAmmount < 0) powerGrid.savedEnergie += Math.abs(chargeAmmount);
		return chargeAmmount;
    	}
	private void setChargeLock(double chargeLock) {
		chargeLock = (int)(carModel.fullBattery * chargeLock);
	}
	public double getBatteryRelativ() {
		return (double)battery / carModel.fullBattery;
	}
}
