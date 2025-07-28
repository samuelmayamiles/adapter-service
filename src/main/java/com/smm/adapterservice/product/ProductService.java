package com.smm.adapterservice.product;

import com.smm.adapterservice.client.mock.MockService;
import com.smm.adapterservice.product.model.SimilarProducts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
class ProductService {

    @Autowired
    private MockService mockService;

    public SimilarProducts retrieveSimilarProducts(final String productId) {

        return mockService.retrieveSimilarProducts(productId);
    }
}
