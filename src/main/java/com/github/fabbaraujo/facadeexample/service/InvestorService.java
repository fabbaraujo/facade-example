package com.github.fabbaraujo.facadeexample.service;

import com.github.fabbaraujo.facadeexample.model.IndividualInvestorPortfolio;
import com.github.fabbaraujo.facadeexample.model.Investor;
import com.github.fabbaraujo.facadeexample.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvestorService {

    static List<Investor> investorsList = new ArrayList<>();
    static Map<String, IndividualInvestorPortfolio> portfoliosMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(InvestorService.class);

    private InvestorServicesFetchOperations investorServicesFetchOperations = new InvestorServicesFetchOperations();
    private DeleteServiceFacade deleteServiceFacade = new DeleteServiceFacadeImpl();

    public Stock addNewStockToTheInvestorPortfolio(String investorId, Stock newStock) {
        if (isPreConditionSuccess(investorId, newStock) && isNewStockInsertSucess(investorId, newStock)) {
            designForIntentCascadePortfolioAdd(investorId);
            return investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
                    newStock.getSymbol());
        }
        return null;
    }

    public boolean deleteAStockFromTheInvestorPortfolio(String investorId, String stockTobeDeletedSymbol) {
        return deleteServiceFacade.deleteAStock(investorId, stockTobeDeletedSymbol);
    }

    public Stock updateAStockByInvestorIdAndStock(String investorId, Stock stockTobeUpdated) {
        Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
        if (investor == null) {
            return null;
        }
        Stock currentStock = investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
                stockTobeUpdated.getSymbol());
        if (currentStock == null) {
            return null;
        }
        currentStock.setNumberOfSharesHeld(stockTobeUpdated.getNumberOfSharesHeld());
        currentStock.setTickerPrice(stockTobeUpdated.getTickerPrice());
        return currentStock;
    }

    public Stock updateAStockByInvestorIdAndStock(String investorId, String symbol, Stock stockTobeUpdated) {
        Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
        if (investor == null) {
            return null;
        }
        Stock currentStock = investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
                symbol);
        if (currentStock == null) {
            return null;
        }
        if (stockTobeUpdated.getNumberOfSharesHeld() > 0) {
            currentStock.setNumberOfSharesHeld(stockTobeUpdated.getNumberOfSharesHeld());
        }
        if (stockTobeUpdated.getTickerPrice() > 0) {
            currentStock.setTickerPrice(stockTobeUpdated.getTickerPrice());
        }
        return currentStock;
    }

    public List<Stock> bulkUpdateOfStocksByInvestorId(String investorId, List<Stock> stocksTobeUpdated) {
        Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
        List<Stock> updatedStock = new ArrayList<>();
        if (investor != null) {
            stocksTobeUpdated.forEach(
                    stock -> updatedStock.add(updateAStockByInvestorIdAndStock(investorId, stock.getSymbol(), stock)));
        }

        return updatedStock;
    }

    private static IndividualInvestorPortfolio updateInvestorPortfolioByInvestorId(Investor investor) {
        return new IndividualInvestorPortfolio(investor.getId(), investor.getStocks().size());

    }

    private boolean isPreConditionSuccess(String investorId, Stock newStock) {
        return investorServicesFetchOperations.fetchInvestorById(investorId) != null && isUnique(investorId, newStock);
    }

    private boolean isNewStockInsertSucess(String investorId, Stock newStock) {
        return investorServicesFetchOperations.fetchInvestorById(investorId).getStocks().add(newStock);
    }

    private boolean isUnique(String investorId, Stock newStock) {
        return investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
                newStock.getSymbol()) == null;
    }

    private void designForIntentCascadePortfolioAdd(String investorId) {
        IndividualInvestorPortfolio individualInvestorPortfolio = portfoliosMap
                .get(investorServicesFetchOperations.fetchInvestorById(investorId).getId());
        individualInvestorPortfolio.setStocksHoldCount(individualInvestorPortfolio.getStocksHoldCount() + 1);
        logger.info("updated the portfolio for ADD stocks operation");
    }

    static {
        Stock stocksSampleOne = new Stock("EXA", 200, 20);
        Stock stocksSampleTwo = new Stock("EXB", 100, 60);

        Stock stocksSampleThree = new Stock("EXC", 300, 200);
        Stock stocksSampleFour = new Stock("EXD", 150, 40);

        Stock stockSampleFive = new Stock("EX5", 200, 20);
        Stock stockSampleSix = new Stock("EX6", 200, 20);

        ArrayList<Stock> stocksLotOne = new ArrayList<>();
        stocksLotOne.add(stocksSampleOne);
        stocksLotOne.add(stocksSampleTwo);
        stocksLotOne.add(stockSampleFive);
        stocksLotOne.add(stockSampleSix);
        ArrayList<Stock> stocksLotTwo = new ArrayList<>();
        stocksLotTwo.add(stocksSampleThree);
        stocksLotTwo.add(stocksSampleFour);
        stocksLotTwo.add(stockSampleFive);
        stocksLotTwo.add(stockSampleSix);

        Investor investorOne = new Investor("INVR_1", "Investor ONE", "conservative investor", stocksLotOne);
        Investor investorTwo = new Investor("INVR_2", "Investor TWO", "Moderate Risk investor", stocksLotTwo);

        investorsList.add(investorOne);
        investorsList.add(investorTwo);

        IndividualInvestorPortfolio portfolioOfInvestorOne = updateInvestorPortfolioByInvestorId(investorOne);
        IndividualInvestorPortfolio portfolioOfInvestorTwo = updateInvestorPortfolioByInvestorId(investorTwo);
        portfoliosMap.put(investorOne.getId(), portfolioOfInvestorOne);
        portfoliosMap.put(investorTwo.getId(), portfolioOfInvestorTwo);

    }
}
