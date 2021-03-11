import React from 'react';
import './TaskList.css';
import { useFetchTasks } from "../../services/TaskTrackerService/TaskTrackerService";
import { Col, Row } from "react-bootstrap";

const TaskList: React.FC = () => {
  const {tasks, isLoading, error} = useFetchTasks()

  if (isLoading) {
    return <p>Loading...</p>
  }
  if (error) {
    return <p>Error...{error}</p>
  }

  return (
    <Row className="TaskList">
      {tasks.map(task => {
        return <Col key={task.taskId}>
          <p>{task.taskInfo?.taskTitle} ({task.taskId})</p>
        </Col>
      })}

      <Col>
        Add Task
      </Col>
    </Row>
  );
}

export default TaskList;
