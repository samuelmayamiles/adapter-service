package com.smm.adapterserviceclient.factory;

import com.smm.adapterserviceclient.AdapterServiceFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductGetSimilarRequestTest {

    private AdapterServiceFeignClient adapterServiceFeignClient;

    @BeforeEach
    void setUp() {
        adapterServiceFeignClient = mock(AdapterServiceFeignClient.class);
    }

    @Test
    void should_CallAdapterServiceFeignClient_When_Execute() {

        // Given
        final String productId = randomNumeric(20);

        final ProductGetSimilarRequest request = ProductGetSimilarRequest.builder(adapterServiceFeignClient)
                .withProductId(productId)
                .build();

        // When
        request.execute();

        // Then
        verify(adapterServiceFeignClient).getSimilarProducts(productId);
    }
}