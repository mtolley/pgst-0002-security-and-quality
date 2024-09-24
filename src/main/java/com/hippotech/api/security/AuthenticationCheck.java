package com.hippotech.api.security;

public class AuthenticationCheck {
    public static boolean checkJWTToken(String request) {
        System.out.println("Checking JWT Token");
        if (request == null || !request.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }  
}
