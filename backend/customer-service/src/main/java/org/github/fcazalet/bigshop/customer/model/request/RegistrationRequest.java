package org.github.fcazalet.bigshop.customer.model.request;

public record RegistrationRequest(String email, String firstName, String lastName, String password) {
}
