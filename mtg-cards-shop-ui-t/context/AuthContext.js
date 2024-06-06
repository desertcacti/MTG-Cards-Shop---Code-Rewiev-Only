
import React, { createContext, useContext, useState, useEffect } from 'react';
import {jwtDecode} from "jwt-decode";
import { useRouter } from 'next/router';
import { useDispatch } from 'react-redux';
import { logoutUser } from '../store/actions/authActions';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {

  const router = useRouter();
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const dispatch = useDispatch();

  const authenticateUser = (token) => {
    try {
      const decoded = jwtDecode(token);
      if (decoded.exp * 1000 > Date.now()) {
        setUser(decoded);
        setIsAuthenticated(true);
      } else {
        throw new Error('Token expired');
      }
    } catch (error) {
      console.error("Authentication error:", error.message);
      logout();
    }
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      authenticateUser(token);
    }
  }, []);

  const login = (token) => {
    localStorage.setItem('token', token);
    authenticateUser(token);
  };

  const logout = () => {
    dispatch(logoutUser());  
localStorage.removeItem('token');
setUser(null);
setIsAuthenticated(false);


  
  };


  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export default AuthContext;

