package org.github.fcazalet.bigshop.customer.controller;

import org.github.fcazalet.bigshop.customer.model.request.RegistrationRequest;
import org.github.fcazalet.bigshop.customer.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(path = "/register", version = "1")
    public ResponseEntity<String> registerV1(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok("Register Successfully, check your mails");
    }

    @GetMapping(path = "/{id}", version = "1")
    public ResponseEntity<String> readV1(@PathVariable String id,
                                         @AuthenticationPrincipal Jwt jwt) {
        if (!id.equals(jwt.getSubject())) {
            return ResponseEntity.notFound().build(); // do not reveal whether the id exists
        }
        return ResponseEntity.ok("Read customer information");
    }
}
