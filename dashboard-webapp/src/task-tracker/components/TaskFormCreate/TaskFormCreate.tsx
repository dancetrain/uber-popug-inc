import React from 'react';
import './TaskFormCreate.css';
import { CreateTaskSchema, Task, TaskInfo, TaskInfoSchema } from "../../types/tasks";
import Form, { ISubmitEvent } from "@rjsf/core"
import { executeCreateTask } from "../../services/TaskTrackerService/TaskTrackerService";

export interface TaskFormCreateProperties {
  onSubmit: { (task: Task): void }
}

const TaskFormCreate: React.FC<TaskFormCreateProperties> = props => {
  const submitHandler: { (event: ISubmitEvent<Task>): void } = evt => {
    console.log("TaskFormCreate Submit:", evt.formData);

    executeCreateTask(evt.formData.taskInfo!!)
      .then(task => {
        props.onSubmit(task);
      })
  };

  return (
    <div className="TaskFormCreate">
      <Form schema={CreateTaskSchema}
            onSubmit={submitHandler}
      />
    </div>
  );
}

export default TaskFormCreate;
