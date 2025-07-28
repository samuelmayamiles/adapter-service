package com.smm.adapterserviceclient.factory;

import com.smm.adapterserviceclient.AdapterServiceFeignClient;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class AdapterServiceRequestFactory {

    private final AdapterServiceFeignClient adapterServiceFeignClient;

    public AdapterServiceRequestFactory(final String endpoint) {
        this(endpoint, getDefaultInterceptor(), getDefaultErrorDecoder());
    }

    public AdapterServiceRequestFactory(final String endpoint, final RequestInterceptor requestInterceptor, final ErrorDecoder errorDecoder) {

        this.adapterServiceFeignClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .errorDecoder(errorDecoder)
                .requestInterceptor(requestInterceptor)
                .target(AdapterServiceFeignClient.class, endpoint);
    }

    private static RequestInterceptor getDefaultInterceptor() {
        return requestTemplate -> {};
    }

    private static ErrorDecoder getDefaultErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    // Products
    public ProductGetSimilarRequest.Builder productGetSimilarRequestBuilder() {
        return ProductGetSimilarRequest.builder(adapterServiceFeignClient);
    }
}
