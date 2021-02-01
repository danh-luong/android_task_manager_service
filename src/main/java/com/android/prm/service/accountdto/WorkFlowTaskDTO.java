package com.android.prm.service.accountdto;

public class WorkFlowTaskDTO {

    private String id, taskId, assignTaskDate, status, feedback, rate, createdFeedbackDate, endDate, userSolutionId, suspendDate, userSuspendId;

    public WorkFlowTaskDTO() {
    }

    public WorkFlowTaskDTO(String taskId, String assignTaskDate, String userSolutionId) {
        this.taskId = taskId;
        this.assignTaskDate = assignTaskDate;
        this.userSolutionId = userSolutionId;
    }

    public WorkFlowTaskDTO(String id, String taskId, String assignTaskDate, String status, String feedback, String rate, String createdFeedbackDate, String endDate, String userSolutionId, String suspendDate) {
        this.id = id;
        this.taskId = taskId;
        this.assignTaskDate = assignTaskDate;
        this.status = status;
        this.feedback = feedback;
        this.rate = rate;
        this.createdFeedbackDate = createdFeedbackDate;
        this.endDate = endDate;
        this.userSolutionId = userSolutionId;
        this.suspendDate = suspendDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAssignTaskDate() {
        return assignTaskDate;
    }

    public void setAssignTaskDate(String assignDate) {
        this.assignTaskDate = assignDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCreatedFeedbackDate() {
        return createdFeedbackDate;
    }

    public void setCreatedFeedbackDate(String createdFeedbackDate) {
        this.createdFeedbackDate = createdFeedbackDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserSolutionId() {
        return userSolutionId;
    }

    public void setUserSolutionId(String userSolutionId) {
        this.userSolutionId = userSolutionId;
    }

    public String getSuspendDate() {
        return suspendDate;
    }

    public void setSuspendDate(String suspendDate) {
        this.suspendDate = suspendDate;
    }
}
