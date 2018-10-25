package net.dstrukov.pickup;

public class ScheduleInputData {
	private int parent1;
	private int parent2;
	private int lunchLimit;
	private int parent1EarliesStart;
	private int parent2EarliesStart;
	private int parent1Latest;
	private int parent2Latest;
	private int[] dropoffTimes;
	private int[] pickupTimes;
	private int parent1Travel;
	private int parent2Travel;

	public int getParent1Travel() {
		return parent1Travel;
	}

	public int getParent2Travel() {
		return parent2Travel;
	}

	public ScheduleInputData(int parent1HoursPerWeek, int parent2HoursPerWeek, int lunchLimit, int parent1EarliesStart, int parent2EarliesStart,
			int parent1Latest, int parent2Latest, int[] dropoffTimes, int[] pickupTimes, int travel1, int travel2) {
		this.parent1 = parent1HoursPerWeek;
		this.parent2 = parent2HoursPerWeek;
		this.lunchLimit = lunchLimit;
		this.parent1EarliesStart = parent1EarliesStart;
		this.parent2EarliesStart = parent2EarliesStart;
		this.parent1Latest = parent1Latest;
		this.parent2Latest = parent2Latest;
		this.dropoffTimes = dropoffTimes;
		this.pickupTimes = pickupTimes;
		this.parent1Travel = travel1;
		this.parent2Travel = travel2;
	}

	public int getParent1() {
		return parent1;
	}

	public void setParent1(int parent1) {
		this.parent1 = parent1;
	}

	public int getParent2() {
		return parent2;
	}

	public void setParent2(int parent2) {
		this.parent2 = parent2;
	}

	public int getLunchLimit() {
		return lunchLimit;
	}

	public void setLunchLimit(int lunchLimit) {
		this.lunchLimit = lunchLimit;
	}

	public int getParent1EarliesStart() {
		return parent1EarliesStart;
	}

	public void setParent1EarliesStart(int parent1EarliesStart) {
		this.parent1EarliesStart = parent1EarliesStart;
	}

	public int getParent2EarliesStart() {
		return parent2EarliesStart;
	}

	public void setParent2EarliesStart(int parent2EarliesStart) {
		this.parent2EarliesStart = parent2EarliesStart;
	}

	public int getParent1Latest() {
		return parent1Latest;
	}

	public void setParent1Latest(int parent1Latest) {
		this.parent1Latest = parent1Latest;
	}

	public int getParent2Latest() {
		return parent2Latest;
	}

	public void setParent2Latest(int parent2Latest) {
		this.parent2Latest = parent2Latest;
	}

	public int[] getDropoffTimes() {
		return dropoffTimes;
	}

	public void setDropoffTimes(int[] dropoffTimes) {
		this.dropoffTimes = dropoffTimes;
	}

	public int[] getPickupTimes() {
		return pickupTimes;
	}

	public void setPickupTimes(int[] pickupTimes) {
		this.pickupTimes = pickupTimes;
	}
}