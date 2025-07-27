import React, { useState } from "react";
import { LoginCredentials } from "../types/auth";

interface LoginFormProps {
    onSubmit: (loginData: LoginCredentials) => void;
    isLoading: boolean;
}

const LoginForm: React.FC<LoginFormProps> = ({ onSubmit, isLoading }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const data: LoginCredentials = {
            email: email,
            password: password
        }
        onSubmit(data);
    };

    return (
        <div style={{ backgroundColor: "white", padding: '2rem' }}>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '1rem' }}>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        required
                        onChange={(e) => setEmail(e.target.value)}
                        disabled={isLoading}
                    />
                </div>
                <div style={{ marginBottom: '1rem' }}>
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        required
                        onChange={(e) => setPassword(e.target.value)}
                        disabled={isLoading}
                    />
                </div>
                <button
                    disabled={isLoading}
                    style={{ opacity: isLoading ? 0.7 : 1 }} // Visual feedback
                    type="submit">
                    {isLoading ? 'Logging in...' : 'Login'} 
                </button>
            </form>
        </div>
    );
};

export default LoginForm;
