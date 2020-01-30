package com.stakater.nordmart.search.mapper;

import com.stakater.nordmart.search.dto.product.ProductCreate;
import com.stakater.nordmart.search.dto.product.ProductUpdate;
import com.stakater.nordmart.search.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductCommandsMapper {
    public abstract Product productCreateToProduct(ProductCreate productCreate);

    public abstract Product productUpdateToProduct(ProductUpdate productUpdate);

}
