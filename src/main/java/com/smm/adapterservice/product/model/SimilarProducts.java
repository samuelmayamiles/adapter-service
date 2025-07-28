package com.smm.adapterservice.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
public class SimilarProducts {

    @Builder.Default
    private Integer count = 0;
    @Builder.Default
    private List<ProductDetail> items = List.of();
}
