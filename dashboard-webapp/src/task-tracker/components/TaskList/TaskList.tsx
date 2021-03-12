import React from 'react';
import './TaskList.css';
import { useFetchTasks } from "../../services/TaskTrackerService/TaskTrackerService";
import { Button, Col, Row } from "react-bootstrap";
import TaskButtonAdd from "../TaskButtonAdd/TaskButtonAdd";
import { Task, TaskInfo } from "../../types/tasks";

const TaskList: React.FC = () => {
  const {tasks, isLoading, error} = useFetchTasks()

  if (isLoading) {
    return <p>Loading...</p>
  }
  if (error) {
    return <p>Error...{error}</p>
  }

  const createTask = (task: Task | undefined) => {
    console.log("Submitted taskInfo", task)
  }

  return (
    <>
      <Row className="TaskList">
        {tasks.map(task => {
          return <Col key={task.taskId}>
            <p>{task.taskInfo?.taskTitle} ({task.taskId})</p>
          </Col>
        })}
      </Row>
      <Row>
        <Col>
          <TaskButtonAdd onClose={createTask}/>
        </Col>
      </Row>
    </>
  );
}

export default TaskList;