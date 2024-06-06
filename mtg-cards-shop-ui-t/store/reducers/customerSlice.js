
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  details: {
    firstName: '',
    lastName: '',
    postalCode: '',
    city: '',
    email: '',
    deliveryMethod: '',  // Nowe pole dla metody dostawy
    paymentMethod: '',  // Nowe pole dla metody płatności
  },
};

export const customerSlice = createSlice({
  name: 'customer',
  initialState,
  reducers: {
 
    updateCustomerDetails: (state, action) => {
      state.details = { ...state.details, ...action.payload };
    },
  
    resetCustomerDetails: (state) => {
      state.details = initialState.details;
    }
  },
});

export const { updateCustomerDetails, resetCustomerDetails } = customerSlice.actions;

export default customerSlice.reducer;