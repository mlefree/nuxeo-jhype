package com.mlefree.nuxeojhype.batch;

import com.mlefree.nuxeojhype.batch.model.Trade;
import com.mlefree.nuxeojhype.batch.model.TradeEvent;
import com.mlefree.nuxeojhype.service.NuxeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class TradeEventProcessor implements ItemProcessor<TradeEvent, Trade> {

    private static final Logger log = LoggerFactory.getLogger(TradeEventProcessor.class);

    @Autowired
    NuxeoService nuxeoService;

    @Override
    public Trade process(final TradeEvent tradeEvent) throws Exception {

        final String avs = tradeEvent.getAvs();
        final Trade trade = new Trade(avs);

        System.out.println("Importing (" + tradeEvent + ")");
        nuxeoService.importSmall(trade, true, false);
        System.out.println("Import done (" + tradeEvent + ")");
        // log.trace("Converting (" + tradeEvent + ") into (" + trade + ")");

        return trade;
    }

}
