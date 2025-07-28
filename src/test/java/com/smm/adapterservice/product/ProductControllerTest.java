package com.smm.adapterservice.product;

import com.smm.adapterservice.product.model.ProductMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Spy
    private ProductMapperImpl productMapper;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void should_DelegateToService_When_GetSimilarProducts() {

        // Given
        final String productId = randomNumeric(1);

        // When
        productController.getSimilarProducts(productId);

        // Then
        then(productService).should().retrieveSimilarProducts(productId);
        then(productService).shouldHaveNoMoreInteractions();
    }
}