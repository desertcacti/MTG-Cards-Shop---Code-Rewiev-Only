
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useDispatch } from 'react-redux';
import { registerUser } from '../store/actions/authActions'; 

const RegisterForm = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [error, setError] = useState(null);
    const [registrationSuccess, setRegistrationSuccess] = useState(false);
    const dispatch = useDispatch();
    

    const onSubmit = async data => {
        const { username, email, password, confirmPassword } = data;
        if (password !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }
        try {
            // Wywołanie akcji rejestracji z dodanym username
            await dispatch(registerUser({ username, email, password }));
            setRegistrationSuccess(true);  // Ustawienie flagi sukcesu rejestracji
            setError(null); // Czyszczenie wszelkich błędów
        } catch (err) {
            // Ustawienie błędu, gdy rejestracja się nie powiedzie
            setError("User with this email or username is already in the system");
            setRegistrationSuccess(false); // Upewnienie się, że flaga sukcesu jest wyłączona
        }
    };


    return (
        <div className="bg-yellow-200 shadow-lg rounded-lg p-8 m-4 w-full max-w-lg">
            <h2 className="text-center text-2xl font-bold mb-6 text-blue-500">Sign up</h2>
            {error && <p className="text-red-500 text-center">{error}</p>}
            {registrationSuccess && <p className="text-green-500 text-center">A confirmation email has been sent to your address. Please confirm your account.</p>}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <input {...register('username', { required: "Username is required" })} type="text" placeholder="Username" className="w-full p-2 border border-gray-300 rounded"/>
                {errors.username && <p className="text-red-500">{errors.username.message}</p>}
                <input {...register('email', { required: "Email is required" })} type="email" placeholder="Email" className="w-full p-2 border border-gray-300 rounded"/>
                {errors.email && <p className="text-red-500">{errors.email.message}</p>}
                <input {...register('password', { required: "Password is required", minLength: { value: 6, message: "Password must be at least 6 characters long" } })} type="password" placeholder="Password" className="w-full p-2 border border-gray-300 rounded"/>
                {errors.password && <p className="text-red-500">{errors.password.message}</p>}
                <input {...register('confirmPassword', { required: "Please confirm your password" })} type="password" placeholder="Repeat password" className="w-full p-2 border border-gray-300 rounded"/>
                {errors.confirmPassword && <p className="text-red-500">{errors.confirmPassword.message}</p>}
                <button type="submit" className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    Sign up
                </button>
            </form>
        </div>
    );
};

export default RegisterForm;