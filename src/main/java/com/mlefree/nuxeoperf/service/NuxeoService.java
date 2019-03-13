package com.mlefree.nuxeoperf.service;

import com.mlefree.nuxeoperf.batch.model.Trade;
import com.mlefree.nuxeoperf.domain.Scenario;

import java.util.List;
import java.util.Optional;

public interface NuxeoService {


    void searchSmall();
    void importSmall();
    void importSmall(Trade trade, Boolean async);
    void importBulkSmall();
}
