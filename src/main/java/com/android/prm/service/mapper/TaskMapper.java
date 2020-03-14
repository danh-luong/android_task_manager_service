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

    @Select("select distinct taskId from WorkFlowTask where userSolutionId = #{userId}")
    public List<Integer> loadTaskId(int userId);

    @Select("select top 1 t.id as txtTaskId, t.name as txtTaskName, i.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            " t.endDate as txtEndDate, i.status from Task t left join \n" +
            "(select w.id, w.taskId, w.assignTaskDate, w.status from WorkFlowTask w where w.userSolutionId = #{userId}) i on t.id = i.taskId where t.id = #{taskId} order by i.id desc")
    public TaskDTO loadTaskById(int taskId, String userId);

    @Select("select t.name as taskName, t.descsriptionTask as descriptionTask , t.startDate as startDate, t.endDate as endDate, t.createdDate as createdDate,\n" +
            "a.name as userCreation, t.status as status from Task t left join Account a \n" +
            "on t.userCreationId = a.id where t.id = #{taskId}")
    public TaskDetailDTO loadTaskDetailById(int taskId);

    @Select("select t.name as txtTaskName, y.assignTaskDate as txtAssignDate, y.status as txtStatus, y.name as txtAssignee from\n" +
            "(select w.id, w.status, w.assignTaskDate ,w.taskId, w.id as workflowTaskId, a.name from WorkFlowTask w left join Account a on w.userSolutionId = a.id\n" +
            " where w.taskId = #{taskId}) y left join Task t on y.taskId = t.id order by y.id desc")
    public List<HistoryTaskDTO> loadHistoryOfTask(int taskId);

    @Insert("insert into Task(name, descsriptionTask, startDate, endDate, createdDate, userCreationId, status) " +
            "values (#{name}, #{descriptionTask}, #{startDate}, #{endDate}, #{createdDate}, #{userApprove}, #{status})")
    public void insertNewTask(String name, String descriptionTask, Date startDate, Date endDate, Date createdDate, String userApprove, String status);

    @Select("SELECT TOP 1 id FROM Task ORDER BY id DESC")
    public String selectCurrentRecordTask();

    @Select("select top 1 t.id as txtTaskId, t.name as txtTaskName, x.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, x.status as status from (select distinct t.taskId, t.id, t.status, t.assignTaskDate \n" +
            "from WorkFlowTask t where t.userSolutionId = #{userSolutionId}) x\n" +
            "left join Task t on x.taskId = t.id where t.id = #{taskId} order by x.id desc")
    public TaskDTO loadTaskByIdForManager(int taskId, int userSolutionId);

    @Select("select top 1 t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, t.status as status\n" +
            " from WorkFlowTask w left join Task t on w.taskId = t.id where t.id = #{taskId} order by w.id desc")
    public TaskDTO loadPendingTaskByIdForManager(int taskId);

    @Select("select distinct t.taskId from WorkFlowTask t where t.userSolutionId = #{userId}")
    public List<Integer> loadTaskIdManager(int userId);

    @Update("update Task set Task.status = 'Doing', Task.userCreationId = #{userId} where Task.id = #{taskId}")
    public void updateTaskAccept(String taskId, String userId);

    @Update("update Task set Task.status = 'Decline', Task.userCreationId = #{userId} where Task.id = #{taskId}")
    public void updateTaskDecline(String taskId, String userId);

    @Select("select p.txtTaskId, p.txtTaskName, p.txtAssignDate, p.txtStartDate, p.txtEndDate, a.name as txtAssignee from \n" +
            "(select t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, w.userSolutionId from WorkFlowTask w left join Task t on w.taskId = t.id where t.status = 'Pending')\n" +
            " p left join Account a on p.userSolutionId = a.id where p.userSolutionId != #{userId} and a.groupId = #{groupId}")
    public List<TaskDTO> loadPendingTask(String userId, String groupId);

    @Select("select p.txtTaskId, p.txtTaskName, p.txtAssignDate, p.txtStartDate, p.txtEndDate, a.name as txtAssignee from\n" +
            "(select w.id as workflowTaskId, t.id as txtTaskId, t.name as txtTaskName, w.assignTaskDate as txtAssignDate, t.startDate as txtStartDate,\n" +
            "t.endDate as txtEndDate, w.userSolutionId from WorkFlowTask w left join Task t on w.taskId = t.id where t.status = 'Pending') p left join Account a on p.userSolutionId = a.id")
    public List<TaskDTO> loadPendingTaskByAdmin();

    @Update("update Task set name = #{taskName}, descsriptionTask = #{descriptionTask}," +
            " endDate = #{endDate} where id = #{taskId}")
    public void updateTask(String taskName, String descriptionTask, Date endDate, String taskId);

    @Update("update Task set status = 'Suspend' where id = #{taskId}")
    public void suspendTask(String taskId);

    @Update("update Task set name = #{taskName}, descsriptionTask = #{descriptionTask}," +
            " startDate = #{startDate}, endDate = #{endDate} where id = #{taskId}")
    public void updatePendingTask(String taskName, String descriptionTask, Date startDate, Date endDate, String taskId);
}
