package io.github.fcazalet.bigshop.customer.mapper;

import io.github.fcazalet.bigshop.customer.model.db.CustomerAddress;
import io.github.fcazalet.bigshop.customer.model.request.AccountAddressRequest;
import io.github.fcazalet.bigshop.customer.model.response.AccountAddressResponse;

public class CustomerAddressMapper {

    public static CustomerAddress toEntity(AccountAddressRequest request){
        CustomerAddress address = new CustomerAddress();
        address.setFirstName(request.getFirstName());
        address.setLastName(request.getLastName());
        address.setCompanyName(request.getCompanyName());
        address.setVatNumber(request.getVatNumber());
        address.setStreetAddress(request.getStreetAddress());
        address.setAddress2(request.getAddress2());
        address.setCity(request.getAddress2());
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        return address;
    }

    public static AccountAddressResponse toResponse(CustomerAddress address){
        AccountAddressResponse response = new AccountAddressResponse();
        response.setId(address.getId());
        response.setFirstName(address.getFirstName());
        response.setLastName(address.getLastName());
        response.setCompanyName(address.getCompanyName());
        response.setVatNumber(address.getVatNumber());
        response.setStreetAddress(address.getStreetAddress());
        response.setAddress2(address.getAddress2());
        response.setCity(address.getAddress2());
        response.setPostalCode(address.getPostalCode());
        response.setState(address.getState());
        response.setCountry(address.getCountry());
        return response;
    }
}
