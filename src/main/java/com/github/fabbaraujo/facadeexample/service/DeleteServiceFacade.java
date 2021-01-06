package com.github.fabbaraujo.facadeexample.service;

import java.util.List;

public interface DeleteServiceFacade {
    boolean deleteAStock(String investorId, String stockTobeDeletedSymbol);
    boolean deleteStocksInBulk(String investorId, List<String> stocksSymbolsList);
}
