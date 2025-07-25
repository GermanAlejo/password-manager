import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';

const App: React.FC = () => {
  return (
      <Routes>
        <Route path='/' element={<LoginPage />} />
      </Routes>
  );
};


export default App;
