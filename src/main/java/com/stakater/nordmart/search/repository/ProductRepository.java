package com.stakater.nordmart.search.repository;

import com.stakater.nordmart.search.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Repository responsible for operations with Elasticsearch data store.
 */
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    /**
     * Finds products by the name or description.
     * @param name name of the product
     * @param description description of the product
     * @return list of products that were found in ES
     */
    List<Product> findByNameLikeOrDescriptionLike(String name, String description);
}
