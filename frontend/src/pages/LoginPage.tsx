import React from 'react';
import LoginForm from '../components/LoginForm';
import { useNavigate } from 'react-router-dom';

const LoginPage: React.FC = () => {
    const navigate = useNavigate();

    const handleLogin = (email: string, password: string) => {
        // Simulate login (use API call here in real apps)
        if (email === 'admin@example.com' && password === 'password') {
            navigate('/dashboard');
        } else {
            alert('Invalid credentials');
        }
    };

    return (
        <div style={{ padding: '2rem' }}>
            <LoginForm onSubmit={handleLogin} />
        </div>
    );
};

export default LoginPage;
