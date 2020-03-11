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

    @Insert("Insert into WorkFlowTask(taskId, assignTaskDate, status) values (#{taskId}, #{currentDate}, #{status})")
    public void createNewWorkFlowTaskFromTask(String taskId, Date currentDate, String status);

    @Select("SELECT TOP 1 id FROM WorkFlowTask ORDER BY id DESC")
    public String getCurrentRecordWorkFlowTask();

    @Insert("Insert into UserTask(userSolutionId, workflowTaskId) values (#{assignee}, #{workFlowtaskId})")
    public void insertUserTask(String assignee, String workFlowtaskId);

    @Insert("insert into WorkFlowTask(taskId, assignTaskDate, status) values (#{taskId}, #{currentDate}, #{status})")
    public void insertWorkFlowTaskAcceptOrDecline(String taskId, Date currentDate, String status);

    @Select("select u.userSolutionId from UserTask u right join (select id from WorkFlowTask where taskId = #{taskId} and status = 'Pending') x on \n" +
            "u.workflowTaskId  = x.id")
    public int selectUserIdThroughWorkFlow(String taskId);

    @Select("select top 1 id from WorkFlowTask where taskId = #{taskId} order by id desc")
    public int getCurrentWorkFlowOfTask(String taksId);

    @Update("update WorkFlowTask set endDate = #{currentDate}, status = 'Not Done' where id = #{workflowId}")
    public void updateTaskNotDoneForCurrentUserDoing(String workflowId, Date currentDate);

    @Insert("insert into WorkFlowTask(taskId, assignTaskDate, status) values (#{taskId}, #{assignDate}, #{status})")
    public void insertNewWorkFlowTaskForNewEmployee(String taskId, Date assignDate, String status);
}
