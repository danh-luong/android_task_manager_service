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
            for (int i = 0; i < historyTaskDTOList.size(); i++) {
                if (historyTaskDTOList.get(i).getTxtSuspendBy() != null) {
                    String userSuspendName = userMapper.getUserNameById(historyTaskDTOList.get(i).getTxtSuspendBy());
                    historyTaskDTOList.get(i).setTxtSuspendBy(userSuspendName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, null, taskCreatedDTO.getStatus());
                String currentTaskId = taskMapper.selectCurrentRecordTask();
                int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
                workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, taskCreatedDTO.getStatus(), String.valueOf(idOfAssignee));
            } else {
                if (!taskCreatedDTO.getUserCreationName().equals(taskCreatedDTO.getUserAssignee())) {
                    taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, userDTO.getId(), "Doing");
                    String currentTaskId = taskMapper.selectCurrentRecordTask();
                    int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
                    workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, "Doing", String.valueOf(idOfAssignee));
                } else {
                    taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, null, taskCreatedDTO.getStatus());
                    String currentTaskId = taskMapper.selectCurrentRecordTask();
                    int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
                    workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, taskCreatedDTO.getStatus(), String.valueOf(idOfAssignee));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/insertNewTaskAdmin")
    public void insertNewTaskAdmin(@RequestBody TaskCreatedDTO taskCreatedDTO) {
        try {
            Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskCreatedDTO.getStartDate());
            java.sql.Date startDateSQL = new java.sql.Date(startDate.getTime());
            Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskCreatedDTO.getEndDate());
            java.sql.Date endDateSQL = new java.sql.Date(endDate.getTime());
            java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            UserDTO userDTO = userMapper.loadIdOfAdmin(taskCreatedDTO.getUserCreationName());
            taskMapper.insertNewTask(taskCreatedDTO.getName(), taskCreatedDTO.getDescriptionTask(), startDateSQL, endDateSQL, currentDateSQL, userDTO.getId(), "Doing");
            String currentTaskId = taskMapper.selectCurrentRecordTask();
            int idOfAssignee = userMapper.loadIdOfUserByUsername(taskCreatedDTO.getUserAssignee());
            workFlowMapper.createNewWorkFlowTaskFromTask(currentTaskId, currentDateSQL, "Doing", String.valueOf(idOfAssignee));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/currentTaskManager")
    public List<TaskDTO> getTaskListManager(@RequestBody String usernameJson) {
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
            listIdTask = taskMapper.loadTaskIdManager(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadTaskByIdForManager(listIdTask.get(i), userId);
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
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

    @PostMapping("/pendingTaskManager")
    public List<TaskDTO> getPendingListManager(@RequestBody String usernameJson) {
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
            listIdTask = taskMapper.loadTaskIdManager(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
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

    @PostMapping("/suspendTaskManager")
    public List<TaskDTO> getSuspendListManager(@RequestBody String usernameJson) {
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
            listIdTask = taskMapper.loadTaskIdManager(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
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

    @PostMapping("/declineTaskManager")
    public List<TaskDTO> getDeclineListManager(@RequestBody String usernameJson) {
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
            listIdTask = taskMapper.loadTaskIdManager(userId);
            userProfile = userMapper.loadUserProfileByUsername(node.get("username").asText());
            for (int i = 0; i < listIdTask.size(); i++) {
                taskDTO = taskMapper.loadPendingTaskByIdForManager(listIdTask.get(i));
                if (taskDTO == null) {
                    return new ArrayList<>();
                }
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

    @PostMapping("/acceptTask")
    public void acceptTaskPending(@RequestBody AcceptDeclineDTO acceptDeclineDTO) {
        java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        int userWorkFlowId = workFlowMapper.selectUserIdThroughWorkFlow(acceptDeclineDTO.getTaskId());
        int userIdCreation = userMapper.loadIdOfUserByUsername(acceptDeclineDTO.getUserId());
        taskMapper.updateTaskAccept(acceptDeclineDTO.getTaskId(), String.valueOf(userIdCreation));
        workFlowMapper.insertWorkFlowTaskAcceptOrDecline(acceptDeclineDTO.getTaskId(), currentDateSQL, "Doing", String.valueOf(userWorkFlowId));
    }

    @PostMapping("/declineTask")
    public void declineTaskPending(@RequestBody AcceptDeclineDTO acceptDeclineDTO) {
        java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        int userWorkFlowId = workFlowMapper.selectUserIdThroughWorkFlow(acceptDeclineDTO.getTaskId());
        int userIdCreation = userMapper.loadIdOfUserByUsername(acceptDeclineDTO.getUserId());
        taskMapper.updateTaskDecline(acceptDeclineDTO.getTaskId(), String.valueOf(userIdCreation));
        workFlowMapper.insertWorkFlowTaskAcceptOrDecline(acceptDeclineDTO.getTaskId(), currentDateSQL, "Decline", String.valueOf(userWorkFlowId));
    }

    @PostMapping("/pendingTask")
    public List<TaskDTO> loadPendingTaskOfSpecificManager(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        int currentIdUser = 0;
        String groupId = null;
        try {
            node = objectMapper.readTree(username);
            currentIdUser = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            groupId = userMapper.loadGroupIdByUserName(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskMapper.loadPendingTask(String.valueOf(currentIdUser), groupId);
    }

    @PostMapping("/pendingTaskEmployeeInAdmin")
    public List<TaskDTO> loadPendingTaskEmployeeInAdmin() {
        return taskMapper.loadPendingTaskByAdmin();
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
    public void updateTask(@RequestBody SuspendDTO suspendDTO) {
        try {
            taskMapper.suspendTask(suspendDTO.getTaskId());
            int userSuspendId = userMapper.loadIdOfUserByUsername(suspendDTO.getUserName());
            WorkFlowTaskDTO workFlowTaskDTO = workFlowMapper.getCurrentWorkFlowTaskByTaskId(suspendDTO.getTaskId());
            Date assignDate = new SimpleDateFormat("yyyy-MM-dd").parse(workFlowTaskDTO.getAssignTaskDate());
            java.sql.Date assignDateSQL = new java.sql.Date(assignDate.getTime());
            java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            workFlowMapper.insertWorkFlowSuspendTask(suspendDTO.getTaskId(), assignDateSQL, "Suspend", workFlowTaskDTO.getUserSolutionId(),
                    currentDateSQL, String.valueOf(userSuspendId));
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
            workFlowMapper.insertNewWorkFlowTaskForNewEmployee(userTaskIdSpinner.getTaskId(), currentDateSQL, "Doing", String.valueOf(newUserIdTask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/submitTask")
    public void submitTask(@RequestBody InsertNewTaskDTO insertNewTaskDTO) {
        try {
            int userSubmitTask = userMapper.loadIdOfUserByUsername(insertNewTaskDTO.getUsername());
            taskMapper.submitTask(insertNewTaskDTO.getTaskId());
            int userId = userMapper.loadIdOfUserByUsername(insertNewTaskDTO.getUsername());
            TaskDTO taskDTO = taskMapper.loadTaskById(Integer.parseInt(insertNewTaskDTO.getTaskId()), String.valueOf(userId));
            Date assignDate = new SimpleDateFormat("yyyy-MM-dd").parse(taskDTO.getTxtAssignDate());
            java.sql.Date assignDateSQL = new java.sql.Date(assignDate.getTime());
            workFlowMapper.insertSubmitTask(insertNewTaskDTO.getTaskId(), assignDateSQL, "Waiting", String.valueOf(userSubmitTask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/waitingTask")
    public List<TaskDTO> getWaitingTask(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        int currentIdUser = 0;
        String groupId = null;
        try {
            node = objectMapper.readTree(username);
            currentIdUser = userMapper.loadIdOfUserByUsername(node.get("username").asText());
            groupId = userMapper.loadGroupIdByUserName(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskMapper.loadWaitingTask(String.valueOf(currentIdUser), groupId);
    }

    @PostMapping("/waitingTaskAdmin")
    public List<TaskDTO> getWaitingTaskAdmin(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        int currentIdUser = 0;
        String groupId = null;
        try {
            node = objectMapper.readTree(username);
            currentIdUser = userMapper.loadIdOfUserByUsername(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskMapper.loadWaitingTaskByAdmin(String.valueOf(currentIdUser));
    }

    @PostMapping("/finishTask")
    public void finishTask(@RequestBody TaskAcceptDeclineDTO taskAcceptDeclineDTO) {
        taskMapper.finishTask(taskAcceptDeclineDTO.getTaskId());
        TaskForDoneDTO taskForDoneDTO = taskMapper.getTaskForDone(taskAcceptDeclineDTO.getTaskId());
        try {
            Date assignTaskDate = new SimpleDateFormat("yyyy-MM-dd").parse(taskForDoneDTO.getAssignTaskDate());
            java.sql.Date assignTaskDateSQL = new java.sql.Date(assignTaskDate.getTime());
            java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            workFlowMapper.insertTaskDone(taskAcceptDeclineDTO.getTaskId(), assignTaskDateSQL, "Done",
                    taskForDoneDTO.getUserSolutionId(), taskAcceptDeclineDTO.getFeedback(), taskAcceptDeclineDTO.getRate(), currentDateSQL);
            taskMapper.updateTaskInSubmit("Done", taskAcceptDeclineDTO.getTaskId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/declineSubmitTask")
    public void declineTask(@RequestBody TaskAcceptDeclineDTO taskAcceptDeclineDTO) {
        taskMapper.finishTask(taskAcceptDeclineDTO.getTaskId());
        TaskForDoneDTO taskForDoneDTO = taskMapper.getTaskForDone(taskAcceptDeclineDTO.getTaskId());
        try {
            Date assignTaskDate = new SimpleDateFormat("dd-MM-yyyy").parse(taskForDoneDTO.getAssignTaskDate());
            java.sql.Date assignTaskDateSQL = new java.sql.Date(assignTaskDate.getTime());
            java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            workFlowMapper.insertTaskDone(taskAcceptDeclineDTO.getTaskId(), assignTaskDateSQL, "Doing",
                    taskForDoneDTO.getUserSolutionId(), taskAcceptDeclineDTO.getFeedback(), taskAcceptDeclineDTO.getRate(), currentDateSQL);
            taskMapper.updateTaskInSubmit("Doing", taskAcceptDeclineDTO.getTaskId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/reviewedTask")
    public List<TaskDTO> getReviewedTask(@RequestBody String usernameJson) {
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
                if (taskDTO.getStatus().equals("Done") || taskDTO.getStatus().equals("Not Done")) {
                    listTask.add(taskDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listTask;
    }

    @PostMapping("/reviewTask")
    public ReviewDTO getReviewTask(@RequestBody String taskId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        ReviewDTO reviewDTO = null;
        try {
            node = objectMapper.readTree(taskId);
            reviewDTO = workFlowMapper.getReviewedTask(node.get("taskId").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return reviewDTO;
    }
}
