package com.smm.adapterservice.client;

import com.smm.adapterservice.errorhandling.exception.NotFoundException;
import com.smm.adapterservice.errorhandling.exception.RemoteServiceUnavailableException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import static feign.FeignException.errorStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(final String methodKey, final Response response) {

        final FeignException exception = errorStatus(methodKey, response);
        final HttpStatus httpStatus = HttpStatus.valueOf(response.status());

        if (httpStatus.is5xxServerError()) {
            return new RemoteServiceUnavailableException();
        }

        if (NOT_FOUND.equals(httpStatus)) {
            return new NotFoundException();
        }

        return exception;
    }
}
