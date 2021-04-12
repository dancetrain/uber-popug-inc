import React, { useState } from 'react';
import './TaskButtonComplete.css';
import { Task } from "../../types/tasks";
import { Button } from "react-bootstrap";
import { executeCompleteTask } from "../../services/TaskTrackerService/TaskTrackerService";

export interface TaskButtonCompleteProps {
  task: Task,
  onComplete: (task: Task) => void
}

const TaskButtonComplete: React.FC<TaskButtonCompleteProps> = props => {
  const handleComplete = () => {
    executeCompleteTask(props.task.taskId)
      .then(task => props.onComplete(task))
  }

  return (
    <div className="TaskButtonComplete">
      <Button onClick={handleComplete}>
        Complete!
      </Button>
    </div>
  );
}

export default TaskButtonComplete;
