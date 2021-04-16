package de.mrgeorgen.v2g;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
public class powerGrid {
	public static int logLevel;
	public static void main(String args[]) {
		if(args.length != 2) {
			System.out.println("Invalid Syntax. Use the number of days the simulation shell run as the first argument and the logLevel as the second. level 1 for only errors. level 2 for information about the carGrid and level 3 for car informations");
			return;
		}
		logLevel = Integer.parseInt(args[1]);
		Random random = new Random();
		ArrayList<car> allCars = new ArrayList<car>();
		ArrayList<carGrid> carGrids = new ArrayList<carGrid>();
		final int numberOfCarGrids = 2 + random.nextInt(10);
		for(int i = 0; i < numberOfCarGrids; ++i) {
			final carGrid carGrid = new carGrid(i);
			carGrids.add(carGrid);
			allCars.addAll(carGrid.dockedCars);
		}
		// done after initlising the car because when the car constructor is called not all carGrids are created yet
		for(car car : allCars) {
			car.workCarGrid = carGrids.get(random.nextInt(carGrids.size()));
		}
		final int hourSimulationRuns = Integer.parseInt(args[0]) * 24;
		int savedEnergie = 0;
		for(int houresPassed = 0; houresPassed < hourSimulationRuns; ++houresPassed) {
			final int hourOfDay = houresPassed % 24;
			if(logLevel >= 2) System.out.println("Day " + houresPassed / 24 + " Hour " + hourOfDay);
			int averagePower;
			if(hourOfDay < 6 || hourOfDay > 22) averagePower = allCars.size() * 4000; // energie available at night
			else if(hourOfDay < 7) averagePower = allCars.size() * -1000; // energie for light in the morning
			else if(hourOfDay < 19) averagePower = allCars.size() * -500; // at the day energie is still needed but not as much because the lights are not on
			else /* hourOfDay > 19 && hourOfDay < 23 */ averagePower = allCars.size() * -1000; // the lights are on again
			int powerAvailable = (int)(averagePower * (0.5 + 1.5 * random.nextDouble()));
			final int energieBeforeCharging = powerAvailable;
			Collections.sort(carGrids, new Comparator<carGrid>() {
				public int compare(carGrid carGrid1, carGrid carGrid2) {
					Double carGrid1RelativChargeState = carGrid1.relativeChargeState();
					Double carGrid2RelativBattery = carGrid2.relativeChargeState();
					return carGrid1RelativChargeState.compareTo(carGrid2RelativBattery);
				}
			});
			if(powerAvailable < 0) Collections.reverse(carGrids);
			for(carGrid carGrid : carGrids) {
				powerAvailable -= carGrid.chargeCars(powerAvailable);
			}
			final int chargePower = energieBeforeCharging - powerAvailable;
			if(chargePower < 0) savedEnergie += Math.abs(chargePower);
			if(logLevel >= 1) {
				if(powerAvailable > 0) System.out.println(powerAvailable + " W could not be used by the cars");
				else if(powerAvailable < 0) System.out.println(Math.abs(powerAvailable) + " W could not be taken from the cars");
			}
			for(car car : allCars) {
				car.tickDrive(hourOfDay);
			}
		}
		System.out.println("vehicle to grid saved " + (double)savedEnergie / 1000 + " kWh with " + allCars.size() + " cars and " + carGrids.size() + " carGrids");
	}
}
