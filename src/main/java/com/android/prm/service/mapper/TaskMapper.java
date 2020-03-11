package com.android.prm.service.mapper;
import com.android.prm.service.accountdto.HistoryTaskDTO;
import com.android.prm.service.accountdto.TaskDTO;
import com.android.prm.service.accountdto.TaskDetailDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface TaskMapper {

    @Select("select distinct t.taskId from WorkFlowTask t right join (select workflowTaskId from UserTask " +
            "where userSolutionId = #{userId}) x on x.workflowTaskId = t.id")
    public List<Integer> loadTaskId(int userId);

    @Select("select top 1 i.txtTaskId, i.txtTaskName, i.txtAssignDate, i.txtStartDate, i.txtEndDate, i.status from UserTask u right join (select w.id as workflowId, t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, w.status as status from WorkFlowTask w left join Task t on w.taskId = t.id where t.id = #{taskId}) i\n" +
            "on u.workflowTaskId = i.workflowId where u.userSolutionId = #{userId} order by i.workflowId desc")
    public TaskDTO loadTaskById(int taskId, String userId);

    @Select("select t.name as taskName, t.descsriptionTask as descriptionTask , t.startDate as startDate, t.endDate as endDate, t.createdDate as createdDate,\n" +
            "a.name as userCreation, t.status as status from Task t left join Account a \n" +
            "on t.userCreationId = a.id where t.id = #{taskId}")
    public TaskDetailDTO loadTaskDetailById(int taskId);

    @Select("select t.name as txtTaskName, y.assignTaskDate as txtAssignDate, y.status as txtStatus, y.name as txtAssignee from\n" +
            "(select * from WorkFlowTask w left join (select u.workflowTaskId, a.name from UserTask u left join Account a on u.userSolutionId = a.id) x\n" +
            "on w.id = x.workflowTaskId where w.taskId = #{taskId}) y left join Task t on y.taskId = t.id order by y.assignTaskDate desc")
    public List<HistoryTaskDTO> loadHistoryOfTask(int taskId);

    @Insert("insert into Task(name, descsriptionTask, startDate, endDate, createdDate, userCreationId, status) " +
            "values (#{name}, #{descriptionTask}, #{startDate}, #{endDate}, #{createdDate}, #{userApprove}, #{status})")
    public void insertNewTask(String name, String descriptionTask, Date startDate, Date endDate, Date createdDate, String userApprove, String status);

    @Select("SELECT TOP 1 id FROM Task ORDER BY id DESC")
    public String selectCurrentRecordTask();

    @Select("select t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, t.status as status\n" +
            " from WorkFlowTask w left join Task t on w.taskId = t.id where t.id = #{taskId} and t.status != 'Pending'")
    public TaskDTO loadTaskByIdForManager(int taskId);

    @Select("select t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, t.status as status\n" +
            " from WorkFlowTask w left join Task t on w.taskId = t.id where t.id = #{taskId}")
    public TaskDTO loadPendingTaskByIdForManager(int taskId);

    @Select("select t.taskId from WorkFlowTask t right join (select workflowTaskId from UserTask " +
            "where userSolutionId = #{userId}) x on x.workflowTaskId = t.id")
    public List<Integer> loadTaskIdManager(int userId);

    @Update("update Task set Task.status = 'Doing' where Task.id = #{taskId}")
    public void updateTaskAccept(String taskId);

    @Update("update Task set Task.status = 'Decline' where Task.id = #{taskId}")
    public void updateTaskDecline(String taskId);

    @Select("select p.txtTaskId, p.txtTaskName, p.txtAssignDate, p.txtStartDate, p.txtEndDate, a.name as txtAssignee from \n" +
            "(select y.txtTaskId, y.txtTaskName, y.txtAssignDate, y.txtStartDate, y.txtEndDate, z.userSolutionId as userId from (select w.id as workflowTaskId, t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate from WorkFlowTask w left join Task t on w.taskId = t.id where t.status = 'Pending' and t.userCreationId = #{userId}) y\n" +
            "left join UserTask z on y.workflowTaskId = z.workflowTaskId) p left join Account a on p.userId = a.id")
    public List<TaskDTO> loadPendingTask(String userId);

    @Update("update Task set name = #{taskName}, descsriptionTask = #{descriptionTask}," +
            " endDate = #{endDate} where id = #{taskId}")
    public void updateTask(String taskName, String descriptionTask, Date endDate, String taskId);

    @Update("update Task set status = 'Suspend' where id = #{taskId}")
    public void suspendTask(String taskId);

    @Update("update Task set name = #{taskName}, descsriptionTask = #{descriptionTask}," +
            " startDate = #{startDate}, endDate = #{endDate} where id = #{taskId}")
    public void updatePendingTask(String taskName, String descriptionTask, Date startDate, Date endDate, String taskId);
}
