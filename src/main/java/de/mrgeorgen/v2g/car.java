package de.mrgeorgen.v2g;
import java.util.Random;

public class car {
	private static int idCounter;
	public final carTemplate carModel;
	public int battery;
	private int chargeLock;
	private final int drivesToWorkHour;
	private final int drivesFromWorkHour;
	private final carGrid homeCarGrid;
	public carGrid workCarGrid;
	private final int driveToWorkPower;
	private final int hoursToWork;
	private boolean stuck;
	private final String carName;
	private final int speed;
	private final int workDistance;

	public car(carGrid homeCarGrid, carTemplate carModel) {
		this.homeCarGrid = homeCarGrid;
		this.carModel = carModel;
		chargeLock = (int)(carModel.fullBattery * 0.6);
		carName = carModel.model + " id. " + idCounter++;
		battery = carModel.fullBattery / 2;
		Random random = new Random();
		drivesToWorkHour = 5 + random.nextInt(4);
		drivesFromWorkHour = 14 + random.nextInt(8);
		speed = 30 + random.nextInt(100);
		workDistance = random.nextInt(100);
		hoursToWork = workDistance / speed;
		driveToWorkPower = carModel.fullBattery * workDistance / carModel.range / (hoursToWork != 0 ? hoursToWork : 1);
	}
	public int charge(int maxPower) {
		int chargePower = Math.abs(maxPower) < carModel.chargeSpeed ? maxPower : carModel.chargeSpeed * Integer.signum(maxPower);
		if(battery + chargePower < chargeLock && chargePower < 0) {
			// do not charge if the car gets under the charge lock
			if(battery >= chargeLock) chargePower = -(battery - chargeLock);
			else chargePower = 0;
		}
		if(battery + chargePower > carModel.fullBattery) chargePower = carModel.fullBattery - battery; // prevent the battery from overcharging
		if(battery + chargePower < 0) chargePower = -battery; // prevent the battery from discharging under 0%
		if(powerGrid.logLevel >= 3 && chargePower != 0) System.out.println(carName + " is " + (chargePower < 0 ? "dis" : "") + "charging with " + (double)Math.abs(chargePower) / 1000 +  " kW. battery: " + (double)battery / 1000 + "/" + (double)carModel.fullBattery / 1000 + " kWh (" + Math.round(getBatteryRelativ() * 100) + "%)");
		battery += chargePower;
		return chargePower;
    	}
	public double getBatteryRelativ() {
		return (double)battery / carModel.fullBattery;
	}
	public void tickDrive(int hourOfDay) {
		if(stuck) return;
		int driveStartHour;
		carGrid carStartFrom;
		carGrid carDrivesTo;
		String destination;
		if(duringDrive(hourOfDay, drivesToWorkHour)) {
			driveStartHour = drivesToWorkHour;
			carStartFrom = homeCarGrid;
			carDrivesTo = workCarGrid;
			destination = "work";
		}
		else if(duringDrive(hourOfDay, drivesFromWorkHour)) {
			driveStartHour = drivesFromWorkHour;
			carStartFrom = workCarGrid;
			carDrivesTo = homeCarGrid;
			destination = "home";
		}
		else return;
		if(hourOfDay == driveStartHour) {
			carStartFrom.dockedCars.remove(this);
			if(hoursToWork != 0) return;
		}
		battery -= driveToWorkPower;
		if(battery < 0) {
			stuck = true;
			if(powerGrid.logLevel >= 1) System.out.println(carName + " is stuck because of an empty battery during drive to " + destination);
			return;
		}
		if(hourOfDay == driveStartHour + hoursToWork) {
			carDrivesTo.dockedCars.add(this);
			if(powerGrid.logLevel >= 3) System.out.println(carName + " drove " + workDistance + " km to " + destination + " with " + speed + " km/h");
		}
	}
	private boolean duringDrive(int hourOfDay, int startHour) {
		return hourOfDay >= startHour && hourOfDay <= startHour + hoursToWork;
	}
}
