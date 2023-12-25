import React, { useEffect, useState,useContext } from "react";
import toast from "react-hot-toast";
import axios from 'axios';
import TaskContext from "./TaskContext";

export const UpdateForm = (props) => {
  const {resetForm} = useContext(TaskContext);
  const [formData, setFormData] = useState(props.getEditedRecord());
  const statusOptions = ['To Do','In Progress','Done'];

  useEffect(() => {
    setFormData(props.getEditedRecord());
  }, [props.getEditedRecord()]);

  const myHandleText = (myevent) => {
    setFormData({ ...formData,[myevent.target.name]: myevent.target.value });
  }

  const myFormSubmit = async (event) => {
    try {
      event.preventDefault();
      const authHeader = { headers: { Authorization: 'Bearer ' + localStorage.getItem('accessToken') } };
      const response = await axios.put(process.env.REACT_APP_UpdateTask_URL, formData, authHeader);
      if (response.status === 200) {
      const updatedTasks = props.taskDetails.map((task) =>
        task.taskId === formData.taskId ? {taskId:formData.taskId, title: formData.title,
          description:formData.description,status:formData.status } : task
      );
      props.setTask(updatedTasks);
      resetForm(); 
      toast.success(response.data); 
      }
    } catch (error) {
      toast.error(error.response.data);
      console.error('Error Updating task:', error);
    }
  };

  return ( 
  <div className="MyForm">
  <form className="update-form" onSubmit={myFormSubmit}>
  <div style={{float:'left',paddingRight: '50px'}}>
    <label htmlFor="title">Title</label><br/>
    <input type="text" value={formData.title} id="title" name="title" onChange={myHandleText} autoComplete="on" required/>
    </div>
    <div style={{float:'left',paddingRight: '50px'}}>
    <label htmlFor="description">Description</label><br />
    <input type="text" value={formData.description} id="description" name="description" onChange={myHandleText} autoComplete="on" required/>
    </div>
   <div style={{float:'left',paddingRight: '50px',paddingTop: '10px'}}>
    <label htmlFor="status">Status</label><br />
    <select className="DropdownWidth" id="status" name="status"  value={formData.status} onChange={myHandleText} >
    {statusOptions.map((x) => <option key={x}>{x}</option>)}
    </select>
    </div>
    <div style={{float:'left',paddingRight: '50px',paddingTop: '30px'}}>
    <button type="submit">Update</button>
    </div>
  </form>
  </div>
  )

}