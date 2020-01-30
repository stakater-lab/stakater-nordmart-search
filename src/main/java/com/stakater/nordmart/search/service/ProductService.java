package com.stakater.nordmart.search.service;

import com.stakater.nordmart.search.model.Product;

/**
 * Service responsible for all operations with product.
 */
public interface ProductService {

    /**
     * Stores product.
     *
     * @param product product to save
     * @return saved product
     */
    Product store(Product product);

    /**
     * Updates existing product. Saves new product if 
     *
     * @param product product which needs update
     * @return updated product
     */
    Product update(Product product);

    /**
     * Deletes product by id.
     *
     * @param productId id of product to delete
     */
    void delete(String productId);
}
