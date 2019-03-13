package com.mlefree.nuxeoperf.batch.model;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TradeVolumeStore {

	private ConcurrentMap<String, TradeVolume> stock = new ConcurrentHashMap<String, TradeVolume>();

	public boolean containsKey(Object key) {
		return stock.containsKey(key);
	}

	public TradeVolume put(String key, TradeVolume value) {
		return stock.put(key, value);
	}

	public Collection<TradeVolume> values() {
		return stock.values();
	}

	public TradeVolume get(Object key) {
		return stock.get(key);
	}

}
