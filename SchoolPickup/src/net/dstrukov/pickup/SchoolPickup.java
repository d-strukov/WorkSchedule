package net.dstrukov.pickup;

import java.util.Date;
import java.util.Random;

import net.dstrukov.pickup.util.Utils;

public class SchoolPickup {

	public static void main(String[] args) {
		// Set initial temp
		double temp = 1000;

		// Cooling rate
		double coolingRate = 0.000002;

		// create random intial solution
		Schedule currentSolution = new Schedule();

		System.out.println("Initial solution value: " + currentSolution.getCalculatedWorth());
		// System.out.println("Tour: \n" + currentSolution);

		// We would like to keep track if the best solution
		// Assume best solution is the current solution
		Schedule best = currentSolution;
		long seed =  (new Date()).getTime();
		System.out.println("Seed: " + seed);

		Random rand = new Random(seed);

		// Loop until system has cooled
		while (temp > 1) {

			for (int dimension = 0; dimension <= 30; dimension++) {

				int improvement = Integer.MAX_VALUE;
				int previousImprovement = -1;
				int step = rand.nextBoolean() ? -1 : 1;
				int stepIncreaseFactor = 1;
				while (improvement > 0) {
					// Create new neighbour tour
					Schedule s = currentSolution.getNeighbour(dimension, step);

					// Get energy of solutions
					int currentDistance = currentSolution.getCalculatedWorth();
					int neighbourDistance = s.getCalculatedWorth();

					// Decide if we should accept the neighbour
					double r = rand.nextDouble();
					if (acceptanceProbability(currentDistance, neighbourDistance, temp) > r) {
						currentSolution = s;

					}
					improvement =  currentDistance - neighbourDistance;
					// Keep track of the best solution found
					if (currentDistance > neighbourDistance) {
						
						best = currentSolution;
					}
					
					
					if (improvement > 0) {
						step *= stepIncreaseFactor;
						step =Math.min(step, 60);
						stepIncreaseFactor++;
						previousImprovement = improvement;
					}
				}

			}

			// Cool system
			temp *= 1 - coolingRate;
		}

		System.out.println("Final solution distance: " + best.getCalculatedWorth());
		System.out.println("" + best);

	}

	public static double acceptanceProbability(int currentDistance, int newDistance, double temperature) {
		// If the new solution is better, accept it
		if (newDistance < currentDistance) {
			return 1.0;
		}
		// If the new solution is worse, calculate an acceptance probability
		return Math.exp((currentDistance - newDistance) / temperature);
	}

}
