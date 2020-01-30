package com.stakater.nordmart.search.mapper;

import com.stakater.nordmart.search.dto.search.ProductDto;
import com.stakater.nordmart.search.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    public abstract ProductDto productToProductDto(Product product);
}
