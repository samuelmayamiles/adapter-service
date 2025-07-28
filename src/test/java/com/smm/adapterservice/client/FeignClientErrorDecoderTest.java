package com.smm.adapterservice.client;

import com.smm.adapterservice.errorhandling.exception.NotFoundException;
import com.smm.adapterservice.errorhandling.exception.RemoteServiceUnavailableException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static feign.Request.HttpMethod.GET;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class FeignClientErrorDecoderTest {

    @InjectMocks
    private FeignClientErrorDecoder feignClientErrorDecoder;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void should_ReturnRemoteServiceUnavailableException_When_DecodeAndHttpStatusIs5xxFamily() {

        // Given
        final Request request = Request.create(GET, RandomStringUtils.randomAlphabetic(20), Map.of(), Request.Body.create(RandomStringUtils.randomAlphabetic(20)), new RequestTemplate());

        // When

        final Exception exception = feignClientErrorDecoder.decode(EMPTY, Response.builder().request(request).status(nextInt(500, 511)).build());

        // Then
        assertThat(exception).isNotNull().isInstanceOf(RemoteServiceUnavailableException.class);
    }

    @Test
    void should_ReturnNotFoundException_When_DecodeAndHttpStatusIsNotFound() {

        // Given
        final Request request = Request.create(GET, RandomStringUtils.randomAlphabetic(20), Map.of(), Request.Body.create(RandomStringUtils.randomAlphabetic(20)), new RequestTemplate());

        // When

        final Exception exception = feignClientErrorDecoder.decode(EMPTY, Response.builder().request(request).status(NOT_FOUND.value()).build());

        // Then
        assertThat(exception).isNotNull().isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest(name = "Check ServiceUnavailableException when HttpStatus is {0}")
    @EnumSource(value = HttpStatus.class)
    void should_ReturnFeignException_When_DecodeAndAnyOtherStatus(final HttpStatus httpStatus) {

        // Given
        final Request request = Request.create(GET, RandomStringUtils.randomAlphabetic(20), Map.of(), Request.Body.create(RandomStringUtils.randomAlphabetic(20)), new RequestTemplate());

        // When
        final Exception exception = feignClientErrorDecoder.decode(EMPTY, Response.builder().request(request).status(httpStatus.value()).build());

        // Then
        if (NOT_FOUND.equals(httpStatus) || httpStatus.is5xxServerError()) {
            assertThat(exception).isNotNull().isNotInstanceOf(FeignException.class);
        } else {
            assertThat(exception).isNotNull().isInstanceOf(FeignException.class);
        }
    }
}