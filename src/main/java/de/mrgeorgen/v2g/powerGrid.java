package de.mrgeorgen.v2g;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
public class powerGrid {
	public static int energieAvailable;
	public static int savedEnergie;
	private static ArrayList<car> allCars = new ArrayList<car>();
	private static ArrayList<carGrid> carGrids = new ArrayList<carGrid>();
	public static void main(String args[]) {
		if(args.length != 1) {
			System.out.println("Invalid Syntax. Use the number of days the simulation shell run as the first argument");
			return;
		}
		Random random = new Random();
		final int numberOfCarGrids = 2 + random.nextInt(10);
		for(int i = 0; i < numberOfCarGrids; ++i) {
			final carGrid carGrid = new carGrid();
			carGrids.add(carGrid);
			allCars.addAll(carGrid.dockedCars);
		}
		final int hourSimulationRuns = Integer.parseInt(args[0]) * 24;
		for(int houresPassed = 0; houresPassed < hourSimulationRuns; ++houresPassed) {
			final int hourOfDay = houresPassed % 24;
			System.out.println("Day " + houresPassed / 24 + " Hour " + hourOfDay);
			int averageEnergie;
			if(hourOfDay < 6 || hourOfDay > 22) averageEnergie = allCars.size() * 3000; // energie available at night
			else if(hourOfDay < 7) averageEnergie = allCars.size() * -1000; // energie for light in the morning
			else if(hourOfDay < 19) averageEnergie = allCars.size() * -500; // at the day energie is still needed but not as much because the lights are not on
			else /* hourOfDay > 19 && hourOfDay < 23 */ averageEnergie = allCars.size() * -1000; // the lights are on again
			energieAvailable = (int)(averageEnergie * (0.5 + 1.5 * random.nextDouble()));
			Collections.sort(carGrids, new Comparator<carGrid>() {
				public int compare(carGrid carGrid1, carGrid carGrid2) {
					Double carGrid1RelativChargeState = carGrid1.relativeChargeState();
					Double carGrid2RelativBattery = carGrid2.relativeChargeState();
					return carGrid1RelativChargeState.compareTo(carGrid2RelativBattery);
				}
			});
			if(energieAvailable < 0) Collections.reverse(carGrids);
			for(carGrid carGrid : carGrids) {
				energieAvailable -= carGrid.chargeCars(energieAvailable);
			}
			if(energieAvailable > 0) System.out.println(energieAvailable + " W could not be used by the cars");
			else if(energieAvailable < 0) System.out.println(Math.abs(energieAvailable) + " W could not be taken from the cars");
		}
		System.out.println("vehicle to grid saved " + (double)savedEnergie / 1000 + " kWh with " + allCars.size() + " cars");
	}
}
