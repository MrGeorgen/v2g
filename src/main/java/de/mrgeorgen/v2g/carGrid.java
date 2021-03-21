package de.mrgeorgen.v2g;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Double;
public class carGrid {
	public ArrayList<car> dockedCars = new ArrayList<car>();
	private final carTemplate[] models = {new carTemplate("Tesla Model 3", 50, 160, 335),
	new carTemplate("Renault Zoe ZE50", 52, 46, 315),
	new carTemplate("BMW i3", 38, 49, 235),
	new carTemplate("Nissan Leaf", 36, 46, 220),
	new carTemplate("Volkswagen ID.4", 150, 125, 400),
	new carTemplate("Tesla Model S Long Range", 90, 250, 555),
	new carTemplate("Smart EQ forfour", 17, 5, 95),
	new carTemplate("Honda e", 29, 56, 170)};
	public void fillWithCars() {
		Random random = new Random();
		for(carTemplate carModel : models) {
			final int numberOfCars = random.nextInt(10);
			for(int i = 0; i < numberOfCars; ++i) {
				dockedCars.add(new car(carModel.model, carModel.fullBattery, carModel.chargeSpeed, i));
			}
		}
	}
	public void chargeCars() {
		Collections.sort(dockedCars, new Comparator<car>() {
			public int compare(car car1, car car2) {
				Double car1RelativBattery = car1.getBatteryRelativ();
				Double car2RelativBattery = car2.getBatteryRelativ();
				return car1RelativBattery.compareTo(car2RelativBattery);
			}
		});
		if(powerGrid.energieAvailable < 0) Collections.reverse(dockedCars);
		dockedCars.forEach((car car) -> {
			car.charge();
		});
		if(powerGrid.energieAvailable > 0) System.out.println(powerGrid.energieAvailable + " W could not be used by the cars");
		else if(powerGrid.energieAvailable < 0) System.out.println(Math.abs(powerGrid.energieAvailable) + " W could not be taken from the cars");
	}
}
