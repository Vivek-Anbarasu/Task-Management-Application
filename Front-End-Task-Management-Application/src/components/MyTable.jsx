import React, { useEffect, useState,useMemo } from 'react';
import '../css/table.css';
import { AddForm } from './AddForm';
import { UpdateForm } from './UpdateForm';
import axios from 'axios';
import toast from "react-hot-toast";
import { useNavigate } from 'react-router-dom'; 

import BootstrapTable from 'react-bootstrap-table-next';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.css';
import paginationFactory from 'react-bootstrap-table2-paginator';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import filterFactory, {textFilter} from "react-bootstrap-table2-filter";



export const MyTable = (props) => {
  const [currentForm, setCurrentForm] = useState('');
  const [editedRecord, setEditedRecord] = useState();
  const [taskDetails, setTaskDetails] = useState([]);
  const navigate = useNavigate();
 
  const toggleForm = (formName) => {
    setCurrentForm(formName);
  }

  const logOut = () => {
    navigate("/");
  }

  const getEditedRecord = ()=> {
    return editedRecord;
  }

  const editRecord = (taskId,formName) => {
    setCurrentForm(formName);
    const editedRecord = taskDetails.filter((mydata, myindex) => {
      return mydata.taskId === taskId;
    });
    setEditedRecord(editedRecord[0]);
  }

  useEffect(() => {
    if(localStorage.getItem('accessToken') === ''){
      navigate("/");
    }else{
      getAllTasks();
    }
  }, []);

  const getAllTasks = () => {
    const authHeader = { headers: { Authorization: 'Bearer ' + localStorage.getItem('accessToken') } };
    axios.post(process.env.REACT_APP_GetAllTask_URL, {}, authHeader)
      .then((response) => {
        setTaskDetails(response.data);
        setCurrentForm('');
      }, (error) => {
        console.log(error);
      });
  }

  const removeRecord = (taskId) => {
  const authHeader = { headers: { Authorization: 'Bearer ' + localStorage.getItem('accessToken') } };
    axios.post(process.env.REACT_APP_DeleteTask_URL, { "taskId": taskId }, authHeader)
      .then((response) => {
        getAllTasks();
        toast.success("Successfully Deleted");
      }, (error) => {
        console.log(error);
      });
  }

  const editButton = (cell, row, rowIndex, formatExtraData) => {
    return (
      <button className="button-4" onClick={() => editRecord(row.taskId,'Update')}>Edit</button>
    );
  };

  const deleteButton = (cell, row, rowIndex, formatExtraData) => {
    return (
      <button className="button-4" onClick={() => removeRecord(row.taskId)}>Delete</button> 
    );
  };

  const columns = [
    {dataField: "taskId",text: "Task Id", hidden: true},
    {dataField: "title",text: "Title",sort: true,headerStyle: (colum, colIndex) => {
      return { width: '100px'};
    }},
    {dataField: "description",text: "Description",sort: true},
    {dataField: "status",text: "Status",sort: true , filter: textFilter(),headerStyle: (colum, colIndex) => {
      return { width: '90px'};
    }},
    {dataField: "edit",text: "",formatter: editButton, headerStyle: (colum, colIndex) => {
      return { width: '150px'};
    }},
    {dataField: "delete",text: "",formatter: deleteButton,headerStyle: (colum, colIndex) => {
      return { width: '100px'};
    }}
  ];

  const pagination = paginationFactory({sizePerPage: 3,firstPageText: "First",lastPageText: "Last",
    nextPageText:">",prePageText: "<",alwaysShowAllBtns: true,showTotal: true
  });

  if(currentForm === 'Update'){setCurrentForm(<UpdateForm getAllTasks={getAllTasks} getEditedRecord={getEditedRecord}/>)}

  return (<div>
  <div style={{ 'paddingLeft': '450px' }}>
  <b>{localStorage.getItem('userName')}</b><br/> 
    <button className="button-3" onClick={() => logOut() }>Log Out</button>
  </div>
  <div>
  <div style={{'paddingBottom': '10px', 'paddingRight': '350px', 'fontSize': '20px', 'color': '#c9f3f5' }}>
  <b>Task Management Application   </b><button className="button-4" onClick={() => toggleForm(<AddForm getAllTasks={getAllTasks}/>)} >Add</button></div>
    <div style={{'paddingBottom': '10px', 'paddingRight': '250px','paddingLeft': '100px', 'fontSize': '20px', 'color': '#c9f3f5' }}>
    <BootstrapTable bootstrap4 keyField="taskId" columns={columns} data={taskDetails} pagination={pagination} filter={filterFactory()} />
      </div> 
     
    
    {currentForm}
  </div>
  </div>
  )
}


