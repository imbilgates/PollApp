package com.bilgates.pollApp.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfile {
    private String user;
    private String picture;
    private String email;
    }
