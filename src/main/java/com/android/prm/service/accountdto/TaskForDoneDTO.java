package com.android.prm.service.accountdto;

import java.io.Serializable;

public class TaskForDoneDTO implements Serializable {

    private String assignTaskDate, userSolutionId;

    public TaskForDoneDTO() {
    }

    public TaskForDoneDTO(String assignTaskDate, String userSolutionId) {
        this.assignTaskDate = assignTaskDate;
        this.userSolutionId = userSolutionId;
    }

    public String getAssignTaskDate() {
        return assignTaskDate;
    }

    public void setAssignTaskDate(String assignTaskDate) {
        this.assignTaskDate = assignTaskDate;
    }

    public String getUserSolutionId() {
        return userSolutionId;
    }

    public void setUserSolutionId(String userSolutionId) {
        this.userSolutionId = userSolutionId;
    }
}
