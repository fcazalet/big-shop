package io.github.fcazalet.bigshop.customer.repository;

import io.github.fcazalet.bigshop.customer.model.db.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
