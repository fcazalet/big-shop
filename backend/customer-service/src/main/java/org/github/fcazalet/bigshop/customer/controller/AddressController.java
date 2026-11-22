package io.github.fcazalet.bigshop.customer.controller;

import io.github.fcazalet.bigshop.customer.model.request.AccountAddressRequest;
import io.github.fcazalet.bigshop.customer.model.response.AccountAddressResponse;
import io.github.fcazalet.bigshop.customer.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers/me/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(version = "1")
    public ResponseEntity<List<AccountAddressResponse>> getAddressesV1(@AuthenticationPrincipal Jwt jwt) {
        Long customerId = Long.parseLong(jwt.getSubject());
        List<AccountAddressResponse> response = addressService.listAddresses(customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(version = "1")
    public ResponseEntity<AccountAddressResponse> addAddressV1(@AuthenticationPrincipal Jwt jwt, @RequestBody AccountAddressRequest request) {
        Long customerId = Long.parseLong(jwt.getSubject());
        AccountAddressResponse response = addressService.addAddress(customerId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{addressId}", version = "1")
    public ResponseEntity<AccountAddressResponse> updateAddressV1(@AuthenticationPrincipal Jwt jwt, @PathVariable Long addressId, @RequestBody AccountAddressRequest request) {
        Long customerId = Long.parseLong(jwt.getSubject());
        AccountAddressResponse response = addressService.updateAddress(customerId, addressId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{addressId}/default/shipping", version = "1")
    public ResponseEntity<Boolean> setDefaultShippingAddressV1(@AuthenticationPrincipal Jwt jwt, @PathVariable Long addressId) {
        Long customerId = Long.parseLong(jwt.getSubject());
        boolean response = addressService.setDefaultShippingAddress(customerId, addressId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{addressId}/default/invoicing", version = "1")
    public ResponseEntity<Boolean> setDefaultInvoicingAddressV1(@AuthenticationPrincipal Jwt jwt, @PathVariable Long addressId) {
        Long customerId = Long.parseLong(jwt.getSubject());
        boolean response = addressService.setDefaultInvoicingAddress(customerId, addressId);
        return ResponseEntity.ok(response);
    }
}
