
import axios from 'axios';
import { AuthResponse, LoginCredentials } from '../types/auth';

//TODO: Should this config be handled here?
// Set base URL based on environment
const API_URL = process.env.NODE_ENV === 'production'
    ? '/api'
    : 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Add response interceptor to handle errors
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            // Handle specific status codes
            if (error.response.status === 401) {
                return Promise.reject('Invalid credentials');
            }
            return Promise.reject(error.response.data.message || 'An error occurred');
        }
        return Promise.reject('Network error. Please try again later.');
    }
);

//token handlers
export const setAuthToken = (token: string | null) => {
    if (token) {
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        localStorage.setItem('authToken', token);
    } else {
        delete api.defaults.headers.common['Authorization'];
        localStorage.removeItem('authToken');
    }
};

//initialize token if it exists
const storedToken = localStorage.getItem('authToken');
if (storedToken) {
  setAuthToken(storedToken);
}

//Login connection to back
export const login = async (credentials: LoginCredentials): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    return response.data;
};
