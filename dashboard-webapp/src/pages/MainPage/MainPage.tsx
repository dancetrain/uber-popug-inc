import React from 'react';
import './MainPage.css';
import TaskList from "../../task-tracker/components/TaskList/TaskList";


const MainPage: React.FC = () => {
  return (
    <div className="MainPage">
      <TaskList />
    </div>
  )
}

export default MainPage;
