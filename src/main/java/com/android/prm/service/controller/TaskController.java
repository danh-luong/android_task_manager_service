package com.android.prm.service.controller;

import com.android.prm.service.accountdto.*;
import com.android.prm.service.mapper.TaskMapper;
import com.android.prm.service.mapper.UserMapper;
import com.android.prm.service.mapper.WorkFlowMapper;
import com.android.prm.service.model.request.AccountRequest;
import com.android.prm.service.model.request.UserProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class TaskController implements Serializable {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkFlowMapper workFlowMapper;

    @PostMapping("/currentTask")
    public List<TaskDTO> getTaskList(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        UserProfile userProfile;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskId(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadTaskById(listIdTask.get(i), String.valueOf(userId));
                taskDTO.setTxtAssignee(userProfile.getName());
                if (taskDTO.getStatus().equals("Doing")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/pendingEmployeeTask")
    public List<TaskDTO> getPendingTask(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        UserProfile userProfile;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskId(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadTaskById(listIdTask.get(i), String.valueOf(userId));
                taskDTO.setTxtAssignee(userProfile.getName());
                if (taskDTO.getStatus().equals("Pending")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/suspendEmployeeTask")
    public List<TaskDTO> getSuspendTask(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        UserProfile userProfile;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskId(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadTaskById(listIdTask.get(i), String.valueOf(userId));
                taskDTO.setTxtAssignee(userProfile.getName());
                if (taskDTO.getStatus().equals("Suspend")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/declineEmployeeTask")
    public List<TaskDTO> getDeclineTask(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        UserProfile userProfile;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskId(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadTaskById(listIdTask.get(i), String.valueOf(userId));
                taskDTO.setTxtAssignee(userProfile.getName());
                if (taskDTO.getStatus().equals("Decline")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/taskDetail")
    public TaskDetailDTO getTaskDetailById(@RequestBody String taskIdJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        TaskDetailDTO taskDetailDTO = null;
        try {
            node = objectMapper.readTree(taskIdJson);
            taskDetailDTO = taskMapper.loadTaskDetailById(node.get("taskId").asInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailDTO;
    }

    @PostMapping("/historyTask")
    public List<HistoryTaskDTO> getHistoryTask(@RequestBody String taskIdJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<HistoryTaskDTO> historyTaskDTOList = null;
        JsonNode node = null;
        try {
            node = objectMapper.readTree(taskIdJson);
            historyTaskDTOList = taskMapper.loadHistoryOfTask(node.get("taskId").asInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(historyTaskDTOList);
        return historyTaskDTOList;
    }

    @PostMapping("/insertNewTask")
    public void insertNewTask(@RequestBody TaskCreatedDTO taskCreatedDTO) {
        try {
            Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskCreatedDTO.getStartDate());
            java.sql.Date startDateSQL = new java.sql.Date(startDate.getTime());
            Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskCreatedDTO.getEndDate());
            java.sql.Date endDateSQL = new java.sql.Date(endDate.getTime());
            java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            UserDTO userDTO = userMapper.loadUserManagerByEmployee(taskCreatedDTO.getUserAssignee());
            if (taskCreatedDTO.getUserCreationName() == null) {
                taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, userDTO.getId(), taskCreatedDTO.getStatus());
                String currentTaskId = taskMapper.selectCurrentRecordTask();
                workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, taskCreatedDTO.getStatus());
                String currentWorkFlowTaskId = workFlowMapper.getCurrentRecordWorkFlowTask();
                int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
                workFlowMapper.insertUserTask(String.valueOf(idOfAssignee), currentWorkFlowTaskId);
            } else {
                if (!taskCreatedDTO.getUserCreationName().equals(taskCreatedDTO.getUserAssignee())) {
                    taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, userDTO.getId(), "Doing");
                    String currentTaskId = taskMapper.selectCurrentRecordTask();
                    workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, "Doing");
                    String currentWorkFlowTaskId = workFlowMapper.getCurrentRecordWorkFlowTask();
                    int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
                    workFlowMapper.insertUserTask(String.valueOf(idOfAssignee), currentWorkFlowTaskId);
                } else {
                    userDTO = userMapper.loadUserAdminByManager(taskCreatedDTO.getUserAssignee());
                    taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, userDTO.getId(), taskCreatedDTO.getStatus());
                    String currentTaskId = taskMapper.selectCurrentRecordTask();
                    workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, taskCreatedDTO.getStatus());
                    String currentWorkFlowTaskId = workFlowMapper.getCurrentRecordWorkFlowTask();
                    int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
                    workFlowMapper.insertUserTask(String.valueOf(idOfAssignee), currentWorkFlowTaskId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/currentTaskManager")
    public List<TaskDTO> getTaskListManager(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        AccountRequest accountRequest;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskIdManager(userId);
            accountRequest = userMapper.loadUserByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
                taskDTO.setTxtAssignee(accountRequest.getUsername());
                if (taskDTO.getStatus().equals("Doing")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/pendingTaskManager")
    public List<TaskDTO> getPendingListManager(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        AccountRequest accountRequest;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskIdManager(userId);
            accountRequest = userMapper.loadUserByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
                taskDTO.setTxtAssignee(accountRequest.getUsername());
                if (taskDTO.getStatus().equals("Pending")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/suspendTaskManager")
    public List<TaskDTO> getSuspendListManager(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        AccountRequest accountRequest;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskIdManager(userId);
            accountRequest = userMapper.loadUserByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
                taskDTO.setTxtAssignee(accountRequest.getUsername());
                if (taskDTO.getStatus().equals("Suspend")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/declineTaskManager")
    public List<TaskDTO> getDeclineListManager(@RequestBody String usernameJson) {
        List<Integer> listIdTask;
        List<TaskDTO> listTask = new ArrayList<>();
        AccountRequest accountRequest;
        TaskDTO taskDTO;
        int userId;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            userId = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            listIdTask = taskMapper.loadTaskIdManager(userId);
            accountRequest = userMapper.loadUserByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
                taskDTO.setTxtAssignee(accountRequest.getUsername());
                if (taskDTO.getStatus().equals("Decline")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/acceptTask")
    public void acceptTaskPending(@RequestBody AcceptDeclineDTO acceptDeclineDTO) {
        java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        int userWorkFlowId = workFlowMapper.selectUserIdThroughWorkFlow(acceptDeclineDTO.getTaskId());
        taskMapper.updateTaskAccept(acceptDeclineDTO.getTaskId());
        workFlowMapper.insertWorkFlowTaskAcceptOrDecline(acceptDeclineDTO.getTaskId(), currentDateSQL, "Doing");
        String currentWorkFlow = workFlowMapper.getCurrentRecordWorkFlowTask();
        workFlowMapper.insertUserTask(String.valueOf(userWorkFlowId), currentWorkFlow);
    }

    @PostMapping("/declineTask")
    public void declineTaskPending(@RequestBody AcceptDeclineDTO acceptDeclineDTO) {
        java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        int userWorkFlowId = workFlowMapper.selectUserIdThroughWorkFlow(acceptDeclineDTO.getTaskId());
        taskMapper.updateTaskDecline(acceptDeclineDTO.getTaskId());
        workFlowMapper.insertWorkFlowTaskAcceptOrDecline(acceptDeclineDTO.getTaskId(), currentDateSQL, "Decline");
        String currentWorkFlow = workFlowMapper.getCurrentRecordWorkFlowTask();
        workFlowMapper.insertUserTask(String.valueOf(userWorkFlowId), currentWorkFlow);
    }

    @PostMapping("/pendingTask")
    public List<TaskDTO> loadPendingTaskOfSpecificManager(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        int currentIdUser = 0;
        try {
            node = objectMapper.readTree(username);
            currentIdUser = userMapper.loadIdOfUserByUsername(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskMapper.loadPendingTask(String.valueOf(currentIdUser));
    }

    @PostMapping("/updateTask")
    public void updateTask(@RequestBody UpdatedTaskDTO updatedTaskDTO) {
        try {
            Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(updatedTaskDTO.getEndDate());
            java.sql.Date endDateSQL = new java.sql.Date(endDate.getTime());
            taskMapper.updateTask(updatedTaskDTO.getTaskName(), updatedTaskDTO.getDescriptionTask(), endDateSQL, updatedTaskDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/suspendTask")
    public void updateTask(@RequestBody String taskId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = null;
            node = objectMapper.readTree(taskId);
            taskMapper.suspendTask(node.get("taskId").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/updatePendingTask")
    public void updatePendingTask(@RequestBody TaskCreatedDTO taskCreatedDTO) {
        try {
            Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskCreatedDTO.getStartDate());
            java.sql.Date startDateSQL = new java.sql.Date(startDate.getTime());
            Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskCreatedDTO.getEndDate());
            java.sql.Date endDateSQL = new java.sql.Date(endDate.getTime());
            taskMapper.updatePendingTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(),
                    startDateSQL, endDateSQL, taskCreatedDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/assignTaskToAnotherEmployee")
    public void assignTaskToAnotherEmployee(@RequestBody UserTaskIdSpinner userTaskIdSpinner) {
        try {
            String newUserIdTask = userTaskIdSpinner.getUsername();
            int currentWorkFlow = workFlowMapper.getCurrentWorkFlowOfTask(userTaskIdSpinner.getTaskId());
            java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            workFlowMapper.updateTaskNotDoneForCurrentUserDoing(String.valueOf(currentWorkFlow), currentDateSQL);
            workFlowMapper.insertNewWorkFlowTaskForNewEmployee(userTaskIdSpinner.getTaskId(), currentDateSQL, "Doing");
            String taskId = workFlowMapper.getCurrentRecordWorkFlowTask();
            workFlowMapper.insertUserTask(newUserIdTask, taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
