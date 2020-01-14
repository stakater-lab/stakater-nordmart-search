package com.stakater.nordmart.search.facade.impl;

import com.stakater.nordmart.search.dto.product.ProductCommand;
import com.stakater.nordmart.search.dto.product.ProductCreate;
import com.stakater.nordmart.search.dto.product.ProductDelete;
import com.stakater.nordmart.search.dto.product.ProductUpdate;
import com.stakater.nordmart.search.facade.ProductCommandFacade;
import com.stakater.nordmart.search.mapper.ProductCommandsMapper;
import com.stakater.nordmart.search.model.Product;
import com.stakater.nordmart.search.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductCommandFacadeImpl implements ProductCommandFacade {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandFacadeImpl.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCommandsMapper productCommandsMapper;

    @Override
    public void processProductCommand(ProductCommand productCommand) {
        logger.info("Going to process product command: {}", productCommand);

        switch (productCommand.getType()) {
            case PRODUCT_CREATE: {
                Product product = productCommandsMapper.productCreateToProduct((ProductCreate) productCommand);
                productService.store(product);
                break;
            }
            case PRODUCT_UPDATE: {
                Product product = productCommandsMapper.productUpdateToProduct((ProductUpdate) productCommand);
                productService.update(product);
                break;
            }
            case PRODUCT_DELETE: {
                ProductDelete productDelete = (ProductDelete) productCommand;
                productService.delete(productDelete.getItemId());
                break;
            } default:
                logger.error("Got unsupported product command {}", productCommand);
        }

    }
}
