package com.github.fabbaraujo.facadeexample.model;

public class IndividualInvestorPortfolio {
    private String investorId;
    private int stocksHoldCount;

    public IndividualInvestorPortfolio(String investorId, int stocksHoldCount){
        this.investorId = investorId;
        this.stocksHoldCount = stocksHoldCount;
    }

    public String getInvestorId() {
        return investorId;
    }
    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }
    public int getStocksHoldCount() {
        return stocksHoldCount;
    }
    public void setStocksHoldCount(int stocksCount) {
        stocksHoldCount = stocksCount;
    }

}
