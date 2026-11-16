package org.github.fcazalet.bigshop.customer.controller;

import org.github.fcazalet.bigshop.customer.model.response.AccountInformationResponse;
import org.github.fcazalet.bigshop.customer.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/{id}", version = "1")
    public ResponseEntity<AccountInformationResponse> readAccountV1(@PathVariable String id,
                                                                    @AuthenticationPrincipal Jwt jwt) {
        if (!id.equals(jwt.getSubject())) {
            return ResponseEntity.notFound().build(); // do not reveal whether the id exists
        }
        try {
            Long lId = Long.parseLong(id);
            AccountInformationResponse response = accountService.readAccount(lId);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
