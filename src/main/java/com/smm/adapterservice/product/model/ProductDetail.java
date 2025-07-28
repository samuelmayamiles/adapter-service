package com.smm.adapterservice.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
public class ProductDetail {

    private String id;
    private String name;
    private BigDecimal price;
    private Boolean availability;
}
