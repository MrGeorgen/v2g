package de.mrgeorgen.v2g;
import java.util.Random;
public class powerGrid {
	public static int energieAvailable;
	public static int savedEnergie;
	public static void main(String args[]) {
		if(args.length != 1) {
			System.out.println("Invalid Syntax. Use the number of days the simulation shell run as the first argument");
			return;
		}
		final carGrid carGrid = new carGrid();
		carGrid.fillWithCars();
		car[] allCars = new car[carGrid.dockedCars.size()];
		allCars = carGrid.dockedCars.toArray(allCars);
		final int hourSimulationRuns = Integer.parseInt(args[0]) * 24;
		Random random = new Random();
		for(int houresPassed = 0; houresPassed < hourSimulationRuns; ++houresPassed) {
			final int hourOfDay = houresPassed % 24;
			System.out.println("Day " + houresPassed / 24 + " Hour " + hourOfDay);
			int averageEnergie;
			if(hourOfDay < 6 || hourOfDay > 22) averageEnergie = carGrid.dockedCars.size() * 3000; // energie available at night
			else if(hourOfDay < 7) averageEnergie = carGrid.dockedCars.size() * -1000; // energie for light in the morning
			else if(hourOfDay < 19) averageEnergie = carGrid.dockedCars.size() * -500; // at the day energie is still needed but not as much because the lights are not on
			else /* hourOfDay > 19 && hourOfDay < 23 */ averageEnergie = carGrid.dockedCars.size() * -1000; // the lights are on again
			energieAvailable = (int)(averageEnergie * (0.5 + 1.5 * random.nextDouble()));
			carGrid.chargeCars();
		}
		System.out.println("vehicle to grid saved " + (double)savedEnergie / 1000 + " kWh with " + carGrid.dockedCars.size() + " cars");
	}
}
