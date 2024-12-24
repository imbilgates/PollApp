package com.bilgates.pollApp.Controller;

import com.bilgates.pollApp.Request.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "${FRONTEND_URL}", allowCredentials = "true")
public class UserController {


    @GetMapping("/user-info")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
            System.out.println(principal.getAttributes());
            return principal.getAttributes();
    }


    @GetMapping("/user-details")
    public UserProfile user(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(oAuth2User.getAttribute("name"));
        userProfile.setPicture(oAuth2User.getAttribute("avatar_url"));
        userProfile.setEmail(oAuth2User.getAttribute("email"));
        return userProfile;
    }

}
