
import { createAsyncThunk } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';


export const fetchOrders = createAsyncThunk(
    'orders/fetchOrders',
    async (_, { getState, rejectWithValue }) => {
        const token = localStorage.getItem('token');
        if (!token) {
            console.log("No token available in localStorage.");
            return rejectWithValue('Authentication token is not available.');
        } else {
            console.log("Token found: ", token);
        }

        try {
            console.log("Sending request to fetch orders.");
            const response = await fetch(`http://localhost:8080/api/orders`, {
                headers: {
                    'Authorization': `${token}`,

                }
            });

            console.log("Response status:", response.status);

            // Check if the response has content before parsing JSON
            if (response.status === 204) {
                console.log("No content to fetch.");
                return [];
            }

            const data = await response.json();
            if (!response.ok) {
                console.error("Failed to fetch orders, server returned an error: ", data);
                throw new Error(data.message || 'Unable to fetch orders');
            }

            console.log("Orders fetched successfully:", data);
            return data;
        } catch (error) {
            console.error("Error fetching orders:", error);
            return rejectWithValue(error.message);
        }
    }
);