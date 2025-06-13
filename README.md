# Password Manager

-------

Password manager project for learning

Generated with spring initializer

`
src/
└── main/
└── java/
└── com/yourapp/passwordmanager/
├── config/
│   ├── SecurityConfig.java                 ✅ SecurityFilterChain, AuthenticationManager
│   ├── CorsConfig.java                     ✅ CORS configuration
│   └── JwtAuthenticationFilter.java        ✅ JWT Filter
│
├── auth/
│   ├── AuthController.java                 ✅ /register, /login endpoints
│   ├── AuthService.java                    ✅ Login logic, token creation
│   ├── AuthRequest.java                    ✅ Login DTO
│   └── AuthResponse.java                   ✅ JWT response DTO
│
├── user/
│   ├── User.java                           ✅ JPA Entity
│   ├── UserRepository.java                 ✅ Spring Data repository
│   ├── UserDetailsImpl.java                ✅ Implements UserDetails
│   └── UserService.java                    ✅ Loads user from DB
│
├── jwt/
│   └── JwtService.java                     ✅ JWT utility (generate, validate, extract)
│
└── PasswordManagerApplication.java         ✅ Main class
`

User logs in ➡️ /auth/login

AuthService validates user manually

JwtService generates token

Client stores JWT (e.g. in localStorage)

➡️ User makes request with Authorization: Bearer <token>

JwtAuthenticationFilter checks JWT

If valid → sets authenticated user in SecurityContext
