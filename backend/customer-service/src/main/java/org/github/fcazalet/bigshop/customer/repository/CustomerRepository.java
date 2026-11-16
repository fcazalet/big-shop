package org.github.fcazalet.bigshop.customer.repository;

import org.github.fcazalet.bigshop.customer.model.db.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
