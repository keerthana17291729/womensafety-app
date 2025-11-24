Women Safety App
=================

Tech stack:
- Backend: Spring Boot (Java, Maven) + Spring Data JPA + JavaMail + MySQL
- Authentication: JWT token (simple custom implementation)
- Frontend: Plain HTML/JS served from Spring Boot static resources; uses Leaflet.js for maps
- Alerts: Email alerts through Gmail SMTP (configure in application.properties). SMS/Twilio optional.
- Note: This is a minimal but working skeleton demonstrating the requested features:
  - User registration + email OTP verification
  - Login with JWT
  - Profile with emergency contacts
  - Real-time location updates from browser (via geolocation API) to server
  - SOS button that emails emergency contacts with a Google Maps link
  - Simple Leaflet map showing user's position

Quick setup (local):
1. Install Java 17+ and Maven.
2. Install MySQL and create a database (see `mysql/schema.sql`).
3. Edit `src/main/resources/application.properties` and set:
   - spring.datasource.username
   - spring.datasource.password
   - spring.mail.username (Gmail address)
   - spring.mail.password (App password for Gmail or real SMTP password)
   - jwt.secret (a random long secret)
4. From project root run:
   mvn clean package
   mvn spring-boot:run
5. Open 
 to create account, verify OTP, login, and test dashboard.

Important notes:
- For Gmail SMTP: if using a personal Gmail account, create an App Password (if 2FA enabled) or enable "Less secure apps" (not recommended).
- SMS sending is not implemented (you can integrate Twilio or other SMS provider where indicated).
- This project is intended as an educational skeleton to build upon. Security/production concerns (rate-limiting, CSRF, certificate, refresh tokens) need additional work before production.

Project files included in ZIP: backend Maven project with HTML/JS static files, SQL schema, and instructions.

