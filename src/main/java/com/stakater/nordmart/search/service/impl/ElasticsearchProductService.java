package com.stakater.nordmart.search.service.impl;

import com.stakater.nordmart.search.model.Product;
import com.stakater.nordmart.search.repository.ProductRepository;
import com.stakater.nordmart.search.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchProductService implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product store(Product product) {
        logger.debug("Going to save product: {}", product);
        Product savedProduct = productRepository.save(product);
        logger.debug("Successfully saved product: {}", product);
        return savedProduct;
    }

    @Override
    public Product update(Product product) {
        logger.debug("Going to updated product: {}", product);
        Product updatedProduct = productRepository.save(product);
        logger.debug("Successfully updated product: {}", product);
        return updatedProduct;
    }

    @Override
    public void  delete(String productId) {
        logger.debug("Going to delete product with id {}", productId);
        productRepository.deleteById(productId);
        logger.debug("Deleted product with id {}", productId);
    }
}
