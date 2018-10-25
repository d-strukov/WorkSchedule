package net.dstrukov.pickup.model;

public class WeekTime {
	private String description=""; 
	private int[] values;

	
	public WeekTime(int size) {
		values = new int[size];
	}

	public WeekTime(int[] values) {
		super();
		this.values = values;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

}
