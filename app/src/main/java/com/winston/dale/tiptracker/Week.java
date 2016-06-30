package com.winston.dale.tiptracker;

/**
 * Created by dalewinston on 1/19/16.
 */
public class Week {
    private String date;
    private String amount;

    public Week(String date, String amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
