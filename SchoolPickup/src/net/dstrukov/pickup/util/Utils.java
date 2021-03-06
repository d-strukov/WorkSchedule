package net.dstrukov.pickup.util;

public class Utils {
	public static int toMinutes(int hours, int minutes) {
		return hours * 60 + minutes;
	}
	
	public static int penaltyLinear(int count, int multiplier){
		count = count >0? count : 0;
		return count * multiplier;
	}
	public static int penaltyXn(int n, int x){
		return (int) Math.pow(x, n);
	}
}
