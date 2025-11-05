package com.example.womensafety.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "location")
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime ts;
    private Boolean sharingEnabled = true;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getTs() { return ts; }
    public void setTs(LocalDateTime ts) { this.ts = ts; }

    public Boolean getSharingEnabled() { return sharingEnabled; }
    public void setSharingEnabled(Boolean sharingEnabled) { this.sharingEnabled = sharingEnabled; }
}
