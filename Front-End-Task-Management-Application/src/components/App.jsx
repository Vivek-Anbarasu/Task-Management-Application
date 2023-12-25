import { Toaster } from "react-hot-toast"; 
import {PageNotFound} from './PageNotFound';
import {MyTable} from './MyTable';
import { BrowserRouter,Routes, Route  } from 'react-router-dom'; 
import React, { useState } from "react";
import '../css/App.css';
import { Login } from "./Login";
import { Register } from "./Register";

export const App = () => {

  const [currentForm, setCurrentForm] = useState('login');

  const toggleForm = (formName) => {
    setCurrentForm(formName);
  }

  return (
    <div className="Application">
    <Toaster position="top-center" /> 
    <BrowserRouter>
    <Routes> 
      <Route path="/" element={currentForm === "login" 
      ? <Login onFormSwitch={toggleForm}/> 
      : <Register onFormSwitch={toggleForm} />}/>
      <Route path="Table" element={<MyTable/>} />
      <Route path="*" element={<PageNotFound/>} />
    </Routes>
    </BrowserRouter>
    </div>
  );
}