

import React, { useState } from 'react';
import { useRouter } from 'next/router';
import Modal from '../components/Modal';
import AuthModal from '../components/AuthModal';
import { useAuth } from '../context/AuthContext';
import { useDispatch, useSelector } from 'react-redux';
import { loginUser } from "../store/actions/authActions"; 
import { jwtDecode } from 'jwt-decode';
import Checkout from '../pages/checkout';
import { toast } from 'react-toastify';
import { useEffect } from 'react';
import { updateCustomerDetails } from '../store/reducers/customerSlice';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const CustomerDetailsForm = () => {
  const dispatch = useDispatch();
  const router = useRouter();
  const [showOptionsModal, setShowOptionsModal] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false);
  const [isLogin, setIsLogin] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loginError, setLoginError] = useState('');
  const [timeLeft, setTimeLeft] = useState(null);
  const cartItems = useSelector(state => state.cart.items);

  

  const handleLogin = async (credentials) => {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', credentials, {
        headers: { 'Content-Type': 'application/json' }
      });
  
      const data = response.data;
      if (data.Authorization) {
        const token = data.Authorization.split(' ')[1];
        localStorage.setItem('token', token);
        setIsAuthenticated(true);
        toast.success("Logged in successfully!");
        router.push('/checkout');
      } else {
        setLoginError(data.error);
        toast.error(`Login failed: ${data.error}`);
      }
    } catch (error) {
      console.error('Error during login:', error);
      setLoginError(error.message);
      toast.error(`Login error: ${error.message}`);
    }
  };


      
      const handleSubmit = (event) => {
        event.preventDefault(); 
      
        // Sprawdzenie, czy koszyk jest pusty
        if (!cartItems || cartItems.length === 0) {
       showEmptyCartToast();
          return; // Przerwij działanie funkcji, nie przechodź do płatności
        }
      
        const hasErrors = Object.values(errors).some(error => error !== '');
        const areFieldsEmpty = Object.values(customerDetails).some(value => value === '');
        
        if (hasErrors || areFieldsEmpty) {
          alert('Please correct the form before proceeding to payment.');
          return; // Przerwij działanie funkcji jeśli są błędy formularza
        }
      
        if (!isAuthenticated) {
          setShowOptionsModal(true);
          dispatch(updateCustomerDetails(customerDetails));
          return;
        } else {
          dispatch(updateCustomerDetails(customerDetails));
          router.push('/checkout'); 
        }
      };


      const showEmptyCartToast = () => {
        toast.error('Musisz dodać coś do koszyka, aby przejść dalej.', {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      };

  const [customerDetails, setCustomerDetails] = useState({
    firstName: '',
    lastName: '',
    postalCode: '',
    street: '',
    city: '',
    phoneNumber: '',
    email: '',
    delivery: 'personalPickup',
    payment: 'fastTransfer',
  });
  const [errors, setErrors] = useState({});


  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        console.log("Dekodowanie tokena...");
        const decoded = jwtDecode(token); // Użycie biblioteki jwt-decode
        console.log("Token zdekodowany, email użytkownika:", decoded.sub);
  
        const currentTime = Date.now() / 1000; // Pobierz czas w sekundach
        if (decoded.exp > currentTime) {
          setIsAuthenticated(true);
          setCustomerDetails(prevDetails => ({
            ...prevDetails,
            email: decoded.sub // Ustaw email z tokena
          }));
        } else {
          localStorage.removeItem('token');
          setIsAuthenticated(false);
          setCustomerDetails(prevDetails => ({
            ...prevDetails,
            email: '' // Usuń email z formularza
          }));
        }
      } catch (error) {
        console.error("Błąd dekodowania tokena:", error);
        localStorage.removeItem('token');
        setIsAuthenticated(false);
      }
    }
  }, [isAuthenticated]);





  const validateField = (name, value) => {
    let message = '';
    switch (name) {
      case 'firstName':
        if (!value || value.length < 2) message = 'The name must contain at least 2 characters.';
        break;
      case 'lastName':
        if (!value || value.length < 3) message = 'The name must contain at least 3 characters.';
        break;
      // case 'postalCode':
      //   if (!/^\d{2}-\d{3}$/.test(value)) message = 'The postal code must be in the format XX-XXX.';
      //   break;
      case 'postalCode':
      if (!/^\d{2}-\d{3}$/.test(value)) {
        message = 'The postal code must be in the format XX-XXX.';
      }
      break;
      case 'city':
        if (!value || value.length < 3) message = 'The city must contain at least 3 characters.';
        break;
      case 'email':
        if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(value)) message = 'Invalid email address format.';
        break;
      case 'phoneNumber':
        // Tylko cyfry, ignorując myślniki
        let numericValue = value.replace(/-/g, ''); // Usuń myślniki do walidacji
        if (numericValue.length !== 9) message = 'The phone number must contain exactly 9 digits.';
        break;
      case 'street':
        if (!value || value.length < 3) message = 'The street must contain at least 3 characters.';
        break;
    }
    // Update errors state
    setErrors(prevErrors => ({
      ...prevErrors,
      [name]: message
    }));
  };





  const handleChange = (event) => {
    const { name, value } = event.target;
    let newValue = value;
  
    if (name === 'phoneNumber') {
      let processedValue = value.replace(/\D/g, ''); // Usuń wszystko oprócz cyfr
      if (processedValue.length > 9) {
        processedValue = processedValue.slice(0, 9); // Ogranicz do 9 cyfr
      }
      processedValue = processedValue
        .match(/.{1,3}/g)?.join('-').substr(0, 11) || '';
      newValue = processedValue;
    } else if (name === 'postalCode') {
      let processedValue = value.replace(/\D/g, ''); // Usuń wszystko oprócz cyfr
      if (processedValue.length > 5) {
        processedValue = processedValue.slice(0, 5); // Ogranicz do 5 cyfr
      }
      // Dodaj myślnik po dwóch cyfrach, jeśli jest więcej niż dwie cyfry
      if (processedValue.length > 2) {
        processedValue = `${processedValue.slice(0, 2)}-${processedValue.slice(2)}`;
      }
      newValue = processedValue;
    }
  
    setCustomerDetails(prevDetails => ({
      ...prevDetails,
      [name]: newValue
    }));
  
    validateField(name, newValue);
  };
  


  const openAuthModal = () => {
    setShowOptionsModal(false);
    setModalOpen(true);
    setIsLogin(true);
  };

  const switchAuthMode = () => {
    setIsLogin(!isLogin);
  };

  const handleContinueWithoutLogin = () => {
    setShowOptionsModal(false);

    router.push('/checkout');
  };
  
  const handleCloseModal = () => {
    setModalOpen(false); 
  };
  

  const handleOpenLoginModal = () => {
    setShowOptionsModal(false); // Zamknij modal opcji
    setModalOpen(true); // Otwórz modal logowania
    setIsLogin(true); // Ustaw stan logowania na 'true'
  };



  return (
    <>
      <form onSubmit={handleSubmit} method="POST" className="space-y-4">
        <h2 className="font-bold text-lg">Personal Data</h2>
        {['firstName', 'lastName', 'street', 'postalCode', 'city', 'phoneNumber', 'email'].map(field => (
          <div key={field}>
            <label>
              {field === 'firstName' ? 'Name' :
               field === 'lastName' ? 'Surname' :
               field === 'phoneNumber' ? 'Phone number' :
               field === 'street' ? 'Street' :
               field === 'email' ? 'Email' :
               field[0].toUpperCase() + field.slice(1)}:
            </label>
            <input
              type={field === 'email' ? 'email' : 'text'}
              name={field}
              value={customerDetails[field] || ''}
              onChange={handleChange}
              readOnly={field === 'email' && isAuthenticated}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder=''  // Set placeholder to an empty string for all fields
            />
            {errors[field] && <div className="text-red-500">{errors[field]}</div>}
          </div>
        ))}
        <div>
          <p className="font-bold text-lg mt-4 mb-2">Delivery:</p>
          {['personalPickup', 'dhl'].map(deliveryOption => (
            <label className="block mb-2 py-1" key={deliveryOption}>
              <input
                type="radio"
                name="delivery"
                value={deliveryOption}
                checked={customerDetails.delivery === deliveryOption}
                onChange={handleChange}
              />
              <span className="ml-1">{deliveryOption === 'personalPickup' ? 'Personal pickup' : 'DHL courier'}</span>
            </label>
          ))}
        </div>
        <div>
          <p className="font-bold text-lg mb-2">Payment option:</p>
          <label className="block mb-6">
            <input
              type="radio"
              name="payment"
              value="fastTransfer"
              checked={customerDetails.payment === 'fastTransfer'}
              onChange={handleChange}
            />
            <span className="ml-1">Fast Transfer</span>
          </label>
        </div>
        <button
          type="submit"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Proceed to Payment
        </button>
      </form>
  
      <ToastContainer position="top-center" autoClose={5000} hideProgressBar={false} newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover />
  
      {showOptionsModal && (
        <Modal isOpen={showOptionsModal} closeModal={handleCloseModal}>
          <div className="p-4 text-center">
            <h2>Continue as a Guest or Log In</h2>
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded m-2"
              onClick={handleOpenLoginModal}
            >
              Log In
            </button>
            <button
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded m-2"
              onClick={handleContinueWithoutLogin}
            >
              Continue without login
            </button>
          </div>
        </Modal>
      )}
      {isModalOpen && (
        <AuthModal
          isModalOpen={isModalOpen}
          setModalOpen={setModalOpen}
          isLogin={isLogin}
          switchAuthMode={switchAuthMode}
          onLoginSuccess={handleLogin}
          closeModal={handleCloseModal}
        />
      )}
    </>
    
  );
};

export default CustomerDetailsForm;

