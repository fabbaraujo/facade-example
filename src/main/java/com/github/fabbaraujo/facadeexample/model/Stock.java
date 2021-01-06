package com.github.fabbaraujo.facadeexample.model;

public class Stock {
    private String symbol;
    private int numberOfSharesHeld;
    private double tickerPrice = 0.0;

    Stock() { }

    public Stock(String symbol, int numberOfSharesHeld, double tickerPrice) {

        this.symbol = symbol;
        this.numberOfSharesHeld = numberOfSharesHeld;
        this.tickerPrice = tickerPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getNumberOfSharesHeld() {
        return numberOfSharesHeld;
    }

    public void setNumberOfSharesHeld(int numberOfShares) {
        this.numberOfSharesHeld = numberOfShares;
    }

    public double getTickerPrice() {
        return tickerPrice;
    }

    public void setTickerPrice(double tickerPrice) {
        this.tickerPrice = tickerPrice;
    }

    @Override
    public String toString() {
        String pattern = "Stock [symbol: %s, numberOfSharesHeld: %d, tickerPrice: %6.2f]";
        return String.format(pattern, symbol, numberOfSharesHeld, tickerPrice);
    }
}
