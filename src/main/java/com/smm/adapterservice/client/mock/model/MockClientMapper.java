package com.smm.adapterservice.client.mock.model;

import com.smm.adapterservice.product.model.ProductDetail;
import com.smm.adapterservice.product.model.SimilarProducts;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MockClientMapper {

    SimilarProducts toDomain(final Integer count, final List<ProductDetail> items);

    ProductDetail toDomain(final ProductDetailResponse response);
}
