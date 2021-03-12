// TODO: Generate meta info from server

export interface UserInfo {
  publicName: string
}

export interface TaskInfo {
  taskTitle: string
  taskDescription: string
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
    "taskDescription": {
      "type": "string" as const,
    }
  },
  "required": ["taskTitle", "taskDescription"]
}

export const CreateTaskSchema = {
  "id": "urn:jsonschema:com:uberpopug:tasktracker:task:Task",
  "type": "object" as const,
  "properties": {
    "taskInfo": {
      "$ref": "#/definitions/taskInfo"
    },
    "taskOwner": {
      "$ref": "#/definitions/userInfo"
    },
  },
  "definitions": {
    "taskInfo": TaskInfoSchema,
    "userInfo": UserInfoSchema
  },
}

export const TaskSchema = {
  "id": "urn:jsonschema:com:uberpopug:tasktracker:task:Task",
  "type": "object" as const,
  "properties": {
    "taskId": {
      "type": "string" as const,
    },
    "taskInfo": {
      "$ref": "#/definitions/taskInfo"
    },
    "taskOwner": {
      "$ref": "#/definitions/userInfo"
    },
    "assignedUser": {
      "$ref": "#/definitions/userInfo"
    }
  },
  "definitions": {
    "taskInfo": TaskInfoSchema,
    "userInfo": UserInfoSchema
  },
  "required": ["taskId", "taskInfo"]
}
