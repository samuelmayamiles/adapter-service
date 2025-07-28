package com.smm.adapterservice.client.mock;

import com.smm.adapterservice.client.mock.model.MockClientMapperImpl;
import com.smm.adapterservice.client.mock.model.ProductDetailResponse;
import com.smm.adapterservice.product.model.SimilarProducts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.openMocks;

class MockServiceTest {

    @Mock
    private MockClient mockClient;

    @Spy
    private MockClientMapperImpl mockClientMapper;

    @InjectMocks
    private MockService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void should_ReturnSimilarProductsWithItems_When_RetrieveSimilarProductsAndClientReturnsValues() {

        // Given
        final String productId = randomNumeric(10);
        final List<ProductDetailResponse> items = IntStream.range(0, 11)
                .mapToObj(index -> ProductDetailResponse.builder()
                        .id(randomNumeric(5))
                        .name(randomAlphabetic(10))
                        .price(BigDecimal.valueOf(nextDouble(1, 50)))
                        .availability(nextBoolean())
                        .build()
                )
                .collect(Collectors.toList());
        final List<String> similarIds = items.stream().map(ProductDetailResponse::getId).collect(Collectors.toList());

        given(mockClient.getProductIds(productId)).willReturn(similarIds);
        items.forEach(item -> given(mockClient.getProductById(item.getId())).willReturn(Optional.of(item)));

        final SimilarProducts expected = SimilarProducts.builder()
                .count(items.size())
                .items(items.stream().map(mockClientMapper::toDomain).collect(Collectors.toList()))
                .build();

        // When
        final SimilarProducts similarProducts = mockService.retrieveSimilarProducts(productId);

        // Then
        assertThat(similarProducts).isNotNull().usingRecursiveComparison().isEqualTo(expected);

        verify(mockClient).getProductIds(productId);
        verify(mockClient, times(similarIds.size())).getProductById(anyString());
        verifyNoMoreInteractions(mockClient);
    }

    @Test
    void should_ThrowException_When_RetrieveSimilarProductsAndClientThrowsException() {

        // Given
        final String productId = randomNumeric(1);

        final RuntimeException thrownException = new RuntimeException(randomAlphabetic(10));
        given(mockClient.getProductIds(productId)).willThrow(thrownException);

        // When
        final Throwable throwable = catchThrowable(() -> mockService.retrieveSimilarProducts(productId));

        // Then
        assertThat(throwable).isNotNull().isInstanceOf(RuntimeException.class).usingRecursiveComparison().isEqualTo(thrownException);
        verify(mockClient).getProductIds(productId);
        verifyNoMoreInteractions(mockClient);
    }
}