package com.mlefree.nuxeojhype.batch.model;

public class TradeVolume {

	private String avs;
	private long volume;

	public TradeVolume(String avs, long volume) {
		super();
		this.avs = avs;
		this.volume = volume;
	}

	public String getAvs() {
		return avs;
	}

	public void setAvs(String avs) {
		this.avs = avs;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "TradeVolume [avs=" + avs + ", volume=" + volume + "]";
	}

}
