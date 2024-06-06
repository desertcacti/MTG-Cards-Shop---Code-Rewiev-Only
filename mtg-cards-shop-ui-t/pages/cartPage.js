

import React, { useContext, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchCartItems, updateQuantity, removeItemFromCart } from '../store/reducers/cartSlice';
import CustomerDetailsForm from './customerDetailsForm';
import AuthContext from '../context/AuthContext'


const CartPage = () => {
  const dispatch = useDispatch();
  const { user, isAuthenticated } = useContext(AuthContext); // Korzystanie z kontekstu autentykacji
  const cartItems = useSelector(state => state.cart.items);

  useEffect(() => {
    dispatch(fetchCartItems());
  }, [dispatch]);

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => total + item.price * item.quantity, 0).toFixed(2);
  };


  return (
    <div className="container mx-auto mt-10 w-3/4">
      <h1 className="text-2xl font-bold text-center mb-8 topbar-text-yellow silkscreen-bold">Basket</h1>
      <div className="flex flex-row justify-between">
        <div className="w-3/4 pr-4">
          {cartItems.length > 0 ? (
            cartItems.map((item) => (
              <div key={item.id} className="mb-6 bg-yellow-200 shadow-lg rounded-lg p-4 flex flex-col md:flex-row items-center justify-between">
                <div className="flex items-center">
                  <img src={item.imageUrl} alt={item.name} className="h-60 w-60 mr-4 object-contain" />
                  <div>
                    <h3 className="text-xl font-semibold">{item.name}</h3>
                    <p className="text-gray-600">Price: {item.price} PLN</p>
                    <p className="text-gray-600">
                      Quantity: 
                      <input
                        type="number"
                        min="1"
                        value={item.quantity}
                        className="mx-2 w-16 p-1 border rounded"
                        onChange={(e) => dispatch(updateQuantity({ itemId: item.id, quantity: e.target.value }))}
                      />
                    </p>
                  </div>
                </div>
                <button
                  onClick={() => dispatch(removeItemFromCart(item.id))}
                  className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mt-4 md:mt-0">
                  Delete
                </button>
              </div>
            ))
          ) : (
            <p className='topbar-text-yellow silkscreen-regular'>Your basket is empty.</p>
          )}
        </div>

        <div className="w-1/4 bg-yellow-200 shadow-lg rounded-lg p-4">
          <h2 className="text-xl font-bold text-center ">Summary</h2>
          <p className="text-center font-semibold text-gray-800 text-lg mt-2">{calculateTotal()} PLN</p>
          <CustomerDetailsForm />

        </div>
      </div>
    </div>
  );
};

export default CartPage;