package com.mlefree.nuxeojhype.service;

import com.mlefree.nuxeojhype.batch.model.Trade;

public interface NuxeoService {

    void searchSmall();
    void importSmall();
    void importSmall(Trade trade, Boolean async, Boolean image);
    void importBulkSmall();
}
