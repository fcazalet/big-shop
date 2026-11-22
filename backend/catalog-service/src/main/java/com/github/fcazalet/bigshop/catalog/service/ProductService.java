package io.github.fcazalet.bigshop.catalog.service;

import io.github.fcazalet.bigshop.catalog.mapper.ProductMapper;
import io.github.fcazalet.bigshop.catalog.model.db.Product;
import io.github.fcazalet.bigshop.catalog.model.response.ProductResponse;
import io.github.fcazalet.bigshop.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse getProduct(Long id){
        Optional<Product> oproduct = productRepository.findById(id);
        if (!oproduct.isPresent()) return null;

        return ProductMapper.toResponse(oproduct.get());
    }
}
