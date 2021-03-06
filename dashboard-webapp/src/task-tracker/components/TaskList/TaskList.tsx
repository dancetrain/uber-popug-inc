import React, { useState } from 'react';
import './TaskList.css';
import { useFetchTasks } from "../../services/TaskTrackerService/TaskTrackerService";
import { ButtonGroup, ButtonToolbar, Card, Col, Row } from "react-bootstrap";
import TaskButtonAdd from "../TaskButtonAdd/TaskButtonAdd";
import { Task } from "../../types/tasks";
import TaskButtonEdit from "../TaskButtonEdit/TaskButtonEdit";
import TaskButtonComplete from "../TaskButtonComplete/TaskButtonComplete";

interface TaskListItemProps {
  task: Task,
  onUpdate: (task: Task) => void
}

const TaskListItem: React.FC<TaskListItemProps> = props => {
  return (
    <Card>
      <Card.Header>
        <Card.Title>
          {props.task.taskInfo?.taskTitle || 'Missing Task Title'}
        </Card.Title>
      </Card.Header>
      <Card.Body>
        <Card.Text>
          {props.task.taskInfo?.taskDescription}
        </Card.Text>
        <Card.Text>
          {props.children}
        </Card.Text>
        <Card.Text>
          Assigned <strong>{props.task.assignedPopug?.publicName}</strong>
        </Card.Text>
        <Card.Text>
          Complete? {props.task.complete ? 'Yes' : 'No'}
        </Card.Text>
      </Card.Body>
      <Card.Footer>
        <ButtonToolbar>
          <ButtonGroup>
            <TaskButtonEdit task={props.task} onClose={task => {
              if (task) {
                props.onUpdate(task)
              }
              console.log("Task modified", task)
            }} />
            <TaskButtonComplete task={props.task} onComplete={task => {
              props.onUpdate(task)
            }} />
          </ButtonGroup>
        </ButtonToolbar>
      </Card.Footer>
    </Card>
  )
}

export interface TaskListProps {
  tasks: Task[]
}

const TaskList: React.FC<TaskListProps> = props => {
  const [newTask, setNewTask] = useState<Task | undefined>(undefined);
  const {tasks, isLoading, error} = useFetchTasks()

  if (isLoading) {
    return <p>Loading...</p>
  }
  if (error) {
    return <p>Error...{error}</p>
  }

  const createTask = (task: Task | undefined) => {
    console.log("Submitted taskInfo", task)

    if (newTask) {
      setNewTask(task)
    }
  }

  const onTaskUpdate = (task: Task) => {
    console.log("TaskUpdate", task)
  }

  return (
    <>
      <Row className="TaskList">
        {tasks.map(task => {
          return <Col key={task.taskId} xs={3}>
            <TaskListItem task={task} onUpdate={onTaskUpdate} />
          </Col>
        })}

        {(newTask) ? <Col key={newTask.taskId}>
          <TaskListItem task={newTask} onUpdate={onTaskUpdate}/>
        </Col> : ''}

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
