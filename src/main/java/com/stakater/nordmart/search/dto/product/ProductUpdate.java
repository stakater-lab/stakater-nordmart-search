package com.stakater.nordmart.search.dto.product;

import lombok.Data;

import static com.stakater.nordmart.search.dto.product.ProductCommandType.PRODUCT_UPDATE;

@Data
public class ProductUpdate implements ProductCommand {

    @Override
    public ProductCommandType getType() {
        return PRODUCT_UPDATE;
    }

    private String itemId;
    private String name;
    private String description;
    private double price;
}
