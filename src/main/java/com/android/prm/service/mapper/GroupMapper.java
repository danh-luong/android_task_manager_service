package com.android.prm.service.mapper;

import com.android.prm.service.accountdto.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GroupMapper {

    @Select("select g.id as groupId, g.name as groupName, g.description as groupDescription, a.name as managerName from GroupDetail g left join Account a \n" +
            "on g.managerId = a.id")
    public List<GroupDTO> getCurrentGroup();

    @Insert("insert into GroupDetail(name, description) values (#{groupName}, #{descriptionGroup})")
    public void createNewGroupDetail(String groupName, String descriptionGroup);

    @Select("select x.groupId, x.employeeId, x.employeeName, x.roleId, r.description as roleName from Role r right join \n" +
            "(select b.id as groupId, a.id as employeeId, a.name as employeeName, a.roleId from Account a \n" +
            "right join (select * from GroupDetail where id = #{taskId}) b on a.groupId = b.id) x on r.id = x.roleId")
    public List<GroupUserDTO> getCurrentUserInGroup(String taskId);

    @Select("select id, username, name, phone, email, roleId from Account where roleId IS NULL")
    public List<EmployeeDTO> getAvailableEmployee();

    @Update("Update Account set groupId = #{groupId}, roleId = #{roleId} where id = #{userId}")
    public void addAvailableEmployeeToGroup(String groupId, String userId, String roleId);

    @Select("select id as groupId, name from GroupDetail")
    public List<GroupSpnDTO> getListCurrentGroup();

    @Select("select id as roleId, description as roleName from Role where id != 1")
    public List<RoleSpnDTO> getListCurrentRole();

    @Select("select distinct roleId from Account where groupId = #{groupId} and roleId = 3")
    public int isCurrentHasManger(String groupId);

    @Update("update Account set roleId = 2 where id = #{managerId}")
    public void deroleFromManagerToEmployee(String managerId);

    @Update("update GroupDetail set managerId = #{managerId} where id = #{groupId}")
    public void setNewManagerToGroup(String managerId, String groupId);
}
