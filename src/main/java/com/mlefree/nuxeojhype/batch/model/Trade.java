package com.mlefree.nuxeojhype.batch.model;

public class Trade {

	private String avs;

	public Trade(String avs) {
		super();
		this.avs = avs;
	}

	public String getAvs() {
		return avs;
	}

	public void setAvs(String stock) {
		this.avs = avs;
	}

	@Override
	public String toString() {
		return "Trade [avs=" + avs + "]";
	}
}
