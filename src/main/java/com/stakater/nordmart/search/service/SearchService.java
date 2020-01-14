package com.stakater.nordmart.search.service;

import com.stakater.nordmart.search.dto.search.ProductSearchCriteria;
import com.stakater.nordmart.search.dto.search.ProductSearchResults;

/**
 * Service responsible for search related operations in data store.
 */
public interface SearchService {
    /**
     * Performs search in data store by provided search criteria.
     *
     * @param productSearchCriteria search criteria used to search products in data store
     * @return search results which consist of search criteria and list of found products
     */
    ProductSearchResults performSearch(ProductSearchCriteria productSearchCriteria);
}
