package com.smm.adapterservice.product;

import com.smm.adapterservice.errorhandling.model.ErrorResponse;
import com.smm.adapterservice.product.model.ProductMapper;
import com.smm.adapterserviceclient.model.product.response.SimilarProductsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Log4j2
@Validated
@RestController
@RequestMapping(value = "/product")
@Tag(name = "Product Controller | Allows operations over Products")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Unexpected Error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
})
class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(
            summary = "Get Similar products | Retrieves products similar to the one given by Product Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieved", content = {@Content(schema = @Schema(implementation = SimilarProductsDTO.class))})
            }
    )
    @GetMapping(value = "/{productId}/similar", produces = "application/adapter-service.api.v1+json")
    ResponseEntity<SimilarProductsDTO> getSimilarProducts(@Valid @PathVariable @NotBlank String productId) {

        log.info("Request received to retrieve products similar to productId {}", productId);
        return ResponseEntity.ok(productMapper.toDto(productService.retrieveSimilarProducts(productId)));
    }
}
