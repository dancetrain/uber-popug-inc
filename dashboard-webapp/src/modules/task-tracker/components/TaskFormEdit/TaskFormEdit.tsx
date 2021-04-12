import React from 'react';
import './TaskFormEdit.css';
import { Task, TaskSchema } from "../../types/tasks";
import Form, { ISubmitEvent } from "@rjsf/core";
import { executeUpdateTask } from "../../services/TaskTrackerService/TaskTrackerService";

export interface TaskFormEditProps {
  task: Task
  onSubmit: { (task: Task): void }
}

const TaskFormEdit: React.FC<TaskFormEditProps> = props => {
  const submitHandler: { (event: ISubmitEvent<Task>): void } = evt => {
    console.log("TaskFormEdit Submit:", evt.formData);
    if (evt.formData.taskInfo) {
      executeUpdateTask(evt.formData.taskId, evt.formData.taskInfo)
        .then(task => props.onSubmit(task))
      // catch an error?
    }
  };

  const task = {...props.task}
  console.log("Schema", TaskSchema)
  console.log("Task", task)
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
