package org.github.fcazalet.bigshop.customer.controller;

import org.github.fcazalet.bigshop.customer.model.request.RegistrationRequest;
import org.github.fcazalet.bigshop.customer.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok("Register Successfully, check your mails");
    }
}
