import React, { useState } from 'react';
import LoginForm from '../components/LoginForm';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import { LoginCredentials } from '../types/auth';
import { useAuth } from '../context/AuthContext';

const LoginPage: React.FC = () => {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const { login: authLogin } = useAuth();

    const handleLogin = async (data: LoginCredentials) => {
        setIsLoading(true);
        setError(null);

        try {
            //authenticate user
            const response = await login(data);
            //store token and uptate auth state
            authLogin(response.token);
            // Redirect after successful login
            navigate('/dashboard');
        } catch (err) {
            setError(
                typeof err === 'string'
                    ? err
                    : 'Login failed. Please try again later.'
            );
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={{ padding: '2rem' }}>
            {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}
            <LoginForm onSubmit={handleLogin} isLoading={isLoading}/>
        </div>
    );
};

export default LoginPage;
