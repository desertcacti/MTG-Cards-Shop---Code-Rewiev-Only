

import React from 'react';

const Modal = ({ children, isOpen, closeModal }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex justify-center items-center">
            <div className="bg-beige p-8 rounded-lg shadow-lg max-w-lg w-full min-w-md min-h-full overflow-auto relative">
                {children}
                {/* Przycisk zamknięcia z ulepszonymi stylami */}
                <button 
                    onClick={closeModal} 
                    className="absolute top-2 right-2 text-2xl font-bold text-white hover:text-gray-300 focus:outline-none"
                    aria-label="Zamknij modal"
                    style={{ cursor: 'pointer' }}
                >
                    &times;
                </button>
            </div>
        </div>
    );
};

export default Modal;