package de.mrgeorgen.v2g;
import java.lang.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class carGrid {
	private final int id;
	public ArrayList<car> dockedCars = new ArrayList<car>();
	private final carTemplate[] models = {new carTemplate("Tesla Model 3", 50, 160, 335),
	new carTemplate("Renault Zoe ZE50", 52, 46, 315),
	new carTemplate("BMW i3", 38, 49, 235),
	new carTemplate("Nissan Leaf", 36, 46, 220),
	new carTemplate("Volkswagen ID.4", 150, 125, 400),
	new carTemplate("Tesla Model S Long Range", 90, 250, 555),
	new carTemplate("Smart EQ forfour", 17, 5, 95),
	new carTemplate("Honda e", 29, 56, 170)};
	public carGrid(int id) {
		this.id = id;
		Random random = new Random();
		for(carTemplate carModel : models) {
			final int numberOfCars = random.nextInt(10);
			for(int i = 0; i < numberOfCars; ++i) {
				dockedCars.add(new car(this, carModel));
			}
		}
	}
	public int chargeCars(int maxPower) {
		Collections.sort(dockedCars, new Comparator<car>() {
			public int compare(car car1, car car2) {
				Double car1RelativBattery = car1.getBatteryRelativ();
				Double car2RelativBattery = car2.getBatteryRelativ();
				return car1RelativBattery.compareTo(car2RelativBattery);
			}
		});
		if(maxPower < 0) Collections.reverse(dockedCars);
		int charged = 0;
		for(car car : dockedCars) {
			charged += car.charge(maxPower - charged);
		}
		if(powerGrid.logLevel >= 2 && charged != 0) System.out.println("carGrid " + id + " " + (charged < 0 ? "dis" : "") + "charged " + dockedCars.size() + " cars with " + (double)Math.abs(charged) / 1000 + " kW");
		return charged;
	}
	public int capacityDockedCars() {
		int maxBattery = 0;
		for(car car : dockedCars) {
			maxBattery += car.carModel.fullBattery;
		}
		return maxBattery;
	}
	public double relativeChargeState() {
		int battery = 0;
		for(car car : dockedCars) {
			battery += car.battery;
		}
		return (double)battery / capacityDockedCars();
	}
}
