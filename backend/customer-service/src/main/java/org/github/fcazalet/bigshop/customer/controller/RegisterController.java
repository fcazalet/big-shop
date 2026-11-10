package org.github.fcazalet.bigshop.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @PostMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Register Successfully");
    }
}
