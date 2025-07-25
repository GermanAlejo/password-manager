PASSWORD MANAGER APP

Spring & MongoDB

```
src/
  ├── main/
  │   ├── java/
  │   │   └── com/passwordmanager/
  │   │       ├── config/        Security & App Configs
  │   │       ├── controller/    REST APIs
  │   │       ├── dto/           Data Transfer Objects
  │   │       ├── model/         JPA Entities
  │   │       ├── repository/    JPA Repositories
  │   │       ├── service/       Business Logic
  │   │       └── security/      Auth/Crypto
  │   └── resources/
  │       └── application.properties
  └── test/       → Unit/Integration Tests
```
1. Configuration Files
application.properties: MongoDB connection settings

SecurityConfig.java: Spring Security configuration

JwtConfig.java: JWT properties (secret, expiration)

2. MongoDB Document Models
User.java
Fields: id, username, email, masterPasswordHash, createdAt

PasswordEntry.java
Fields: id, userId, website, username, encryptedPassword, iv, notes, createdAt

3. Repository Interfaces
UserRepository.java: MongoDB repository for User

PasswordEntryRepository.java: MongoDB repository for PasswordEntry

4. Security Components
JwtService.java: JWT generation/validation

JwtAuthenticationFilter.java: JWT authentication filter

AuthenticationProvider.java: Custom authentication provider

5. Encryption Service
CryptoService.java:
Methods: generateIV(), deriveKey(), encrypt(), decrypt()

6. Business Logic Services
UserService.java:
Methods: registerUser(), authenticate()

PasswordEntryService.java:
Methods: createEntry(), getUserEntries(), updateEntry(), deleteEntry()

7. DTOs (Data Transfer Objects)
UserRegistrationDto.java: Registration request

LoginRequestDto.java: Login request

LoginResponseDto.java: JWT + user data

PasswordEntryDto.java: Password entry request/response

8. REST Controllers
AuthController.java:
Endpoints: /register, /login

PasswordEntryController.java:
Endpoints: /passwords (GET, POST, PUT, DELETE)

9. Utility Classes
ModelMapperConfig.java: DTO ↔ Entity mapping

GlobalExceptionHandler.java: Error handling

SecurityUtil.java: Password strength validation

