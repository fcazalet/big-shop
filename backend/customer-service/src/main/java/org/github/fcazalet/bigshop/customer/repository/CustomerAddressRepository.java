package org.github.fcazalet.bigshop.customer.repository;

import org.github.fcazalet.bigshop.customer.model.db.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByCustomerId(Long customerId);
    Optional<CustomerAddress> findOneByIdAndCustomerId(Long addressId, Long customerId);
}
