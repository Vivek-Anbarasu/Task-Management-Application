import React, { useEffect, useState } from "react";
import toast from "react-hot-toast";
import axios from 'axios';

export const UpdateForm = (props) => {

  const [formData, setFormData] = useState(props.getEditedRecord());

  useEffect(() => {
    setFormData(props.getEditedRecord());
  }, [props.getEditedRecord()]);

  const myHandleText = (myevent) => {
    setFormData({ ...formData,[myevent.target.name]: myevent.target.value });
  }

  const myFormSubmit = (event) => {
    event.preventDefault();
    const authHeader = { headers: { Authorization: 'Bearer ' + localStorage.getItem('accessToken') } };
    axios.post(process.env.REACT_APP_UpdateTask_URL, formData, authHeader)
      .then((response) => {
        props.getAllTasks();
        if(response.data.message === 'Successfully Updated'){
          toast.success(response.data.message); 
      }else{
          toast.error(response.data.message);
      }
      }, (error) => {
        console.log(error);
      });
  }

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
          <option value="To Do">To Do</option>
          <option value="In Progress">In Progress</option>
          <option value="Done">Done</option>
    </select>
    </div>
    <div style={{float:'left',paddingRight: '50px',paddingTop: '30px'}}>
    <button type="submit">Update</button>
    </div>
  </form>
  </div>
  )

}