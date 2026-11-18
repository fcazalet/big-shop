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

    @GetMapping(path = "/me", version = "1")
    public ResponseEntity<AccountInformationResponse> readAccountV1(@AuthenticationPrincipal Jwt jwt) {
        try {
            Long lId = Long.parseLong(jwt.getSubject());
            AccountInformationResponse response = accountService.readAccount(lId);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
