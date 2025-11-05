package com.example.womensafety.controllers;

import com.example.womensafety.entities.User;
import com.example.womensafety.services.UserService;
import com.example.womensafety.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private UserService userService;
    @Autowired private JwtUtil jwtUtil;

    // Register -> send OTP email
    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody User user) throws Exception {
        User saved = userService.registerUser(user);
        Map<String,Object> resp = new HashMap<>();
        resp.put("message", "User registered. OTP sent to email. Verify to activate account.");
        resp.put("userId", saved.getId());
        return resp;
    }

    @PostMapping("/verify-otp")
    public Map<String,Object> verifyOtp(@RequestBody Map<String,String> body) {
        String destination = body.get("destination");
        String code = body.get("code");
        boolean ok = userService.verifyOtp(destination, code);
        Map<String,Object> resp = new HashMap<>();
        resp.put("success", ok);
        resp.put("message", ok?"Verified":"Invalid or expired OTP");
        return resp;
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> body) throws Exception {
        String emailOrPhone = body.get("emailOrPhone");
        String password = body.get("password");
        User user = userService.loginUser(emailOrPhone, password);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        Map<String,Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", user);
        return resp;
    }
}
