package com.stakater.nordmart.search.dto.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductCreate.class, name = "productCreate"),
        @JsonSubTypes.Type(value = ProductUpdate.class, name = "productUpdate"),
        @JsonSubTypes.Type(value = ProductDelete.class, name = "productDelete"),
})
public interface ProductCommand {

    ProductCommandType getType();
}
