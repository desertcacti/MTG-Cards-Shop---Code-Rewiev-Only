

import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';
import { fetchOrders } from '../store/actions/orderActions';
import { useAuth } from '../context/AuthContext';

const OrdersPage = () => {
    const dispatch = useDispatch();
    const router = useRouter();
    const { orders, error } = useSelector(state => state.order);
    const { isAuthenticated } = useAuth();

    useEffect(() => {
        if (!isAuthenticated) {
            router.push('/');
        } else {
            dispatch(fetchOrders());
        }
    }, [dispatch, isAuthenticated, router]);

    if (error) {
        return <p>Error loading orders: {error}</p>;
    }

    console.log('Orders:', orders);
console.log('Error:', error);
return (
    <div className="container mx-auto mt-10 w-3/4">
        <h1 className="text-3xl font-bold text-center mb-8 topbar-text-yellow   ">My Orders</h1>
        {orders.length > 0 ? (
            orders.map((order, index) => (


                <div key={index} className="mb-6 bg-yellow-200 shadow-lg rounded-lg p-4 flex flex-col md:flex-row justify-between  items-start ">


                    <div className="md:w-3/4 md:pl-40 p-4 flex flex-col">

                        <h3 className="pl-10 text-xl font-semibold mb-4 ">Order Data: </h3>
                        
                        <div className="flex flex-col space-y-2">
                        <p><strong>Personal:</strong> {order.customerDetails.firstName} {order.customerDetails.lastName} </p>
                            <p><strong>Email:</strong> {order.email}</p>
                            <p><strong>City:</strong> {order.customerDetails.city}</p>
                            <p><strong>Postal Code:</strong> {order.customerDetails.postalCode}</p>
                        </div>
                    </div>


                    <div className="md:w-1/2 p-4">
                        <h4 className="pl-10 text-xl font-semibold mb-2">Cart Items:</h4>
                        <ul>
                            {order.cartItems.map((item, idx) => (
                                <li key={idx}>
                                    <p>{item.quantity} x {item.name} - {item.price.toFixed(2)} PLN</p>
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>

            ))
        ) : (
            <p className="text-center">You have no orders.</p>
        )}
    </div>
);
};

export default OrdersPage;

