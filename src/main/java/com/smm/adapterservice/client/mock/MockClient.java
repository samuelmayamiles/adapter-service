package com.smm.adapterservice.client.mock;

import com.smm.adapterservice.client.FeignClientErrorDecoder;
import com.smm.adapterservice.client.mock.model.ProductDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "mock-service", url = "${services.mock-service}", configuration = FeignClientErrorDecoder.class)
interface MockClient {

    @GetMapping(value = "/product/{productId}/similarids")
    List<String> getProductIds(@PathVariable("productId") String productId);

    @GetMapping(value = "/product/{productId}")
    Optional<ProductDetailResponse> getProductById(@PathVariable("productId") String productId);
}
