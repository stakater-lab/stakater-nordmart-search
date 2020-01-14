package com.stakater.nordmart.search.dto.product;

import lombok.Data;

import static com.stakater.nordmart.search.dto.product.ProductCommandType.PRODUCT_DELETE;

@Data
public class ProductDelete implements ProductCommand {

    @Override
    public ProductCommandType getType() {
        return PRODUCT_DELETE;
    }

    private String itemId;
}
