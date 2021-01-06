package com.github.fabbaraujo.facadeexample.controller;

import com.github.fabbaraujo.facadeexample.model.Investor;
import com.github.fabbaraujo.facadeexample.model.Stock;
import com.github.fabbaraujo.facadeexample.model.exceptions.InvestorNotFoundException;
import com.github.fabbaraujo.facadeexample.service.InvestorServicesFetchOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ComponentScan(basePackageClasses = { InvestorServicesFetchOperations.class })
public class InvestorServiceFetchController {
    @Autowired
    private InvestorServicesFetchOperations investorServiceFetchOpertions;

    @GetMapping(value = "/welcome", produces = "text/plain;charset=UTF-8")
    public String welcomePageWhichProducesCharset() {
        return "Seja bem vindo Investidor! (\"Welcome Investor!\" in Portuguese)";
    }

    @GetMapping("/investors")
    public String welcomeUsers() {
        return "You are in right API, However please refer the API docs for specific URLs that can be accessed with your Roles";
    }

    @GetMapping("/investors/admin")
    public List<Investor> fetchAllInvestors() {
        return investorServiceFetchOpertions.fetchAllInvestors();
    }

    @GetMapping("/investors/{investorId}")
    public Investor fetchInvestorById(@PathVariable String investorId) {
        Investor resultantInvestor = investorServiceFetchOpertions.fetchInvestorById(investorId);
        if (resultantInvestor == null) {
            throw new InvestorNotFoundException("Investor Id-" + investorId);
        }
        return resultantInvestor;
    }

    @GetMapping(path = "/investors/{investorId}/stocks")
    public List<Stock> fetchStocksByInvestorId(@PathVariable String investorId,
                                               @RequestParam(value = "offset", defaultValue = "0") int offset,
                                               @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return investorServiceFetchOpertions.fetchStocksByInvestorId(investorId, offset, limit);
    }

    @GetMapping(path = "/investors/{investorId}/stocks/{symbol}", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public Stock fetchAStockByInvestorIdAndStockId(@PathVariable String investorId, @PathVariable String symbol) {
        return investorServiceFetchOpertions.fetchSingleStockByInvestorIdAndStockSymbol(investorId, symbol);
    }
}
