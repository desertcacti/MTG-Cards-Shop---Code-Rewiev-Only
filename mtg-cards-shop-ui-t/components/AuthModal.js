
import React from 'react';
import Modal from '../components/Modal';
import LoginForm from '../components/LoginForm';
import RegisterForm from '../components/RegisterForm';

const AuthModal = ({ isModalOpen, setModalOpen, isLogin, switchAuthMode, handleLogin }) => {

  
  const renderContent = () => {
    if (isLogin) {
      return (
        <>
          <LoginForm handleLogin={handleLogin} />
          <div className="font-bold text-center mt-4">
            <span className="text-yellow-500">Don't have an account? </span>
            <button onClick={switchAuthMode} className="text-blue-500 hover:underline">
              Register here
            </button>
          </div>
        </>
      );
    } else {
      return (
        <>
          <RegisterForm handleLogin={handleLogin} />
          <div className="font-bold text-center mt-4">
            <span className="text-yellow-500">Already have an account? </span>
            <button onClick={switchAuthMode} className="text-blue-500 hover:underline">
              Log In
            </button>
          </div>
        </>
      );
    }
  };

  return (
    <Modal isOpen={isModalOpen} closeModal={() => setModalOpen(false)}>
      {renderContent()}
    </Modal>
  );
};

export default AuthModal;


