package com.android.prm.service.accountdto;

import java.io.Serializable;

public class TaskCreatedDTO implements Serializable {

    private String id, name, descriptionTask, startDate, endDate, createdDate, userCreationName, userAssignee, status;

    public TaskCreatedDTO() {
    }

    public TaskCreatedDTO(String id, String name, String descriptionTask, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.descriptionTask = descriptionTask;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskCreatedDTO(String name, String descriptionTask, String startDate, String endDate,
                          String createdDate, String userCreationName, String status, String userAssignee) {
        this.name = name;
        this.descriptionTask = descriptionTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.userCreationName = userCreationName;
        this.status = status;
        this.userAssignee = userAssignee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public TaskCreatedDTO(String endDate, String userAssignee) {
        this.endDate = endDate;
        this.userAssignee = userAssignee;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserCreationName() {
        return userCreationName;
    }

    public void setUserCreationName(String userCreationName) {
        this.userCreationName = userCreationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserAssignee() {
        return userAssignee;
    }

    public void setUserAssignee(String userAssignee) {
        this.userAssignee = userAssignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
