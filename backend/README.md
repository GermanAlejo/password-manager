# Password Manager

-------

Password manager project for learning

Backend folder

Generated with spring initializer

```
src/
└── main/
└── java/
└── com/passwordmanager/password_manager
├── config/
│   ├── SecurityConfig.java                     SecurityFilterChain, AuthenticationManager
│   ├── CorsConfig.java                         CORS configuration
│   └── JwtAuthenticationFilter.java            JWT Filter
│
├── controller/
│   ├── AuthController.java                     /register, /login endpoints
│   ├── PasswordEntryController.java            Create, retrieve, delete, ... Entries
│   └── ValidationHandler.java               
│
├── dto/
│   ├── AuthResponseDTO.java                    DTO Object representing registration responses
│   ├── LoginRequestDTO.java                    DTO Object representing login/registration requests
│   ├── LoginResponseDTO.java                   DTO Object representing login response
│   └── PasswordEntryDTO.java                   DTO Object representing request/responses from entries
│   
│   ├── EncryptionException.java                  
│   ├── IllegalPasswordEntryException.java        
│   ├── InvalidArgumentsException.java                 
│   ├── PasswordEntryNotFoundException          
│   ├── UserNotFoundException
│    └── UserAlreadyRegisteredException.java     
│
├── repository/                                 MongoDB Data Repository
│   ├── PasswordRepository.java              
│   └── UserRepository.java    
│             
├── model/
│   ├── User.java                               Mongo Document Entity
│   └── PasswordEntry.java                   

├── security/
│   ├── JwtService.java                         JWT utility (generate, validate, extract)
│   ├── UserDetailsImpl.java                    Implements Srping interface UserDetails
│   └── EncryptionService.java                  Service layer with encrypt/decrypt/hashing methods
│   
│   
└── PasswordManagerApplication.java             Main class

```

User logs in ➡️ /auth/login

AuthService validates user manually

JwtService generates token

Client stores JWT (e.g. in localStorage)

➡️ User makes request with Authorization: Bearer <token>

JwtAuthenticationFilter checks JWT

If valid → sets authenticated user in SecurityContext

### EncryptionService.java

This class holds methods for encryption, decryption and hashing passwords

In our logic we will be hashing login passwords, and encrypting passwords from entries
