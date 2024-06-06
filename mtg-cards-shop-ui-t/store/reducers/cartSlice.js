

import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

// Asynchroniczne akcje
export const fetchCartItems = createAsyncThunk('cart/fetchCartItems', async () => {
  const response = await fetch('http://localhost:8080/api/cart');
  if (!response.ok) {
    throw new Error('Failed to fetch cart items');
  }
  const data = await response.json();
  return data.map(item => ({
    id: item.id, // id koszyka, jeśli potrzebne
    productId: item.productId, // zakładamy, że backend zwraca productId
    name: item.name,
    price: item.price,
    quantity: item.quantity,
    imageUrl: item.imageUrl
  }));
});


//stare działające
export const updateQuantity = createAsyncThunk('cart/updateQuantity', async ({ itemId, quantity }) => {
  const response = await fetch(`http://localhost:8080/api/cart/${itemId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ quantity: parseInt(quantity, 10) }),
  });
  if (!response.ok) {
    throw new Error('Failed to update item quantity');
  }
  return { itemId, quantity: parseInt(quantity, 10) };
});



export const removeItemFromCart = createAsyncThunk('cart/removeItemFromCart', async (productId) => {
  const response = await fetch(`http://localhost:8080/api/cart/${productId}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('Failed to remove item from cart');
  }
  return productId;
});


export const clearCartAsync = createAsyncThunk('cart/clearCart', async () => {
  const response = await fetch('http://localhost:8080/api/cart/clear', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    }
  });
  if (!response.ok) {
    throw new Error('Failed to clear the cart');
  }
  return [];
});


const initialState = {
  items: [],
  status: 'idle', // 'idle', 'loading', 'succeeded', 'failed'
  error: null
};

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    clearCart: (state) => {
      state.items = [];
    }
  },

extraReducers: (builder) => {
  builder
    .addCase(fetchCartItems.pending, (state) => {
      state.status = 'loading';
    })
    .addCase(fetchCartItems.fulfilled, (state, action) => {
      state.items = action.payload;
      state.status = 'succeeded';
    })
    .addCase(fetchCartItems.rejected, (state, action) => {
      state.status = 'failed';
      state.error = action.error.message;
    })

//stare działające

.addCase(updateQuantity.fulfilled, (state, action) => {
          const index = state.items.findIndex(item => item.id === action.payload.itemId);
          if (index !== -1) {
            state.items[index].quantity = action.payload.quantity;
          }
        })




    .addCase(removeItemFromCart.fulfilled, (state, action) => {
      state.items = state.items.filter(item => item.id !== action.payload);
    })
    .addMatcher(action => action.type.endsWith('/rejected'), (state, action) => {
      state.status = 'failed';
      state.error = action.error.message;
    });
},
});

export const { clearCart } = cartSlice.actions;
export default cartSlice.reducer;



