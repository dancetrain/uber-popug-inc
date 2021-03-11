// TODO: Generate meta info from server

export interface UserInfo {
  publicName: string
}

export interface TaskInfo {
  taskTitle: string
  taskOwner?: UserInfo
  assignedUser?: UserInfo
}

export interface Task {
  taskId: string
  taskInfo?: TaskInfo
}
