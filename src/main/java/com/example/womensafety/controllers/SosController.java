package com.example.womensafety.controllers;

import com.example.womensafety.repositories.EmergencyContactRepository;
import com.example.womensafety.repositories.UserRepository;
import com.example.womensafety.repositories.LocationRepository;
import com.example.womensafety.entities.User;
import com.example.womensafety.entities.EmergencyContact;
import com.example.womensafety.entities.Location;
import com.example.womensafety.services.EmailService;
import com.example.womensafety.config.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/sos")
public class SosController {
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmergencyContactRepository contactRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private LocationRepository locationRepo;
    @Autowired private EmailService emailService;

    @PostMapping("/trigger")
    public Map<String,Object> triggerSos(@RequestHeader("Authorization") String auth, @RequestBody(required=false) Map<String,Object> body) {
        String token = auth.replace("Bearer ", "");
        DecodedJWT decoded = jwtUtil.verifyToken(token);
        Long userId = decoded.getClaim("userId").asLong();
        User user = userRepo.findById(userId).orElse(null);

        Optional<Location> maybe = locationRepo.findTopByUserIdOrderByTsDesc(userId);
        String mapLink = "https://www.google.com/maps/search/?api=1&query=";
        if (maybe.isPresent()) {
            Location loc = maybe.get();
            mapLink += loc.getLatitude() + "," + loc.getLongitude();
        } else {
            mapLink += ""; // no location
        }

        List<EmergencyContact> contacts = contactRepo.findByUserId(userId);
        for (EmergencyContact c : contacts) {
            // send email alert
            String to = c.getEmail();
            if (to == null || to.isEmpty()) continue;
            String subject = "SOS Alert from " + (user != null ? user.getName() : "Unknown user");
            String bodyText = "ALERT: " + (user != null ? user.getName() : "Someone") + " pressed SOS.\nLocation: " + mapLink + "\nPlease act immediately.";
            try {
                emailService.sendSimpleEmail(to, subject, bodyText);
            } catch (Exception e) {
                System.out.println("Failed to send SOS email to " + to + ": " + e.getMessage());
            }
        }

        Map<String,Object> r = new HashMap<>();
        r.put("success", true);
        r.put("contactsNotifyCount", contacts.size());
        r.put("mapLink", mapLink);
        return r;
    }
}
