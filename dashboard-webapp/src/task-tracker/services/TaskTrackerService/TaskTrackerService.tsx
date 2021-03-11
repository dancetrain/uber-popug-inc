import { useState } from "react";
import { Task, TaskInfo } from "../../types/tasks";
import { useQuery } from "react-query";


export function useFetchTasks() {
  const [tasks, setTasks] = useState<Task[]>([])

  const { isLoading, error } = useQuery('allTasks', () =>
     fetch('http://localhost:8080/task').then(res =>
       res.json()
     ).then((tasks: Task[]) => {
       if (tasks && tasks.length && typeof (tasks) === 'object') {
         setTasks(tasks)
       } else {
         console.log("Wrong response", tasks)
       }
     })
   )

  return {
    tasks,
    isLoading,
    error
  }
}

export function useFetchTask(taskId: string) {
  const [task, setTask] = useState<Task | null>(null)

  const { isLoading, error } = useQuery(`task-${taskId}`, () =>
    fetch(`http://localhost:8080/task/${taskId}`, {
       headers: {
         "Content-Type": "application/json"
       },
       method: "GET",
     }).then(res =>
       res.json()
     ).then((task: Task) => {
       if (task && typeof (task) === 'object') {
         setTask(task)
       } else {
         console.log("Wrong response", task)
       }
     })
  )
  return {
    task,
    isLoading,
    error
  }
}

export function useUpdateTask(taskId: string, taskInfo: TaskInfo) {
  const [task, setTask] = useState<Task | null>(null)

  const { isLoading, error } = useQuery(`task-${taskId}`, () =>
     fetch(`http://localhost:8080/task/${taskId}`, {
       headers: {
         "Content-Type": "application/json"
       },
       method: "PUT",
       body: JSON.stringify(taskInfo)
     }).then(res =>
       res.json()
     ).then((task: Task) => {
       if (task && typeof (task) === 'object') {
         setTask(task)
       } else {
         console.log("Wrong response", task)
       }
     })
   )

  return {
    task,
    isLoading,
    error
  }
}

export function useDeleteTask(taskId: string) {
  const [task, setTask] = useState<Task | null>(null)

  const { isLoading, error } = useQuery(`task-${taskId}`, () =>
     fetch(`http://localhost:8080/task/${taskId}`, {
       method: "DELETE",
     }).then(res =>
       res.json()
     ).then((task: Task) => {
       if (task && typeof (task) === 'object') {
         setTask(task)
       } else {
         console.log("Wrong response", task)
       }
     })
   )

  return {
    task,
    isLoading,
    error
  }
}


