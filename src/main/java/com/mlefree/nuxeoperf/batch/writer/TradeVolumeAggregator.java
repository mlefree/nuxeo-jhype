package com.mlefree.nuxeoperf.batch.writer;


import com.mlefree.nuxeoperf.batch.model.TradeVolumeStore;
import com.mlefree.nuxeoperf.batch.model.TradeVolume;
import com.mlefree.nuxeoperf.batch.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TradeVolumeAggregator implements ItemWriter<Trade> {

	@Autowired
	private TradeVolumeStore tradeVolumeStore;

	private static final Logger log = LoggerFactory.getLogger(TradeVolumeAggregator.class);

	@Override
	public void write(List<? extends Trade> trades) throws Exception {
		trades.forEach(t -> {
			if (tradeVolumeStore.containsKey(t.getAvs())) {
				TradeVolume tradeVolume = tradeVolumeStore.get(t.getAvs());
				//long newVolume = tradeVolume.getVolume() + t.getShares();
                long newVolume = tradeVolume.getVolume() + 1;
                    // Increment stock volume
				tradeVolume.setVolume(newVolume);
			}
			else {
				log.trace("Adding new trade {}", t.getAvs());
				tradeVolumeStore.put(t.getAvs(),
						new TradeVolume(t.getAvs(), 1));
			}
		});
	}

}
