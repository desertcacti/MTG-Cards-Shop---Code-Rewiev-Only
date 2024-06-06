

import React, { useContext, useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import axios from 'axios';
import AuthContext from '../context/AuthContext';
import Modal from '../components/Modal';
import { clearCartAsync } from "../store/reducers/cartSlice";
import { toast } from 'react-toastify';
import { useRouter } from 'next/router';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Checkout = () => {
  const { isAuthenticated } = useContext(AuthContext);
  const cartItems = useSelector(state => state.cart.items);
  const customerDetails = useSelector(state => state.customer.details);
  const [isModalOpen, setModalOpen] = useState(false);
  const dispatch = useDispatch();
  const router = useRouter();

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => total + item.price * item.quantity, 0).toFixed(2);
  };

  const handleCheckout = () => {
    setModalOpen(true);
  };

  const showSuccessToast = () => {
    toast.success('Order confirmed', {
      position: "top-center",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  };



  const handleLoginConfirmPayment = async () => {
    try {
      const token = localStorage.getItem('token');  // Pobieranie tokena z localStorage
      if (!token) {
        console.log("Token nie istnieje lub wygasł.");
        return; // Jeśli token nie istnieje, przerwij funkcję
      }
  
      console.log("Token jest aktywny!");

      console.log("Wysyłany token:", token);
  
      // Endpoint dla zalogowanych użytkowników
      const endpoint = 'http://localhost:8080/api/orders/checkout';
  
      // Logowanie oryginalnych przedmiotów w koszyku
      console.log("Original cart items:", cartItems);
  
      // Mapowanie cartItems do formatu używanego przez API
      const mappedCartItems = cartItems.map(item => ({
        productId: item.productId,
        quantity: item.quantity
      }));
  
      // Logowanie mapowanych przedmiotów koszyka
      console.log("Mapped cart items:", mappedCartItems);
  
      // Przygotowanie danych zamówienia
      const orderData = {
        email: customerDetails.email,
        cartItems: mappedCartItems,
        customerDetails: {
          firstName: customerDetails.firstName,
          lastName: customerDetails.lastName,
          postalCode: customerDetails.postalCode,
          city: customerDetails.city,
          email: customerDetails.email,
          street: customerDetails.street,
          phoneNumber: customerDetails.phoneNumber
        }
      };
  
      // Logowanie danych zamówienia, które będą wysyłane
      console.log("Order data to be sent:", orderData);
  
      // Wysyłanie żądania POST z danymi zamówienia oraz nagłówkiem autoryzacji
      const response = await axios.post(endpoint, orderData, {
        headers: {
          'Authorization': token
        }
      });

     
  
      // Obsługa odpowiedzi z serwera
      if (response.data.success) {
        const orderId = response.data.orderId;
        console.log('Order processed successfully. Order ID:', orderId);
        setModalOpen(false);
        showSuccessToast();
        dispatch(clearCartAsync());
        setTimeout(() => {
          router.push('/'); // Przekierowanie na stronę główną po 5 sekundach
        }, 3000);
      } else {
        console.error('Order processing failed:', response.data.message);
      }
    } catch (error) {
      console.error('Error processing order:', error.response ? error.response.data : error);
    }
  };

  // działa w chuj

  const handleGuestConfirmPayment = async () => {
    try {
      console.log("Original cart items:", cartItems);  // Log original cart items
  
      const mappedCartItems = cartItems.map(item => ({
        productId: item.productId,
        quantity: item.quantity
      }));
  
      console.log("Mapped cart items:", mappedCartItems);  // Log mapped cart items
  
      const orderData = {
        email: customerDetails.email,
        cartItems: mappedCartItems,
        customerDetails: {
          firstName: customerDetails.firstName,
          lastName: customerDetails.lastName,
          postalCode: customerDetails.postalCode,
          city: customerDetails.city,
          email: customerDetails.email,
          street: customerDetails.street,
          phoneNumber: customerDetails.phoneNumber
        }
      };
  
      console.log("Order data to be sent:", orderData);  // Log final order data
  
      const endpoint = 'http://localhost:8080/api/orders/guest-checkout';
      const config = {
        headers: {
          'Content-Type': 'application/json'
        }
      };
  
      const response = await axios.post(endpoint, orderData, config);
      console.log("Server response:", response.data);  // Log response from the server
  
      if (response.data.success) {
        const orderId = response.data.orderId;
        console.log('Order processed successfully. Order ID:', orderId);
        setModalOpen(false);
        showSuccessToast();
        dispatch(clearCartAsync());  // Najpierw czyść koszyk
        setTimeout(() => {
            router.push('/'); // Potem przekieruj, dając więcej czasu na zakończenie operacji
        }, 3000); 
      } else {
        console.error('Order processing failed:', response.data.message);
      }
    } catch (error) {
      console.error('Error processing order:', error.response ? error.response.data : error);
    }
  };

  return (
    <div className="container mx-auto mt-10 w-3/4">
      <div className="bg-yellow-200 p-6 rounded-lg shadow-lg">
        <h1 className="text-2xl font-bold text-center mb-8 text-blue-500">Order Summary</h1>
        {cartItems.map((item) => (
          <div key={item.id} className="mb-4">
            <span>{item.quantity} x {item.name} - ${item.price.toFixed(2)}</span>
          </div>
        ))}
        <p className="text-xl font-bold my-4">Total: ${calculateTotal()}</p>
        <div className="mt-10">
          <h2 className="text-xl font-bold mb-4">Customer Details</h2>
          {customerDetails && (
            <div className="space-y-2">
              <p><strong>Name:</strong> {customerDetails.firstName} {customerDetails.lastName}</p>
              <p><strong>Email:</strong> {customerDetails.email}</p>
              <p><strong>Street:</strong> {customerDetails.street}</p>
              <p><strong>City:</strong> {customerDetails.city}</p>
              <p><strong>Postal Code:</strong> {customerDetails.postalCode}</p>
              <p><strong>Phone Number:</strong> {customerDetails.phoneNumber}</p>
            </div>
          )}
        </div>
        <div className="mt-10 flex justify-center">
          <button onClick={handleCheckout} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Proceed to Payment
          </button>
        </div>
      </div>
      <Modal isOpen={isModalOpen} closeModal={() => setModalOpen(false)}>
        <h2>Confirm Your Payment</h2>
        <p>Make sure all your details are correct before proceeding with your payment.</p>
        <div className="flex justify-around p-4">
          {isAuthenticated ? (
            <button onClick={handleLoginConfirmPayment} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
              Confirm Payment
            </button>
          ) : (
            <button onClick={handleGuestConfirmPayment} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
              Confirm Payment as Guest
            </button>
          )}
          <button onClick={() => setModalOpen(false)} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
            Cancel Payment
          </button>
        </div>
      </Modal>
      <ToastContainer />
    </div>
  );
};

export default Checkout;

































