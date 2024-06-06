

import { toast } from 'react-toastify';
import { jwtDecode } from 'jwt-decode';
import axiosInstance from '../../services/axiosInstance';
import  {useAuth}  from '../../context/AuthContext';
import axios from 'axios';




import {
    LOGIN_REQUEST, LOGIN_SUCCESS, LOGIN_FAIL, LOGOUT,
    EMAIL_VERIFICATION_REQUEST, EMAIL_VERIFICATION_SUCCESS, EMAIL_VERIFICATION_FAILURE,
    REGISTER_REQUEST, REGISTER_SUCCESS, REGISTER_FAIL,
    RESEND_CONFIRMATION_REQUEST, RESEND_CONFIRMATION_SUCCESS, RESEND_CONFIRMATION_FAIL
} from '../types';


// Action creators
const loginRequest = () => ({
    type: LOGIN_REQUEST
});

const loginSuccess = (user, token) => ({
    type: LOGIN_SUCCESS,
    payload: { user, token }
});

const loginFail = error => ({
    type: LOGIN_FAIL,
    payload: error
});


const showToast = (type, message, onClick = null) => {
    const options = {
        position: "top-center",
        autoClose: 5000,  // Automatyczne zamykanie po 5 sekundach
        hideProgressBar: false,
        closeOnClick: true,  // Pozwól na zamykanie po kliknięciu
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
    };

    // Jeśli istnieje funkcja onClick, modyfikuj opcje
    if (onClick) {
        options.onClick = onClick;  // Dodaj funkcję onClick do opcji
        options.autoClose = false;  // Niech toast nie zamyka się automatycznie, gdy oczekujemy interakcji
    }

    // Wywołaj odpowiednią funkcję toast na podstawie typu
    toast[type](message, options);
};



export const loginUser = credentials => async dispatch => {
    console.log("Sending login credentials:", credentials);
    dispatch(loginRequest());
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        });
        const data = await response.json();
        console.log("Response status:", response.status);
        console.log("Response data:", data);

        if (response.ok) {
            const token = data.Authorization; 
            localStorage.setItem('token', token);
            localStorage.setItem('isAuthenticated', 'true');
            dispatch(loginSuccess(data.user, token)); // Przekazujemy pełne dane użytkownika o ile dostępne
            showToast('success', 'Successfully logged in!');
        } else {
            let errorMessage = data.error || 'Login failed'; // Użycie błędu serwera bezpośrednio
            if (errorMessage === "Click here to send verification email again.") {
                // Here we provide a callback function to handle click on the toast
                showToast('error', errorMessage, () => dispatch(resendConfirmationEmail(credentials.usernameOrEmail)));
            } else {
                dispatch(loginFail({ message: errorMessage }));
                showToast('error', errorMessage);
            }
        }
    } catch (error) {
        console.error("Login error:", error);
        dispatch(loginFail({ message: error.message }));
        showToast('error', 'Login error: ' + error.message);
    }
};

export const logoutUser = () => dispatch => {
    const token = localStorage.getItem('token');
    if (token) {
        console.log(`Logging out token: ${token}`);  // Logowanie informacji o tokenie
        localStorage.removeItem('token');
            dispatch({ type: 'LOGOUT_SUCCESS' });
  
        console.log('Successfully logged out');

    } else {

        console.log('No token found, possibly already logged out');
        dispatch({ type: 'LOGOUT_FAILURE', error: 'No token' });
    }
};


const registerRequest = () => ({
    type: REGISTER_REQUEST
});

const registerSuccess = user => ({
    type: REGISTER_SUCCESS,
    payload: user
});

const registerFail = error => ({
    type: REGISTER_FAIL,
    payload: error
});

export const registerUser = credentials => async dispatch => {
    dispatch(registerRequest());
    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        });
        const data = await response.json();
        if (response.ok) {
            dispatch(registerSuccess(data));
            return data;
        } else {
            dispatch(registerFail(data.message || 'Registration failed'));
            throw new Error(data.message || 'Registration failed');
        }
    } catch (error) {
        dispatch(registerFail(error.message));
        throw error;
    }
};



export const resendConfirmationEmail = usernameOrEmail => async dispatch => {
    try {
        const response = await fetch('http://localhost:8080/api/auth/resendConfirmation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ usernameOrEmail })
        });

        const responseBody = await response.text();  // Zamiast response.json();
        console.log(responseBody);

        if (response.ok) {
            toast.success('Confirmation email resent successfully.', {
                position: "top-center",
                autoClose: 1500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                onClose: () => {
                    // Tutaj zamknij modal lub przeładuj stronę
                    // closeModal(); // Jeśli masz funkcję zamykającą modal
                    window.location.reload(); // Przeładuj stronę lub użyj router.push('/home') dla React Router
                }
            });
        } else {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Failed to resend confirmation email.');
        }
    } catch (error) {
      
        toast.error('Error resending confirmation email: ' + error.message, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            onClose: () => {
             
                window.location.reload(); 
            }
        });
       
    }
};