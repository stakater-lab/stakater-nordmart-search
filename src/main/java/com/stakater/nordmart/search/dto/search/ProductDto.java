package com.stakater.nordmart.search.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductDto {
    @EqualsAndHashCode.Include
    private String itemId;
    private String name;
    private String description;
    private double price;
    private Date created;
    private Date updated;
}
