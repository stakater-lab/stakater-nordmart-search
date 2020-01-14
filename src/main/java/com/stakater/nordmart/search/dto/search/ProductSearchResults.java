package com.stakater.nordmart.search.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductSearchResults {

    private String criteria;
    private List<ProductDto> products;
}
