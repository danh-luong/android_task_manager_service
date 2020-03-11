package com.android.prm.service.accountdto;

import java.io.Serializable;

public class TaskDTO implements Serializable {

    private String txtTaskId, txtTaskName, txtAssignDate, txtStartDate, txtEndDate, txtAssignee, status;

    public TaskDTO() {
    }

    public TaskDTO(String txtTaskId, String txtTaskName, String txtAssignDate, String txtStartDate, String txtEndDate, String txtAssignee, String status) {
        this.txtTaskId = txtTaskId;
        this.txtTaskName = txtTaskName;
        this.txtAssignDate = txtAssignDate;
        this.txtStartDate = txtStartDate;
        this.txtEndDate = txtEndDate;
        this.txtAssignee = txtAssignee;
        this.status = status;
    }

    public String getTxtTaskName() {
        return txtTaskName;
    }

    public void setTxtTaskName(String txtTaskName) {
        this.txtTaskName = txtTaskName;
    }

    public String getTxtAssignDate() {
        return txtAssignDate;
    }

    public void setTxtAssignDate(String txtAssignDate) {
        this.txtAssignDate = txtAssignDate;
    }

    public String getTxtStartDate() {
        return txtStartDate;
    }

    public void setTxtStartDate(String txtStartDate) {
        this.txtStartDate = txtStartDate;
    }

    public String getTxtEndDate() {
        return txtEndDate;
    }

    public void setTxtEndDate(String txtEndDate) {
        this.txtEndDate = txtEndDate;
    }

    public String getTxtAssignee() {
        return txtAssignee;
    }

    public String getTxtTaskId() {
        return txtTaskId;
    }

    public void setTxtTaskId(String txtTaskId) {
        this.txtTaskId = txtTaskId;
    }

    public void setTxtAssignee(String txtAssignee) {
        this.txtAssignee = txtAssignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

