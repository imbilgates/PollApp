package com.bilgates.pollApp.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {


    @GetMapping("/user-info")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            // Log the principal to see if the attributes are correctly populated
            System.out.println(principal.getAttributes());
            return principal.getAttributes();  // Return user attributes if authenticated
        }
        return Map.of("error", "User not authenticated"); // Return error if not authenticated
    }




}
