package net.dstrukov.pickup;

import java.util.Arrays;

import net.dstrukov.pickup.util.Utils;

public class Schedule {
	static ScheduleInputData data = new ScheduleInputData(Utils.toMinutes(38, 00), Utils.toMinutes(39, 00),
			Utils.toMinutes(5, 00), (int) Utils.toMinutes(7, 45), (int) Utils.toMinutes(7, 30), 17 * 60+15,
			 18 * 60,
			new int[] { Utils.toMinutes(8, 25), Utils.toMinutes(8, 25), Utils.toMinutes(8, 25), Utils.toMinutes(8, 25),
					Utils.toMinutes(8, 25) },
			new int[] { Utils.toMinutes(15, 30), Utils.toMinutes(15, 30), Utils.toMinutes(12, 00),
					Utils.toMinutes(15, 30), Utils.toMinutes(15, 30) },
			Utils.toMinutes(0, 35), Utils.toMinutes(0, 20));

	byte[] pickupFlag = new byte[] { 1, 1, 2, 1, 1 };
	byte[] dropOffFlag = new byte[] { 1, 1, 1, 1, 1 };

	int[] workStart1 = new int[5];
	int[] workHours1 = new int[5];

	int[] workStart2 = new int[5];
	int[] workHours2 = new int[5];

	static int penGrandma = 5000;

	static int penPickUpToEarly = 10;
	static int penPickUpToLate = 300;

	static int penDropOffToEarly = 300;
	static int penDropOffToLate = 10;

	static int penUndertime = 300;
	static int penOvertime = 200;

	static int penOverhead = 10;

	public Schedule(Schedule schedule) {
		this.pickupFlag = Arrays.copyOf(schedule.pickupFlag, schedule.pickupFlag.length);
		this.dropOffFlag = Arrays.copyOf(schedule.dropOffFlag, schedule.dropOffFlag.length);
		this.workHours1 = Arrays.copyOf(schedule.workHours1, schedule.workHours1.length);
		this.workHours2 = Arrays.copyOf(schedule.workHours2, schedule.workHours2.length);
		this.workStart1 = Arrays.copyOf(schedule.workStart1, schedule.workStart1.length);
		this.workStart2 = Arrays.copyOf(schedule.workStart2, schedule.workStart2.length);
	}

	public Schedule() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("Drop offs: [ %1$d,%2$d,%3$d,%4$d,%5$d ] \n", dropOffFlag[0], dropOffFlag[1],
//				dropOffFlag[2], dropOffFlag[3], dropOffFlag[4]));
//		sb.append(String.format("Pick Ups: [ %1$d,%2$d,%3$d,%4$d,%5$d ] \n", pickupFlag[0], pickupFlag[1],
//				pickupFlag[2], pickupFlag[3], pickupFlag[4]));
//		sb.append(String.format("Work Start 1: [ %d, %d, %d, %d, %d ] \n", workStart1[0], workStart1[1], workStart1[2],
//				workStart1[3], workStart1[4]));
//		sb.append(String.format("Work Hours 1: [ %d, %d, %d, %d, %d ] \n", workHours1[0], workHours1[1], workHours1[2],
//				workHours1[3], workHours1[4]));
//		sb.append(String.format("Work Start 2: [ %d, %d, %d, %d, %d ] \n", workStart2[0], workStart2[1], workStart2[2],
//				workStart2[3], workStart2[4]));
//		sb.append(String.format("Work Hours 2: [ %d, %d, %d, %d, %d ] \n", workHours2[0], workHours2[1], workHours2[2],
//				workHours2[3], workHours2[4]));

		String dropoff = "";
		String start = "";
		String end = "";
		String pickup = "";

		int totalHours = 0;
		for (int i = 0; i < 5; i++) {
			dropoff += String.format("%s\t", dropOffFlag[i] == 1 ? "X" : dropOffFlag[i] == 0 ? "G" : "");
			start += String.format("%s\t", toTimeString(workStart1[i]));
			end += String.format("%s\t", toTimeString(getParentWorkEnd(true, i)));
			pickup += String.format("%s\t", pickupFlag[i] == 1 ? "X" : pickupFlag[i] == 0 ? "G" : "");

			totalHours += workHours1[i];
		}

		sb.append(dropoff + "\n");
		sb.append(start + "\n");
		sb.append(end + "\n");
		sb.append(pickup + "\n" + totalHours/60 + "\n");

		dropoff = "";
		start = "";
		end = "";
		pickup = "";
		totalHours = 0;
		for (int i = 0; i < 5; i++) {
			dropoff += String.format("%s\t", dropOffFlag[i] == 2 ? "X" : dropOffFlag[i] == 0 ? "G" : "");
			start += String.format("%s\t", toTimeString(workStart2[i]));
			end += String.format("%s\t", toTimeString(getParentWorkEnd(false, i)));
			pickup += String.format("%s\t", pickupFlag[i] == 2 ? "X" : pickupFlag[i] == 0 ? "G" : "");
			totalHours += workHours2[i];
		}

		sb.append(dropoff + "\n");
		sb.append(start + "\n");
		sb.append(end + "\n");
		sb.append(pickup + "\n"+ totalHours/60 + "\n");

		return sb.toString();
	}

	private Object toTimeString(int i) {

		return String.format("%02d:%02d", i / 60, i % 60);
	}

	public int getCalculatedWorth() {
		int total = 0;

		int grandmaUseCount = 0;
		int dropOffTooEarly = 0, dropOffTooLate = 0;
		int pickUpTooEarly = 0, pickUpTooLate = 0;
		int workOverhead = 0;
		for (int i = 0; i < pickupFlag.length; i++) {
			int dropOffTime = data.getDropoffTimes()[i];
			int pickupTime = data.getPickupTimes()[i];

			if (dropOffFlag[i] == 0) {
				grandmaUseCount++;
			} else if (dropOffFlag[i] == 1) {

				int dropOffDeviation = dropOffTime - workStart1[i] + data.getParent1Travel();
				if (dropOffDeviation > 0)
					dropOffTooEarly += dropOffDeviation;
				else
					dropOffTooLate -= dropOffDeviation;
			} else if (dropOffFlag[i] == 2) {
				int dropOffDeviation = dropOffTime - workStart2[i] + data.getParent2Travel();
				if (dropOffDeviation > 0)
					dropOffTooEarly += dropOffDeviation;
				else
					dropOffTooLate -= dropOffDeviation;

			}

			if (pickupFlag[i] == 0) {
				grandmaUseCount++;
			} else if (pickupFlag[i] == 1) {
				int pickUpDeviation = pickupTime - getParentWorkEnd(true, i) - data.getParent1Travel();
				if (pickUpDeviation > 0)
					pickUpTooEarly += pickUpDeviation;
				else
					pickUpTooLate -= pickUpDeviation;
			} else if (pickupFlag[i] == 2) {
				int pickUpDeviation = pickupTime - getParentWorkEnd(false, i) - data.getParent2Travel();
				if (pickUpDeviation > 0)
					pickUpTooEarly += pickUpDeviation;
				else
					pickUpTooLate -= pickUpDeviation;
			}

		}

		int parent1Hours = 0, parent2Hours = 0;
		for (int i = 0; i < workHours1.length; i++) {
			parent1Hours += workHours1[i];
			parent2Hours += workHours2[i];
		}

		int undertime = 0, overtime = 0;
		int hoursDeviation1 = data.getParent1() - parent1Hours;
		int hoursDeviation2 = data.getParent2() - parent2Hours;

		if (hoursDeviation1 > 0)
			undertime += hoursDeviation1;
		else
			overtime -= hoursDeviation1;

		if (hoursDeviation2 > 0)
			undertime += hoursDeviation2;
		else
			overtime -= hoursDeviation2;

		int beforeHardStart = 0, afterHardStop = 0;
		int beforePreferedStart = 0, afterPreferedStop = 0;

		for (int i = 0; i < workStart1.length; i++) {
			if (workStart1[i] < data.getParent1EarliesStart())
				beforeHardStart += data.getParent1EarliesStart() - workStart1[i];
			if (getParentWorkEnd(true, i) > data.getParent1Latest())
				afterHardStop += getParentWorkEnd(true, i) - data.getParent1Latest();

			if (workStart2[i] < data.getParent2EarliesStart())
				beforeHardStart += data.getParent2EarliesStart() - workStart2[i];
			if (getParentWorkEnd(false, i) > data.getParent2Latest())
				afterHardStop += getParentWorkEnd(false, i) - data.getParent2Latest();
		}

		total += Utils.penaltyLinear(beforeHardStart, 100);
		total += Utils.penaltyLinear(afterHardStop, 100);
		total += Utils.penaltyLinear(beforePreferedStart, 20);
		total += Utils.penaltyLinear(afterPreferedStop, 20);

		total += Utils.penaltyXn(grandmaUseCount, penGrandma);
		total += Utils.penaltyLinear(dropOffTooEarly, penDropOffToEarly);
		total += Utils.penaltyLinear(dropOffTooLate, penDropOffToLate);
		total += Utils.penaltyLinear(pickUpTooEarly, penPickUpToEarly);
		total += Utils.penaltyLinear(pickUpTooLate, penPickUpToLate);
		total += Utils.penaltyLinear(undertime, penUndertime);
		total += Utils.penaltyLinear(overtime, penOvertime);

		return total;
	}

	private int getParentWorkEnd(boolean parentOneFlag, int i) {
		return (parentOneFlag ? workStart1[i] : workStart2[i]) + (parentOneFlag ? workHours1[i] : workHours2[i])
				+ ((parentOneFlag ? workHours1[i] : workHours2[i]) > data.getLunchLimit() ? 30 : 0);
	}

	public Schedule getNeighbour(int dimension, int step) {
		Schedule s = new Schedule(this);
		int position = 0;
		if (dimension < (position += s.pickupFlag.length)) {
			s.pickupFlag[position - dimension - 1] += step/15;
			if (s.pickupFlag[position - dimension - 1] > 2)
				s.pickupFlag[position - dimension - 1] = 2;
			else if (s.pickupFlag[position - dimension - 1] < 0)
				s.pickupFlag[position - dimension - 1] = 0;
		} else if (dimension < (position += s.dropOffFlag.length)) {
			s.dropOffFlag[position - dimension - 1] += step/15;
			if (s.dropOffFlag[position - dimension - 1] > 2)
				s.dropOffFlag[position - dimension - 1] = 2;
			else if (s.dropOffFlag[position - dimension - 1] < 0)
				s.dropOffFlag[position - dimension - 1] = 0;
		} else if (dimension < (position += s.workHours1.length)) {
			s.workHours1[position - dimension - 1] += step;
			if (s.workHours1[position - dimension - 1] < 0)
				s.workHours1[position - dimension - 1] = 0;
		} else if (dimension < (position += s.workHours2.length)) {
			s.workHours2[position - dimension - 1] += step;
			if (s.workHours2[position - dimension - 1] < 0)
				s.workHours2[position - dimension - 1] = 0;
		} else if (dimension < (position += s.workStart1.length)) {
			s.workStart1[position - dimension - 1] += step;
			if (s.workStart1[position - dimension - 1] < 0)
				s.workStart1[position - dimension - 1] = 0;
		} else if (dimension < (position += s.workStart2.length)) {
			s.workStart2[position - dimension - 1] += step;
			if (s.workStart2[position - dimension - 1] < 0)
				s.workStart2[position - dimension - 1] = 0;
		}

		// System.out.println(position);

		return s;

	}

}
