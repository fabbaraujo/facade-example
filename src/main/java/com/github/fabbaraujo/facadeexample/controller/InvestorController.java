package com.github.fabbaraujo.facadeexample.controller;

import com.github.fabbaraujo.facadeexample.model.Stock;
import com.github.fabbaraujo.facadeexample.model.exceptions.CustomHeaderNotFoundException;
import com.github.fabbaraujo.facadeexample.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@ComponentScan(basePackageClasses = { InvestorService.class })
public class InvestorController {

    private static final String ID = "/{id}";
    @Autowired
    private InvestorService investorService;

    @PostMapping("/investors/{investorId}/stocks")
    public ResponseEntity<Void> addNewStockToTheInvestorPortfolio(@PathVariable String investorId,
                                                                  @RequestBody Stock newStock) {
        Stock insertedStock = investorService.addNewStockToTheInvestorPortfolio(investorId, newStock);
        if (insertedStock == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
                .buildAndExpand(insertedStock.getSymbol()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/investors/{investorId}/stocks")
    public ResponseEntity<Void> updateAStockOfTheInvestorPortfolio(@PathVariable String investorId,
                                                                   @RequestBody Stock stockTobeUpdated) {
        Stock updatedStock = investorService.updateAStockByInvestorIdAndStock(investorId, stockTobeUpdated);
        if (updatedStock == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
                .buildAndExpand(updatedStock.getSymbol()).toUri();
        return ResponseEntity.created(location).build();
    }

    // bulk (patch) operation example with custom request header

    @PatchMapping("/investors/{investorId}/stocks")
    public ResponseEntity<Void> updateStockOfTheInvestorPortfolio(@PathVariable String investorId,
                                                                  @RequestHeader(value = "x-bulk-patch") Optional<Boolean> isBulkPatch,
                                                                  @RequestBody List<Stock> stocksTobeUpdated) throws CustomHeaderNotFoundException {
        // without custom header we are not going to process this bulk operation
        if (!isBulkPatch.isPresent()) {
            throw new CustomHeaderNotFoundException("x-bulk-patch not found in your headers");
        }
        investorService.bulkUpdateOfStocksByInvestorId(investorId, stocksTobeUpdated);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/investors/{investorId}/stocks/{symbol}")
    public ResponseEntity<Void> updateAStockOfTheInvestorPortfolio(@PathVariable String investorId,
                                                                   @PathVariable String symbol, @RequestBody Stock stockTobeUpdated) {
        Stock updatedStock = investorService.updateAStockByInvestorIdAndStock(investorId, symbol, stockTobeUpdated);
        if (updatedStock == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
                .buildAndExpand(updatedStock.getSymbol()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/investors/{investorId}/stocks/{symbol}")
    public ResponseEntity<Void> deleteAStockFromTheInvestorPortfolio(@PathVariable String investorId,
                                                                     @PathVariable String symbol) {
        if (investorService.deleteAStockFromTheInvestorPortfolio(investorId, symbol)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(null);
    }
}
