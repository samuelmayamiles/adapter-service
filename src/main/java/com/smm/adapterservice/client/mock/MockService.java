package com.smm.adapterservice.client.mock;

import com.smm.adapterservice.client.mock.model.MockClientMapper;
import com.smm.adapterservice.product.model.ProductDetail;
import com.smm.adapterservice.product.model.SimilarProducts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MockService {

    @Autowired
    private MockClient mockClient;

    @Autowired
    private MockClientMapper mockClientMapper;

    public SimilarProducts retrieveSimilarProducts(final String productId) {

        final List<ProductDetail> items = mockClient.getProductIds(productId)
                .stream()
                .map(this::retrieveProduct)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return mockClientMapper.toDomain(items.size(), items);
    }

    private Optional<ProductDetail> retrieveProduct(final String productId) {

        return mockClient.getProductById(productId).map(mockClientMapper::toDomain);
    }
}
