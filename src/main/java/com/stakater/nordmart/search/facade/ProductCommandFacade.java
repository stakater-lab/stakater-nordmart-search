package com.stakater.nordmart.search.facade;

import com.stakater.nordmart.search.dto.product.ProductCommand;

/**
 * Facade for the process command. It handles all the types of process commands.
 */
public interface ProductCommandFacade {

    /**
     * Processes product command. It can process all the product commands supported by the system.
     * @param productCommand process command
     */
    void processProductCommand(ProductCommand productCommand);
}
