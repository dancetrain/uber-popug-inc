import React, { useState } from 'react';
import './TaskButtonAdd.css';
import { Button, Modal } from "react-bootstrap";
import { Task } from "../../types/tasks";
import TaskFormCreate from "../TaskFormCreate/TaskFormCreate";

export interface TaskButtonAddProps {
  onClose: (task?: Task) => void
}

const TaskButtonAdd: React.FC<TaskButtonAddProps> = props => {
  const [show, setShow] = useState(false);

  const handleClose = () => {
    props.onClose(undefined);
    setShow(false);
  }
  const handleShow = () => {
    setShow(true);
  }

  return (
    <>
      <Button variant="primary" onClick={handleShow}>
        Add Task
      </Button>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Add new task</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <TaskFormCreate
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
    </>
  );
}

export default TaskButtonAdd;
