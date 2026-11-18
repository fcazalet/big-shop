package org.github.fcazalet.bigshop.customer.service;

import org.github.fcazalet.bigshop.customer.mapper.CustomerAddressMapper;
import org.github.fcazalet.bigshop.customer.model.db.Customer;
import org.github.fcazalet.bigshop.customer.model.db.CustomerAddress;
import org.github.fcazalet.bigshop.customer.model.request.AccountAddressRequest;
import org.github.fcazalet.bigshop.customer.model.response.AccountAddressResponse;
import org.github.fcazalet.bigshop.customer.repository.CustomerAddressRepository;
import org.github.fcazalet.bigshop.customer.repository.CustomerRepository;
import org.github.fcazalet.bigshop.customer.service.keycloak.KeycloakAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final KeycloakAdminClient keycloak;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public AddressService(KeycloakAdminClient keycloak, CustomerRepository customerRepository, CustomerAddressRepository customerAddressRepository) {
        this.keycloak = keycloak;
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
    }


    /**
     * Return account addresses list
     *
     * @param customerId
     * @return
     */
    public List<AccountAddressResponse> listAddresses(Long customerId) {
        List<AccountAddressResponse> responseList = new ArrayList<>();
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(customerId);
        for (CustomerAddress address : addresses) {
            responseList.add(CustomerAddressMapper.toResponse(address));
        }
        logger.debug("List customer {} addresses", customerId);
        return responseList;
    }


    /**
     * Add an address to customer
     *
     * @param customerId
     * @param request
     * @return
     */
    public AccountAddressResponse addAddress(Long customerId, AccountAddressRequest request) {
        CustomerAddress address = CustomerAddressMapper.toEntity(request);
        Customer customer = new Customer();
        customer.setId(customerId);
        address.setCustomer(customer);
        address = customerAddressRepository.save(address);
        logger.debug("Address {} created on customer {}", address.getId(), customerId);
        return CustomerAddressMapper.toResponse(address);
    }


    /**
     * Add an address to customer
     *
     * @param customerId
     * @param request
     * @return
     */
    public AccountAddressResponse updateAddress(Long customerId, Long addressId, AccountAddressRequest request) {
        Optional<CustomerAddress> oAddress = customerAddressRepository.findOneByIdAndCustomerId(addressId, customerId);
        if (oAddress.isEmpty()) {
            logger.debug("No address {} find for customer {}", addressId, customerId);
            return null;
        } else {
            CustomerAddress address = oAddress.get();
            address.setFirstName(request.getFirstName());
            address.setLastName(request.getLastName());
            address.setCompanyName(request.getCompanyName());
            address.setVatNumber(request.getVatNumber());
            address.setStreetAddress(request.getStreetAddress());
            address.setAddress2(request.getAddress2());
            address.setPostalCode(request.getPostalCode());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setCountry(request.getCountry());
            address = customerAddressRepository.save(address);
            logger.debug("Address {} update for customer {}", addressId, customerId);
            return CustomerAddressMapper.toResponse(address);
        }
    }


    /**
     * Remove an address from customer
     *
     * @param customerId
     * @param addressId
     * @return
     */
    public boolean removeAddress(Long customerId, Long addressId) {
        Optional<CustomerAddress> oAddress = customerAddressRepository.findOneByIdAndCustomerId(addressId, customerId);
        if (oAddress.isEmpty()) {
            logger.debug("No address {} find for customer {}", addressId, customerId);
            return false;
        } else {
            customerAddressRepository.delete(oAddress.get());
            logger.debug("Address {} removed from customer {}", addressId, customerId);
            return true;
        }
    }


    /**
     * Set an address as default shipping for customer
     *
     * @param customerId
     * @param addressId
     * @return
     */
    public boolean setDefaultShippingAddress(Long customerId, Long addressId) {
        Optional<CustomerAddress> oAddress = customerAddressRepository.findOneByIdAndCustomerId(addressId, customerId);
        if (oAddress.isEmpty()) {
            logger.debug("No address {} find for customer {}", addressId, customerId);
            return false;
        } else {
            CustomerAddress address = oAddress.get();
            Customer customer = address.getCustomer();
            customer.setDefaultShippingAddress(address);
            customerRepository.save(customer);
            logger.debug("Address {} set as default shipping on customer {}", addressId, customerId);
            return true;
        }
    }


    /**
     * Set an address as default invoicing from customer
     *
     * @param customerId
     * @param addressId
     * @return
     */
    public boolean setDefaultInvoicingAddress(Long customerId, Long addressId) {
        Optional<CustomerAddress> oAddress = customerAddressRepository.findOneByIdAndCustomerId(addressId, customerId);
        if (oAddress.isEmpty()) {
            logger.debug("No address {} find for customer {}", addressId, customerId);
            return false;
        } else {
            CustomerAddress address = oAddress.get();
            Customer customer = address.getCustomer();
            customer.setDefaultInvoiceAddress(address);
            customerRepository.save(customer);
            logger.debug("Address {} set as default invoicing on customer {}", addressId, customerId);
            return true;
        }
    }
}
