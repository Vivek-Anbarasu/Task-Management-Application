import React, { useState } from "react";
import toast from 'react-hot-toast';
import * as Yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


export const Login = (props) => {
    const navigate = useNavigate();
    localStorage.setItem('accessToken','');
    localStorage.setItem('userName','');

    const loginApplication = async (data) => {
          axios.post(process.env.REACT_APP_Auth_URL, {username: data.username,password: data.password})
          .then((response) => {
              localStorage.setItem('accessToken',response.data.accessToken); 
              localStorage.setItem('userName',data.username); 
              toast.success("Successfully Logged In");
              navigate("/Table");
            }, (error) => {
              console.log(error);
              toast.error("Invalid Credentials");
            });
      }

    const schema = Yup.object().shape({
        username: Yup.string().required("Please enter your username"),
        password: Yup.string().required("Please enter your password")
      });

      const { register, handleSubmit, formState: { errors, isDirty, isValid } } = useForm({
        mode: 'onSubmit',
        resolver: yupResolver(schema)
      });

    return (
        <div className="auth-form-container"> 
            <h2>Login</h2>
            <form className="login-form" onSubmit={handleSubmit(loginApplication)}>
                <label htmlFor="username">User Name</label>
                <div className="error-message">{errors.username && <p>{errors.username.message}</p>}</div>
                <input type="username" placeholder="user name" id="username" name="username" {...register('username')} autoComplete="off" />
          
                <label htmlFor="password">Password</label>
                <div className="error-message">{errors.password && <p>{errors.password.message}</p>}</div>
                <input  type="password" placeholder="********" id="password" name="password" {...register('password')} />
                
                <button type="submit" >Log In</button>
            </form>
            <button className="link-btn" onClick={() => props.onFormSwitch('register')}>Don't have an account? Register here.</button>
        </div>
    )
}