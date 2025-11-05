package com.example.womensafety.controllers;

import com.example.womensafety.entities.Location;
import com.example.womensafety.repositories.LocationRepository;
import com.example.womensafety.config.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired private LocationRepository locationRepo;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/update")
    public Map<String,Object> updateLocation(@RequestHeader("Authorization") String auth, @RequestBody Map<String,Object> body) {
        String token = auth.replace("Bearer ", "");
        DecodedJWT decoded = jwtUtil.verifyToken(token);
        Long userId = decoded.getClaim("userId").asLong();

        Double lat = ((Number)body.get("latitude")).doubleValue();
        Double lon = ((Number)body.get("longitude")).doubleValue();
        Boolean sharing = body.get("sharingEnabled") == null ? true : (Boolean)body.get("sharingEnabled");

        Location loc = new Location();
        loc.setUserId(userId);
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        loc.setTs(LocalDateTime.now());
        loc.setSharingEnabled(sharing);
        locationRepo.save(loc);
        Map<String,Object> r = new HashMap<>();
        r.put("success", true);
        return r;
    }

    @GetMapping("/live/{userId}")
    public Map<String,Object> getLive(@PathVariable Long userId) {
        Optional<Location> maybe = locationRepo.findTopByUserIdOrderByTsDesc(userId);
        Map<String,Object> r = new HashMap<>();
        if (maybe.isPresent()) {
            Location loc = maybe.get();
            r.put("latitude", loc.getLatitude());
            r.put("longitude", loc.getLongitude());
            r.put("ts", loc.getTs().toString());
            r.put("sharingEnabled", loc.getSharingEnabled());
        } else {
            r.put("message", "no location available");
        }
        return r;
    }
}
