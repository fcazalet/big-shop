package io.github.fcazalet.bigshop.catalog.mapper;

import io.github.fcazalet.bigshop.catalog.model.db.Product;
import io.github.fcazalet.bigshop.catalog.model.request.ProductRequest;
import io.github.fcazalet.bigshop.catalog.model.response.ProductResponse;

public class ProductMapper {

    public static Product toEntity(ProductRequest product) {
        return null;
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        return response;
    }
}
