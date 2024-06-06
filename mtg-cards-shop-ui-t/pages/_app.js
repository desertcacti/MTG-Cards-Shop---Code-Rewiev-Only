

import "../styles/globals.css";
import React from 'react';
import Header from "../components/Header";
import { Provider as ReduxProvider } from 'react-redux';
import { store } from '../store/store';
import { AuthProvider } from '../context/AuthContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function MyApp({ Component, pageProps }) {
  return (
    <ReduxProvider store={store}>
      <AuthProvider>
        <div className="app-container">
          <Header />
          <div className="main-content topbar-bg-navy">
            <Component {...pageProps} />
          </div>
          <ToastContainer />
        </div>
      </AuthProvider>
    </ReduxProvider>
  );
}

export default MyApp;