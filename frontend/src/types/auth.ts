export interface LoginCredentials {
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    //TODO: add expiration time here?
}
