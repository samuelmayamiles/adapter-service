package com.smm.adapterservice.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smm.adapterservice.client.mock.MockService;
import com.smm.adapterservice.errorhandling.exception.NotFoundException;
import com.smm.adapterservice.errorhandling.exception.RemoteServiceUnavailableException;
import com.smm.adapterservice.errorhandling.model.ErrorResponse;
import com.smm.adapterservice.product.model.ProductMapper;
import com.smm.adapterservice.product.model.SimilarProducts;
import com.smm.adapterservice.utils.RestTemplateUtils;
import com.smm.adapterserviceclient.factory.AdapterServiceRequestFactory;
import com.smm.adapterserviceclient.model.product.response.SimilarProductsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class SimilarProductsGetITest {

    @LocalServerPort
    private Integer randomServerPort;

    @MockBean
    private MockService mockService;

    @Autowired
    private ProductMapper productMapper;

    private RestTemplate restTemplate;

    private AdapterServiceRequestFactory adapterServiceRequestFactory;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        adapterServiceRequestFactory = new AdapterServiceRequestFactory(String.format("http://localhost:%s", randomServerPort));
    }

    @Test
    void should_ReturnOkAndSimilarProductsDto_When_GetSimilarProductsUsingClient() {

        // Given
        final String productId = randomNumeric(1);
        final SimilarProducts similarProducts = SimilarProducts.builder().build();

        given(mockService.retrieveSimilarProducts(productId)).willReturn(similarProducts);

        final SimilarProductsDTO expected = productMapper.toDto(similarProducts);

        // When
        final SimilarProductsDTO similarProductsDTO = adapterServiceRequestFactory.productGetSimilarRequestBuilder()
                .withProductId(productId)
                .build()
                .execute();

        // Then
        assertThat(similarProductsDTO).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void should_ReturnOkAndSimilarProductsDto_When_GetSimilarProducts() {

        // Given
        final String productId = randomNumeric(1);
        final SimilarProducts similarProducts = SimilarProducts.builder().build();

        given(mockService.retrieveSimilarProducts(productId)).willReturn(similarProducts);

        final SimilarProductsDTO expected = productMapper.toDto(similarProducts);

        // When
        final ResponseEntity<SimilarProductsDTO> response = restTemplate.exchange(getUrl(productId), GET, RestTemplateUtils.buildHttpEntity(null), SimilarProductsDTO.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isNotNull().isEqualTo(OK);
        assertThat(response.getBody()).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void should_ReturnNotFoundAndErrorResponse_When_GetSimilarProductsAndClientReturnsEmpty() throws JsonProcessingException {

        // Given
        final String productId = randomNumeric(1);

        given(mockService.retrieveSimilarProducts(productId)).willThrow(new NotFoundException());

        final ErrorResponse expected = ErrorResponse.builder()
                .message("Not Found")
                .build();

        // When
        final Throwable throwable = catchThrowable(() -> restTemplate.exchange(getUrl(productId), GET, RestTemplateUtils.buildHttpEntity(null), SimilarProductsDTO.class));

        // Then
        verify(mockService).retrieveSimilarProducts(productId);
        assertThat(throwable).isNotNull().isInstanceOf(HttpClientErrorException.class).hasMessageContaining(RestTemplateUtils.toJson(expected));
    }

    @Test
    void should_ReturnServiceUnavailableAndErrorResponse_When_GetSimilarProductsAndClientReturnsEmpty() throws JsonProcessingException {

        // Given
        final String productId = randomNumeric(1);

        given(mockService.retrieveSimilarProducts(productId)).willThrow(new RemoteServiceUnavailableException());

        final ErrorResponse expected = ErrorResponse.builder()
                .message("Remote Service is currently unavailable")
                .build();

        // When
        final Throwable throwable = catchThrowable(() -> restTemplate.exchange(getUrl(productId), GET, RestTemplateUtils.buildHttpEntity(null), SimilarProductsDTO.class));

        // Then
        verify(mockService).retrieveSimilarProducts(productId);
        assertThat(throwable).isNotNull().isInstanceOf(HttpServerErrorException.class).hasMessageContaining(RestTemplateUtils.toJson(expected));
    }

    private String getUrl(final String operatorId) {
        return format("http://localhost:%d/product/%s/similar", randomServerPort, operatorId);
    }
}
