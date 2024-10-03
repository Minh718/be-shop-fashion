package com.shopro.shop1905.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.services.IndexingService;

@RestController
public class IndexingController {

    @Autowired
    private IndexingService indexingService;

    @GetMapping("/index-lucene-product")
    public String index() {
        indexingService.indexExistingData();
        return "Indexing started";
    }
}
