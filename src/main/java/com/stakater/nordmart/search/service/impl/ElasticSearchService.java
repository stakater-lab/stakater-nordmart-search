package com.stakater.nordmart.search.service.impl;

import com.stakater.nordmart.search.dto.search.ProductDto;
import com.stakater.nordmart.search.dto.search.ProductSearchCriteria;
import com.stakater.nordmart.search.dto.search.ProductSearchResults;
import com.stakater.nordmart.search.mapper.ProductMapper;
import com.stakater.nordmart.search.model.Product;
import com.stakater.nordmart.search.repository.ProductRepository;
import com.stakater.nordmart.search.service.SearchService;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class ElasticSearchService implements SearchService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductSearchResults performSearch(ProductSearchCriteria productSearchCriteria) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        multiMatchQuery(productSearchCriteria.getCriteria().toLowerCase(), "name", "description")
                        .operator(AND)
                        .fuzziness(Fuzziness.TWO)
                        .prefixLength(3)
                )
                .build();
        Page<Product> products = productRepository.search(searchQuery);

        List<ProductDto> productDtos = products.stream()
                .map(product -> productMapper.productToProductDto(product))
                .collect(Collectors.toList());

        return new ProductSearchResults(productSearchCriteria.getCriteria(), productDtos);
    }
}
