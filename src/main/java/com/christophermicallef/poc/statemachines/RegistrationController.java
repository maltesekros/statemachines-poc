package com.christophermicallef.poc.statemachines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/register")
    public boolean register(@RequestParam String name) throws Exception {
        return registrationService.saveCorrectCustomerDetails(name);
    }

    @GetMapping("/confirm")
    public boolean confirm(@RequestParam String name) throws Exception {
        return registrationService.receiveEmailConfirmation(name);
    }
}
