package com.example.womensafety.controllers;

import com.example.womensafety.entities.EmergencyContact;
import com.example.womensafety.entities.User;
import com.example.womensafety.repositories.EmergencyContactRepository;
import com.example.womensafety.repositories.UserRepository;
import com.example.womensafety.config.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired private EmergencyContactRepository contactRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;

    // add contact
    @PostMapping("/contacts")
    public Map<String,Object> addContact(@RequestHeader("Authorization") String auth, @RequestBody Map<String,String> body) {
        String token = auth.replace("Bearer ", "");
        DecodedJWT decoded = jwtUtil.verifyToken(token);
        Long userId = decoded.getClaim("userId").asLong();

        EmergencyContact contact = new EmergencyContact();
        contact.setName(body.get("name"));
        contact.setPhone(body.get("phone"));
        contact.setEmail(body.get("email"));
        User user = userRepo.findById(userId).orElse(null);
        contact.setUser(user);
        contactRepo.save(contact);
        Map<String,Object> r = new HashMap<>();
        r.put("success", true);
        return r;
    }

    @GetMapping("/me")
    public Map<String,Object> me(@RequestHeader("Authorization") String auth) {
        String token = auth.replace("Bearer ", "");
        DecodedJWT decoded = jwtUtil.verifyToken(token);
        Long userId = decoded.getClaim("userId").asLong();
        User user = userRepo.findById(userId).orElse(null);
        List<EmergencyContact> contacts = contactRepo.findByUserId(userId);
        Map<String,Object> r = new HashMap<>();
        r.put("user", user);
        r.put("contacts", contacts);
        return r;
    }
}
