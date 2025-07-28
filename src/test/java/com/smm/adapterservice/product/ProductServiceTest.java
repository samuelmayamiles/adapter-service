package com.smm.adapterservice.product;

import com.smm.adapterservice.client.mock.MockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceTest {

    @Mock
    private MockService mockService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void should_DelegateToService_When_DelegateToService() {

        // Given
        final String productId = randomNumeric(1);

        // When
        productService.retrieveSimilarProducts(productId);

        // Then
        then(mockService).should().retrieveSimilarProducts(productId);
        then(mockService).shouldHaveNoMoreInteractions();
    }
}