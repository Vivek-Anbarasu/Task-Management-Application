import React, { useContext } from "react";
import toast from "react-hot-toast";
import axios from 'axios';
import '../css/table.css';
import * as Yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import TaskContext from "./TaskContext";

export const AddForm = (props) => {
  const {resetForm} = useContext(TaskContext);

  const schema = Yup.object().shape({
    title: Yup.string().required(),
    description: Yup.string().required(),
    status: Yup.string().required()
  });

  const statusOptions = ['To Do','In Progress','Done'];

  const { register, handleSubmit, formState: {errors} } = useForm({
    mode: 'onSubmit',
    resolver: yupResolver(schema)
  });

  const myFormSubmit = async (data) => {
    try {
      const authHeader = { headers: { Authorization: 'Bearer ' + localStorage.getItem('accessToken') } };
      const response = await axios.post(process.env.REACT_APP_SaveTask_URL, data, authHeader);
      if (response.status === 201) {
      data["taskId"] = response.data;
      const newTaskList = [...props.taskDetails, data]
      props.setTask(newTaskList);
      resetForm();
      toast.success("Successfully Saved"); 
      }
    } catch (error) {
      toast.error(error.response.data);
      console.error('Error adding task:', error);
    }
  };

  return (
  <div className="MyForm">
  <form className="add-form" onSubmit={handleSubmit(myFormSubmit)}>
  <div style={{float:'left',paddingRight: '50px'}}>
    <label htmlFor="title">Title</label><br/>
    <div className="error-message">{errors.title && <p>{errors.title.message}</p>}</div>
    <input type="text" id="title" name="title"  {...register('title')} autoComplete="off"/>
    </div>
    <div style={{float:'left',paddingRight: '50px'}}>
    <label htmlFor="description">Description</label><br />
    <div className="error-message">{errors.description && <p>{errors.description.message}</p>}</div>
    <input type="text" id="description" name="description"  {...register('description')} autoComplete="on"/>
    </div>
   <div style={{float:'left',paddingRight: '50px',paddingTop: '10px'}}>
    <label htmlFor="status">Status</label><br />
    <div className="error-message">{errors.status && <p>{errors.status.message}</p>}</div>
    <select className="DropdownWidth" id="status" name="status" {...register("status")}>
    {statusOptions.map((x) => <option key={x}>{x}</option>)}
    </select>
    </div>
    <div style={{float:'left',paddingTop: '30px'}}>
    <button type="submit" >Save</button>
    </div>
  </form>
  </div>
  )

}