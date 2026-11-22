package io.github.fcazalet.bigshop.catalog.repository;

import io.github.fcazalet.bigshop.catalog.model.db.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
