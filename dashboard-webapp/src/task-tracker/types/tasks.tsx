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

export const UserInfoSchema = {
  "id": "urn:jsonschema:com:uberpopug:tasktracker:task:UserInfo",
  "type": "object" as const,
  "properties": {
    "publicName": {
      "type": "string" as const
    }
  },
  "required": ["publicName"]
}

// const taskInfoSchema: JSONSchema7 = {
export const TaskInfoSchema = {
  "id": "urn:jsonschema:com:uberpopug:tasktracker:task:TaskInfo",
  "type": "object" as const,
  "properties": {
    "taskTitle": {
      "type": "string" as const,
    },
    "taskOwner": {
      "$ref": "#/definitions/userInfo"
    },
    "assignedUser": {
      "$ref": "#/definitions/userInfo"
    }
  },
  "definitions": {
    "userInfo": UserInfoSchema
  },
  "required": ["taskTitle"]
}


export const TaskSchema = {
  "id": "urn:jsonschema:com:uberpopug:tasktracker:task:Task",
  "type": "object" as const,
  "properties": {
    "taskId": {
      "type": "string" as const,
    }
  },
  "definitions": {
    "taskInfo": TaskInfoSchema
  },
  "required": ["taskId"]
}
