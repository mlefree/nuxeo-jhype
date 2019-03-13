package com.mlefree.nuxeoperf.batch.model;

public class TradeEvent {

    private String avs;

    public TradeEvent() {
    }

    public TradeEvent(String avs) {
        super();
        this.avs = avs;
    }

    public String getAvs() {
        return avs;
    }

    public void setAvs(String avs) {
        this.avs = avs;
    }

    @Override
    public String toString() {
        return "TradeEvent [avs=" + avs + "]";
    }
}
