package net.dstrukov.pickup;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import net.dstrukov.pickup.gui.OutputPanel;
import net.dstrukov.pickup.util.Utils;

public class SchoolPickup {

	public static void main(String[] args) {
		// Set initial temp

		// Cooling rate
		double coolingRate = 0.00002;

		// create random intial solution
		Schedule currentSolution = new Schedule();

		System.out.println("Initial solution value: " + currentSolution.getCalculatedWorth());
		// System.out.println("Tour: \n" + currentSolution);

		// We would like to keep track if the best solution
		// Assume best solution is the current solution
		Schedule best = currentSolution;
		int bestSolution = best.getCalculatedWorth();
		long seed = (new Date()).getTime();
		System.out.println("Seed: " + seed);

		Random rand = new Random(seed);
		OutputPanel op = new OutputPanel();
		op.setSchedule(best);
		JFrame f = new JFrame();
		f.setSize(600, 800);
		f.setTitle("OutputPanel Test");
		f.getContentPane().add(op, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		Date lastRefresh = new Date();
		Integer[] values = new Integer[512];
		for (int k = 0; k < 512; k++) {
			values[k] = k;
		}
		List<Integer> l = Arrays.asList(values);
		Collections.shuffle(l);
		
		Integer[] v = new Integer[21];
		for (int dimension = 10; dimension <= 30; dimension++) {
			v[dimension-10] = dimension;
		}
		
		List<Integer> ll = Arrays.asList(v);
		for (int z = 0; z < 512; z++) {
			int k = l.get(z);
			currentSolution = new Schedule();
			currentSolution.dropOffFlag = new byte[] { (k & 1) > 0 ? (byte) 1 : 2, (k & 2) > 0 ? (byte) 1 : 2,
					(k & 4) > 0 ? (byte) 1 : 2, (k & 8) > 0 ? (byte) 1 : 2, (k & 16) > 0 ? (byte) 1 : 2 };
			currentSolution.pickupFlag = new byte[] { (k & 32) > 0 ? (byte) 1 : 2, (k & 64) > 0 ? (byte) 1 : 2, 0,
					(k & 128) > 0 ? (byte) 1 : 2, (k & 256) > 0 ? (byte) 1 : 2 };
			double temp = 10000;
			// Loop until system has cooled
			while (temp > 1) {
				
				Collections.shuffle(ll);
				for (int a = 0; a < ll.size(); a++) {

					int dimension = ll.get(a);
					int improvement = Integer.MAX_VALUE;
					int previousImprovement = -1;
					int step = rand.nextBoolean() ? -15 : 15;
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
						improvement = currentDistance - neighbourDistance;
						// Keep track of the best solution found
						if (bestSolution > neighbourDistance) {

							best = currentSolution;
							bestSolution = neighbourDistance;
							if (new Date().getTime() - lastRefresh.getTime() > 200) {
								op.setSchedule(best);
								lastRefresh = new Date();
							}
							System.out.println("iteration: " + z + " solution:" + bestSolution);
							System.out.println("" + best);
						}

						if (improvement > 0) {
							// step *= stepIncreaseFactor;
							// step =Math.min(step, 60);
							stepIncreaseFactor++;
							previousImprovement = improvement;
						}
					}

				}

				// Cool system
				temp *= 1 - coolingRate;
			}
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
