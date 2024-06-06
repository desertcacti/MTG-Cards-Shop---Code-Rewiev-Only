

import {
  LOGIN_REQUEST,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
  REGISTER_REQUEST,
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  EMAIL_VERIFICATION_REQUEST,
  EMAIL_VERIFICATION_SUCCESS,
  EMAIL_VERIFICATION_FAILURE,
  RESEND_CONFIRMATION_REQUEST,
  RESEND_CONFIRMATION_SUCCESS,
  RESEND_CONFIRMATION_FAIL
} from '../types';



const initialState = {
  isAuthenticated: false,
  user: null,
  token: typeof localStorage !== 'undefined' ? localStorage.getItem('token') : null,
  loading: false,
  error: null
};
const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_REQUEST:
    case REGISTER_REQUEST:
    case EMAIL_VERIFICATION_REQUEST:
    case RESEND_CONFIRMATION_REQUEST:
      return {
        ...state,
        loading: true,
        error: null
      };

    case LOGIN_SUCCESS:
    case REGISTER_SUCCESS:
    case EMAIL_VERIFICATION_SUCCESS:
    case RESEND_CONFIRMATION_SUCCESS:
      localStorage.setItem('token', action.payload.token); 
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload.userDetails,
        token: action.payload.token,
        loading: false
      };

    case LOGIN_FAIL:
    case REGISTER_FAIL:
    case EMAIL_VERIFICATION_FAILURE:
    case RESEND_CONFIRMATION_FAIL:
      localStorage.removeItem('token');
      return {
        ...state,
        isAuthenticated: false,
        user: null,
        token: null,
        error: action.payload.message,
        loading: false
      };

    case LOGOUT:
      localStorage.removeItem('token');
      return {
        ...state,
        isAuthenticated: false,
        user: null,
        token: null,
        loading: false
      };

    default:
      return state;
  }
};

export default authReducer;