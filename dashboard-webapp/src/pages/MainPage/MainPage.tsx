import React, { useState } from 'react';
import './MainPage.css';
import TaskList from "../../task-tracker/components/TaskList/TaskList";
import TaskReassignButton from "../../task-tracker/components/TaskReassignButton/TaskReassignButton";
import { Task } from "../../task-tracker/types/tasks";


const MainPage: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([])

  return (
    <div className="MainPage">
      <TaskReassignButton onComplete={data => setTasks(data)} />
      <hr />
      <TaskList tasks={tasks}/>
    </div>
  )
}

export default MainPage;
