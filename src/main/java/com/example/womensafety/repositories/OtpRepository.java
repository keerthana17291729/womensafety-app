package com.example.womensafety.repositories;

import com.example.womensafety.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByDestinationAndCode(String destination, String code);
}
