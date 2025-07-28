package com.smm.adapterserviceclient.factory;

import com.smm.adapterserviceclient.AdapterServiceFeignClient;
import com.smm.adapterserviceclient.model.product.response.SimilarProductsDTO;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ProductGetSimilarRequest implements Request<SimilarProductsDTO> {

    private final String productId;

    private final AdapterServiceFeignClient adapterServiceFeignClient;

    private ProductGetSimilarRequest(Builder builder) {

        this.productId = builder.productId;
        this.adapterServiceFeignClient = builder.adapterServiceFeignClient;
    }

    static ProductGetSimilarRequest.Builder builder(final AdapterServiceFeignClient adapterServiceFeignClient) {
        return new Builder(adapterServiceFeignClient);
    }

    @Override
    public SimilarProductsDTO execute() {
        return adapterServiceFeignClient.getSimilarProducts(productId);
    }

    public static final class Builder {

        private final AdapterServiceFeignClient adapterServiceFeignClient;

        private String productId;

        Builder(final AdapterServiceFeignClient adapterServiceFeignClient) {

            this.adapterServiceFeignClient = adapterServiceFeignClient;
        }

        public Builder withProductId(final String productId) {

            this.productId = productId;
            return this;
        }

        public ProductGetSimilarRequest build() {

            return new ProductGetSimilarRequest(this);
        }
    }
}
