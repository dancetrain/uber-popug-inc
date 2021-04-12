import React, { useState } from 'react';
import './TaskButtonEdit.css';
import { Task } from "../../types/tasks";
import { Button, Modal } from "react-bootstrap";
import TaskFormEdit from "../TaskFormEdit/TaskFormEdit";

export interface TaskButtonEditProps {
  task: Task,
  onClose: (task: Task | undefined) => void
}

const TaskButtonEdit: React.FC<TaskButtonEditProps> = props => {
  const [show, setShow] = useState(false);

  const handleClose = () => {
    props.onClose(undefined);
    setShow(false);
  }
  const handleShow = () => {
    setShow(true);
  }

  const taskCopy = {...props.task}

  return (
    <div className="TaskButtonEdit">
      <Button variant="primary" onClick={handleShow}>
        Edit
      </Button>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Edit task {taskCopy.taskInfo?.taskTitle}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <TaskFormEdit
            task={taskCopy}
            onSubmit={
              task => {
                props.onClose(task);
                setShow(false);
              }
            }
          />
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default TaskButtonEdit;
