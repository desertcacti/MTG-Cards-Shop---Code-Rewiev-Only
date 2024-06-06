

import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useForm } from 'react-hook-form';
import { loginUser, resendConfirmationEmail } from '../store/actions/authActions'; // Załóżmy, że masz taką akcję

const LoginForm = () => {
    const { register, handleSubmit, formState: { errors }, watch } = useForm();
    const dispatch = useDispatch();
    const isAuthenticated = useSelector(state => state.auth.isAuthenticated);
    const loading = useSelector(state => state.auth.loading);
    const loginError = useSelector(state => state.auth.error);

    // Obserwowanie wartości pola usernameOrEmail
    const usernameOrEmail = watch('usernameOrEmail');

    useEffect(() => {
        if (isAuthenticated) {
            window.location.href = '/'; // Przekierowanie na stronę główną po udanym logowaniu
        }
    }, [isAuthenticated]);

    const onSubmit = data => {
        dispatch(loginUser(data)); // Wywołujemy akcję logowania
    };

    const handleResendClick = (event) => {
        event.preventDefault();  // Zapobieganie przeładowaniu strony
        dispatch(resendConfirmationEmail(usernameOrEmail));
          // Przekazanie obecnego stanu usernameOrEmail
    };

    return (
        <div className="bg-yellow-200 shadow-lg rounded-lg p-8 m-4 w-full max-w-lg">
            <h2 className="text-center text-2xl font-bold mb-6 text-blue-500 ">Sign in</h2>
            {loginError && (
                <div className="text-red-500 text-center">
                    <p>{loginError.message}</p>
                    {loginError.accountNotConfirmed && (
                        <p>
                            {/* Zaktualizowane przekazywanie eventu do funkcji */}
                            <a href="#" onClick={handleResendClick} className="text-blue-500 hover:text-blue-700">
                                Wyślij ponownie
                            </a>
                        </p>
                    )}
                </div>
            )}
            
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <input {...register('usernameOrEmail', { required: "Username or Email is required" })} type="text" placeholder="Username/Email" className="w-full p-2 border border-gray-300 rounded"/>
                {errors.usernameOrEmail && <p className="text-red-500">{errors.usernameOrEmail.message}</p>}
                <input {...register('password', { required: true })} type="password" placeholder="Password" className="w-full p-2 border border-gray-300 rounded"/>
                {errors.password && <p className="text-red-500">Password is required.</p>}
                <button type="submit" className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" disabled={loading}>
                    Sign in
                </button>
            </form>
        </div>
    );
};

export default LoginForm;