package io.github.fcazalet.bigshop.customer.service;

import io.github.fcazalet.bigshop.customer.model.db.Customer;
import io.github.fcazalet.bigshop.customer.model.response.AccountInformationResponse;
import io.github.fcazalet.bigshop.customer.repository.CustomerRepository;
import io.github.fcazalet.bigshop.customer.service.keycloak.KeycloakAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final KeycloakAdminClient keycloak;
    private final CustomerRepository customerRepository;

    public AccountService(KeycloakAdminClient keycloak, CustomerRepository customerRepository) {
        this.keycloak = keycloak;
        this.customerRepository = customerRepository;
    }


    /**
     * Return account information from customer id
     *
     * @param customerId
     * @return
     */
    public AccountInformationResponse readAccount(Long customerId) {
        Optional<Customer> oCustomer = customerRepository.findById(customerId);
        if (oCustomer.isEmpty()) {
            return null;
        }
        Customer customer = oCustomer.get();
        AccountInformationResponse response = new AccountInformationResponse();
        response.setId(customer.getId());
        response.setEmail(customer.getEmail());
        response.setCreatedAt(customer.getCreatedAt());
        return response;
    }
}
