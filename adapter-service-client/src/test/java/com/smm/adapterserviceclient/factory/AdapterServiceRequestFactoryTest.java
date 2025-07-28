package com.smm.adapterserviceclient.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdapterServiceRequestFactoryTest {

    private static final String ENDPOINT = "http://localhost";

    private AdapterServiceRequestFactory adapterServiceRequestFactory;

    @BeforeEach
    void setUp() {
        adapterServiceRequestFactory = new AdapterServiceRequestFactory(ENDPOINT);
    }

    @Test
    void should_CreateBuilderWithClient_When_AuditSearchRequest() {

        // When
        final ProductGetSimilarRequest.Builder request = adapterServiceRequestFactory.productGetSimilarRequestBuilder();

        // Then
        assertThat(request).isNotNull().hasNoNullFieldsOrPropertiesExcept("productId");
    }
}