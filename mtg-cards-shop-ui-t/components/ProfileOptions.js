

import React, { useState } from 'react';
import { useRouter } from 'next/router';
import { MdAccountCircle, MdExitToApp, MdList } from "react-icons/md";
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '../redux/actions/authActions'; // Assuming there is a logout action

const ProfileOption = () => {
  const router = useRouter();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector(state => state.auth.isAuthenticated); // Assuming state shape
  const [showOptions, setShowOptions] = useState(false);

  const handleNavigate = (path) => {
    router.push(path);
  };

  const handleLogout = () => {
    dispatch(logout()); // Dispatch logout action
    handleNavigate('/'); // Redirect to home after logout
  };

  return (
    <div className="relative">
      <MdAccountCircle
        className="text-3xl cursor-pointer"
        onMouseEnter={() => setShowOptions(true)}
        onMouseLeave={() => setShowOptions(false)}
      />
      {isAuthenticated && showOptions && (
        <div
          className="absolute w-48 bg-white shadow-md rounded p-2 flex flex-col items-start text-sm"
          onMouseEnter={() => setShowOptions(true)}
          onMouseLeave={() => setShowOptions(false)}
        >
          <p className="p-2 w-full hover:bg-gray-100 cursor-pointer" onClick={() => handleNavigate('/my-data')}>Ustawienia</p>
          <p className="p-2 w-full hover:bg-gray-100 cursor-pointer" onClick={() => handleNavigate('/my-orders')}>Moje zamówienia</p>
          <button 
            className="mt-2 w-full bg-red-500 text-white p-2 rounded hover:bg-red-700 transition duration-200 ease-in-out flex justify-center items-center"
            onClick={handleLogout}
          >
            <MdExitToApp className="mr-2" />
            Wyloguj
          </button>
        </div>
      )}
    </div>
  );
};

export default ProfileOption;
