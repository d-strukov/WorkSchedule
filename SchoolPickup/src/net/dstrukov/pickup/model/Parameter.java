package net.dstrukov.pickup.model;

public class Parameter {
	
	private String shortDescription ="", longDescription="";
	private int value = 0;
	
	public Parameter(String shortDescription, String longDescription, int value) {
		super();
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.value = value;
	}

	public Parameter() {
	
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	

}
