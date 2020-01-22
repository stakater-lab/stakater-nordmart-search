package com.stakater.nordmart.search.controller;

import com.stakater.nordmart.search.dto.search.ProductSearchCriteria;
import com.stakater.nordmart.search.dto.search.ProductSearchResults;
import com.stakater.nordmart.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Api("Controller responsible for search products operations.")
@RequestMapping(path = "/api/v1/product-search", produces = APPLICATION_JSON_VALUE)
@RestController
public class ProductSearchController {
    @Autowired
    private SearchService searchService;

    @ApiOperation("Performs search by provided criteria. Returns DTO which includes search criteria" +
            " and list of found products." )
    @GetMapping
    public ProductSearchResults search(@RequestParam("criteria") String criteria) {
        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();
        productSearchCriteria.setCriteria(criteria);
        return searchService.performSearch(productSearchCriteria);
    }
}
