import React from 'react';
import './TaskFormEdit.css';
import { Task, TaskSchema } from "../../types/tasks";
import Form, { ISubmitEvent } from "@rjsf/core";

export interface TaskFormEditProps {
  task: Task
  onSubmit: { (task: Task): void }
}

const TaskFormEdit: React.FC<TaskFormEditProps> = props => {
  const submitHandler: { (event: ISubmitEvent<Task>): void } = evt => {
    console.log("TaskFormEdit Submit:", evt.formData);
    props.onSubmit(evt.formData);
  };

  const task = {...props.task}

  return (
    <div className="TaskFormEdit">
      <Form schema={TaskSchema}
            formData={task}
            onSubmit={submitHandler}
      />
    </div>
  );
}

export default TaskFormEdit;
