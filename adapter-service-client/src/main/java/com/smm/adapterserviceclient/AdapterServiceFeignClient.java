package com.smm.adapterserviceclient;

import com.smm.adapterserviceclient.model.product.response.SimilarProductsDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface AdapterServiceFeignClient {

    // Products
    @RequestLine("GET /product/{productId}/similar")
    @Headers({"Accept: application/adapter-service.api.v1+json"})
    SimilarProductsDTO getSimilarProducts(@Param("productId") String productId);
}
