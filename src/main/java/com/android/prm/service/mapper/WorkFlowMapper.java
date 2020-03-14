package com.android.prm.service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Mapper
@Component
public interface WorkFlowMapper {

    @Insert("Insert into WorkFlowTask(taskId, assignTaskDate, status, userSolutionId) values (#{taskId}, #{currentDate}, #{status}, #{userSolutionId})")
    public void createNewWorkFlowTaskFromTask(String taskId, Date currentDate, String status, String userSolutionId);

    @Select("SELECT TOP 1 id FROM WorkFlowTask ORDER BY id DESC")
    public String getCurrentRecordWorkFlowTask();

    //Don't use
    @Insert("Insert into UserTask(userSolutionId, workflowTaskId) values (#{assignee}, #{workFlowtaskId})")
    public void insertUserTask(String assignee, String workFlowtaskId);

    @Insert("insert into WorkFlowTask(taskId, assignTaskDate, status, userSolutionId) values (#{taskId}, #{currentDate}, #{status}, #{userSolutionId})")
    public void insertWorkFlowTaskAcceptOrDecline(String taskId, Date currentDate, String status, String userSolutionId);

    @Select("select userSolutionId from WorkFlowTask where taskId = #{taskId} and status = 'Pending'")
    public int selectUserIdThroughWorkFlow(String taskId);

    @Select("select top 1 id from WorkFlowTask where taskId = #{taskId} order by id desc")
    public int getCurrentWorkFlowOfTask(String taksId);

    @Update("update WorkFlowTask set endDate = #{currentDate}, status = 'Not Done' where id = #{workflowId}")
    public void updateTaskNotDoneForCurrentUserDoing(String workflowId, Date currentDate);

    @Insert("insert into WorkFlowTask(taskId, assignTaskDate, status, userSolutionId) values (#{taskId}, #{assignDate}, #{status}, #{assignedUser})")
    public void insertNewWorkFlowTaskForNewEmployee(String taskId, Date assignDate, String status, String assignedUser);
}
