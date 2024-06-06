

import { configureStore } from '@reduxjs/toolkit';
import cartReducer from '../store/reducers/cartSlice';
import customerReducer from '../store/reducers/customerSlice';
import authReducer from '../store/reducers/authReducer'; 
import userReducer from '../store/reducers/userSlice';
import orderReducer from './reducers/orderReducer';

export const store = configureStore({
  reducer: {
    cart: cartReducer,
    customer: customerReducer,
    auth: authReducer,
    user: userReducer,
    order: orderReducer
  },
});