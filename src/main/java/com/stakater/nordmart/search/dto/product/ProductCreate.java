package com.stakater.nordmart.search.dto.product;

import lombok.Data;

import static com.stakater.nordmart.search.dto.product.ProductCommandType.PRODUCT_CREATE;

@Data
public class ProductCreate implements ProductCommand {

    @Override
    public ProductCommandType getType() {
        return PRODUCT_CREATE;
    }

    private String itemId;
    private String name;
    private String description;
    private double price;
}
