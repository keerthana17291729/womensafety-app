package com.example.womensafety.services;

import com.example.womensafety.entities.User;
import com.example.womensafety.entities.Otp;
import com.example.womensafety.repositories.UserRepository;
import com.example.womensafety.repositories.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private OtpRepository otpRepository;
    @Autowired private EmailService emailService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User registerUser(User user) throws Exception {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new Exception("Email and password required");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setVerified(false);
        User saved = userRepository.save(user);
        // generate OTP and send
        String code = generateAndSendOtp(saved.getEmail());
        return saved;
    }

    public String generateAndSendOtp(String destination) {
        String code = String.format("%06d", new Random().nextInt(999999));
        Otp otp = new Otp();
        otp.setDestination(destination);
        otp.setCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otp);
        // send email
        String subject = "Your OTP for Women Safety App";
        String body = "Your OTP is: " + code + " (valid for 10 minutes)";
        try {
            emailService.sendSimpleEmail(destination, subject, body);
        } catch (Exception e) {
            // log but continue
            System.out.println("Failed to send email: " + e.getMessage());
        }
        return code;
    }

    public boolean verifyOtp(String destination, String code) {
        Optional<Otp> maybe = otpRepository.findByDestinationAndCode(destination, code);
        if (maybe.isPresent()) {
            Otp otp = maybe.get();
            if (otp.getExpiresAt().isAfter(LocalDateTime.now())) {
                // mark user verified if exists
                Optional<User> u = userRepository.findByEmail(destination);
                if (u.isPresent()) {
                    User user = u.get();
                    user.setVerified(true);
                    userRepository.save(user);
                }
                return true;
            }
        }
        return false;
    }

    public User loginUser(String emailOrPhone, String password) throws Exception {
        Optional<User> maybe = userRepository.findByEmail(emailOrPhone);
        if (maybe.isEmpty()) maybe = userRepository.findByPhone(emailOrPhone);
        if (maybe.isEmpty()) throw new Exception("User not found");
        User user = maybe.get();
        if (!encoder.matches(password, user.getPassword())) throw new Exception("Invalid credentials");
        if (!user.isVerified()) throw new Exception("Account not verified (verify OTP)");
        return user;
    }
}
