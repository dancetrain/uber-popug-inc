import React from 'react';
import './TaskReassignButton.css';
import { Button } from "react-bootstrap";
import { executeReassignTasks } from "../../services/TaskTrackerService/TaskTrackerService";
import { Task } from "../../types/tasks";

export interface TaskReassignButtonProps {
  onComplete: (tasks: Task[]) => void
}

const TaskReassignButton: React.FC<TaskReassignButtonProps> = props => {
  const reassignCall = () => {
    executeReassignTasks()
      .then(props.onComplete)
  }

  return (
    <div className="TaskReassignButton">
      <Button onClick={reassignCall}>
        Reassign
      </Button>
    </div>
  );
}

export default TaskReassignButton;
