package com.github.fabbaraujo.facadeexample.service;

import com.github.fabbaraujo.facadeexample.model.IndividualInvestorPortfolio;
import com.github.fabbaraujo.facadeexample.model.Investor;
import com.github.fabbaraujo.facadeexample.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteServiceFacadeImpl implements DeleteServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(InvestorService.class);
    private InvestorServicesFetchOperations investorServicesFetchOperations = new InvestorServicesFetchOperations();

    @Override
    public boolean deleteAStock(String investorId, String stockTobeDeletedSymbol) {
        boolean deletedStatus = false;

        Stock stockTobeDeleted = investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
                stockTobeDeletedSymbol);
        if (stockTobeDeleted != null) {
            Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
            deletedStatus = investor.getStocks().remove(stockTobeDeleted);
        }
        designForIntentCascadePortfolioDelete(investorId, deletedStatus);
        return deletedStatus;
    }

    @Override
    public boolean deleteStocksInBulk(String investorId, List<String> stocksSymbolsList) {
        return false;
    }

    private void designForIntentCascadePortfolioDelete(String investorId, boolean deletedStatus) {
        IndividualInvestorPortfolio individualInvestorPortfolio = InvestorService.portfoliosMap
                .get(investorServicesFetchOperations.fetchInvestorById(investorId).getId());
        if (deletedStatus) {
            individualInvestorPortfolio.setStocksHoldCount(individualInvestorPortfolio.getStocksHoldCount() - 1);
            logger.info("updated the portfolio for Delete stocks operation");
        } else {
            logger.warn("Update to the individual portofolio not pursued as deleted status of DEL operation returned ");
        }
    }

}
