package com.stakater.nordmart.search.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResults {

    private String criteria;
    private List<ProductDto> products;

}
