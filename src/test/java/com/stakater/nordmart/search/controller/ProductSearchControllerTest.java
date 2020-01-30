package com.stakater.nordmart.search.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stakater.nordmart.search.BaseTest;
import com.stakater.nordmart.search.dto.product.ProductCreate;
import com.stakater.nordmart.search.dto.product.ProductDelete;
import com.stakater.nordmart.search.dto.product.ProductUpdate;
import com.stakater.nordmart.search.dto.search.ProductDto;
import com.stakater.nordmart.search.dto.search.ProductSearchResults;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

@DirtiesContext
public class ProductSearchControllerTest extends BaseTest {
    private static final String CAR_CRITERIA = "Car";
    private static final String ITEM_ID = "1";
    private static final String PRODUCT_NAME = "Car model";
    private static final String PRODUCT_DESCRIPTION = "Car model toy";
    private static final int PRODUCT_PRICE = 50;
    private static final String UPDATE_PRODUCT_NAME = "Car model 2";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testSearch() throws Exception {
        ProductCreate productCreate = createProductCommand();

        String productCreateJson = objectMapper.writeValueAsString(productCreate);
        kafkaTemplate.send(productTopicName, productCreateJson);

        testProductSearch();
    }

    @Test
    void testProductUpdate() throws Exception {
        ProductCreate productCreate = createProductCommand();

        String productCreateJson = objectMapper.writeValueAsString(productCreate);
        kafkaTemplate.send(productTopicName, productCreateJson);

        testProductSearch();

        ProductUpdate productUpdate = new ProductUpdate();
        productUpdate.setItemId(productCreate.getItemId());
        productUpdate.setName(UPDATE_PRODUCT_NAME);
        productUpdate.setDescription(productCreate.getDescription());
        productUpdate.setPrice(PRODUCT_PRICE);

        String productUpdateJson = objectMapper.writeValueAsString(productUpdate);
        kafkaTemplate.send(productTopicName, productUpdateJson);

        ProductSearchResults productSearchResults = performSearch();

        assertEquals(CAR_CRITERIA, productSearchResults.getCriteria());
        List<ProductDto> products = productSearchResults.getProducts();
        assertEquals(1, products.size());
        ProductDto productDto = products.get(0);
        assertEquals(ITEM_ID, productDto.getItemId());
        assertEquals(UPDATE_PRODUCT_NAME, productDto.getName());
        assertEquals(PRODUCT_DESCRIPTION, productDto.getDescription());
        assertEquals(PRODUCT_PRICE, productDto.getPrice());
    }

    @Test
    void testProductDelete() throws Exception {
        ProductCreate productCreate = createProductCommand();

        String productCreateJson = objectMapper.writeValueAsString(productCreate);
        kafkaTemplate.send(productTopicName, productCreateJson);

        testProductSearch();

        ProductDelete productDelete = new ProductDelete();
        productDelete.setItemId(productCreate.getItemId());

        String productDeleteJson = objectMapper.writeValueAsString(productDelete);
        kafkaTemplate.send(productTopicName, productDeleteJson);

        ProductSearchResults productSearchResults = performSearch();

        assertEquals(CAR_CRITERIA, productSearchResults.getCriteria());
        List<ProductDto> products = productSearchResults.getProducts();
        assertEquals(0, products.size());
    }

    private void testProductSearch() throws InterruptedException, JsonProcessingException {
        ProductSearchResults productSearchResults = performSearch();

        assertEquals(CAR_CRITERIA, productSearchResults.getCriteria());
        List<ProductDto> products = productSearchResults.getProducts();
        assertEquals(1, products.size());
        ProductDto productDto = products.get(0);
        assertEquals(ITEM_ID, productDto.getItemId());
        assertEquals(PRODUCT_NAME, productDto.getName());
        assertEquals(PRODUCT_DESCRIPTION, productDto.getDescription());
        assertEquals(PRODUCT_PRICE, productDto.getPrice());
    }

    @NotNull
    private ProductSearchResults performSearch() throws InterruptedException, JsonProcessingException {
        ResponseEntity<String> responseEntity;
        ProductSearchResults productSearchResults = null;

        int counter = 10;
        boolean dataIndexed = false;
        while (!dataIndexed && counter > 0) {
            Thread.sleep(1000);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTestServerUrl() + CONTROLLER_PATH)
                    .queryParam("criteria", CAR_CRITERIA);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    entity, String.class);

            counter--;
            if (responseEntity.hasBody() && OK.equals(responseEntity.getStatusCode())) {
                ObjectMapper objectMapper = new ObjectMapper();
                productSearchResults = objectMapper.readValue(responseEntity.getBody(), ProductSearchResults.class);
                dataIndexed = true;
            }
        }
        return productSearchResults;
    }

    @NotNull
    private ProductCreate createProductCommand() {
        ProductCreate productCreate = new ProductCreate();
        productCreate.setItemId(ITEM_ID);
        productCreate.setName(PRODUCT_NAME);
        productCreate.setDescription(PRODUCT_DESCRIPTION);
        productCreate.setPrice(PRODUCT_PRICE);
        return productCreate;
    }
}