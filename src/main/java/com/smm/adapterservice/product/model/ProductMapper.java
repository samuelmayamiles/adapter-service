package com.smm.adapterservice.product.model;

import com.smm.adapterserviceclient.model.product.response.SimilarProductsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    SimilarProductsDTO toDto(final SimilarProducts domain);
}
