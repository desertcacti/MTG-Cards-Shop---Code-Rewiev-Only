
import React, { useState, useEffect, useRef } from 'react';
import { useRouter } from 'next/router';
import Image from "next/image";
import { MdAccountBox } from "react-icons/md";
import { FaBasketShopping } from "react-icons/fa6";
import AuthModal from "../components/AuthModal";
import { jwtDecode } from "jwt-decode";
import { useAuth } from '../context/AuthContext';
import { useSelector, useDispatch } from 'react-redux';
import { logoutUser } from '../store/actions/authActions';
import { set } from 'react-hook-form';
import OrderPage from '../pages/orderPage';



const Header = () => {
  const router = useRouter();
  const { logout } = useAuth();
  const dispatch = useDispatch();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [timeLeft, setTimeLeft] = useState(null);
  const profileMenuRef = useRef(null);
  const [isModalOpen, setModalOpen] = React.useState(false);
  const [isLogin, setIsLogin] = React.useState(true);
  const [showProfileMenu, setShowProfileMenu] = React.useState(false);
  const hideMenuTimeoutRef = useRef(null);

  const logoUrl = "http://localhost:8080/api/media/LOGO.png";

  

  const handleNavigate = (path) => {
    router.push(path);
  };

const handleLogout = () => {
  logout();  
  window.location.href = "/";
};

  const toggleModal = () => setModalOpen(!isModalOpen);
  const switchAuthMode = () => setIsLogin(!isLogin);

  const showProfileMenuWithDelay = () => {
    if (hideMenuTimeoutRef.current) {
      clearTimeout(hideMenuTimeoutRef.current);
    }
    setShowProfileMenu(true);
  };

  const hideProfileMenuWithDelay = () => {
    hideMenuTimeoutRef.current = setTimeout(() => {
      setShowProfileMenu(false);
    }, 80);
  };


  useEffect(() => {
        const checkAuthentication = () => {
          const token = localStorage.getItem('token');
          if (token) {
            try {
              console.log("Dekodowanie tokena...");
              const decoded = jwtDecode(token);
              console.log("Token zdekodowany:", decoded);
              const expirationTime = decoded.exp * 1000;
              const currentTime = Date.now();
              if (expirationTime > currentTime) {
                setIsAuthenticated(true);
                setTimeLeft(expirationTime - currentTime);
                  console.log("poprawnie zaologowano użytkownika" + token)

              } else {
                localStorage.removeItem('token');
                setIsAuthenticated(false);
                setModalOpen(true);
              }
            } catch (error) {
              console.error("Błąd dekodowania tokena:", error);
              localStorage.removeItem('token');
              setIsAuthenticated(false);
            }
          } else {
            setIsAuthenticated(false);
          }
        };
    
        checkAuthentication();
      }, [isAuthenticated ]);
    
  return (

    
    <>
  
    
      <div className="topbar-bg-navy">
        <div className="flex pl-40 items-center">
          <Image src={logoUrl} alt="Topbar Logo" height={100} width={100} />

          {/* <style>
            @import url('https://fonts.googleapis.com/css2?family=Gloria+Hallelujah&family=Silkscreen:wght@400;700&display=swap');
            </style>  */}

          <p className="topbar-text-yellow   font-bold silkscreen-bold  ml-10 text-xl ">
            Magic: The Gathering Cards Shop
          </p>
        </div>
      </div>

      

      <div className="sticky top-0 navbar-bg-yellow z-10  custom-border border-navy ">
        <div className="h-10 pl-1 flex items-center shadow-yellow-800 shadow-lg">
          <p className="w-30 ml-40 mr-12 silkscreen-regular navbar-text-navy font-bold cursor-pointer" onClick={() => handleNavigate('/')}>Home Page</p>


          <p className="w-15 mr-14 font-bold silkscreen-regular text-red-600  cursor-pointer" onClick={() => handleNavigate('/cards/red')}>Red</p>
         <p className="w-15 mr-14 font-bold silkscreen-regular text-blue-600 cursor-pointer" onClick={() => handleNavigate('/cards/blue')}>Blue</p>
          <p className="w-15 mr-14 font-bold silkscreen-regular text-green-600 cursor-pointer" onClick={() => handleNavigate('/cards/green')}>Green</p>
        <p className="w-15 mr-14 font-bold silkscreen-regular text-white cursor-pointer " onClick={() => handleNavigate('/cards/white')}>White</p>
       <p className="w-15 silkscreen-regular navbar-text-navy font-bold cursor-pointer " onClick={() => handleNavigate('/cards/black')}>Black</p>

          <div className="flex items-center" style={{ position: 'absolute', right: '10%' }}>
            {isAuthenticated && (
              <span className="text-green-500 mr-2">Logged In</span>
            )}
            <FaBasketShopping className="text-2xl cursor-pointer mr-4" onClick={() => handleNavigate('/cartPage')} />
            <div className="relative" onMouseEnter={showProfileMenuWithDelay} onMouseLeave={hideProfileMenuWithDelay}>
              <MdAccountBox className="text-2xl mr-1 cursor-pointer" onClick={isAuthenticated ? () => setShowProfileMenu(prevState => !prevState) : toggleModal} />
              {showProfileMenu && isAuthenticated && (
                <div className="absolute mt-2 bg-white shadow-lg rounded p-2 w-48" onMouseEnter={showProfileMenuWithDelay} onMouseLeave={hideProfileMenuWithDelay}>
                  {/* <p className="p-2 hover:bg-gray-100 cursor-pointer" onClick={() => handleNavigate('/profile')}>Moje dane</p> */}
                  <p className="p-2 hover:bg-gray-100 cursor-pointer" onClick={() => handleNavigate('/orderPage')}>Moje zamówienia</p>
                  <p className="p-2 hover:bg-gray-100 cursor-pointer" onClick={handleLogout}>Wyloguj</p>
                </div>
              )}
            </div>
            <AuthModal isModalOpen={isModalOpen} setModalOpen={setModalOpen} isLogin={isLogin} switchAuthMode={switchAuthMode} />
          </div>

        </div>
      </div>
    </>
  );
};

export default Header;



