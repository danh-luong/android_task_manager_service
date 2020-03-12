package com.android.prm.service.mapper;

import com.android.prm.service.accountdto.EmployeeDTO;
import com.android.prm.service.accountdto.UserDTO;
import com.android.prm.service.accountdto.UserInfoAdminDTO;
import com.android.prm.service.model.request.AccountRequest;
import com.android.prm.service.model.request.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Select("SELECT id, username, password, roleId from Account where username = #{username}")
    public AccountRequest loadUserByUsername(String username);

    @Select("SELECT name, email, phone, address from Account where username = #{username}")
    public UserProfile loadUserProfileByUsername(String username);

    @Select("select x.status, x.id, x.name, x.email, x.phone, x.address, x.description as roleName, g.name as groupName from GroupDetail g right join \n" +
            "(SELECT a.status, a.username, a.groupId, a.id, a.name, a.email, a.phone, a.address, r.description from Account a left join Role r on a.roleId = r.id) x\n" +
            "on g.id = x.groupId where x.username = #{username}")
    public UserInfoAdminDTO loadUserProfileByUsernameFromAdmin(String username);

    @Select("select id from Account where username = #{username}")
    public int loadIdOfUserByUsername(String username);

    @Select("SELECT id, name, email, phone, address, groupId, roleId, username from Account where username = #{username}")
    public UserDTO loadProfileUserWithGroupId(String username);

    @Select("select id, username, name, email, phone, address, groupId, roleId from Account where username != #{username}")
    public List<UserDTO> loadUserByGroupOfManager(String groupId);

    @Select("select id, username, name, email, phone, address, groupId, roleId from Account where username != #{username}")
    public List<UserDTO> loadUserByGroupOfAdmin(String username);

    @Select("select * from Account where groupId = (select groupId from Account where username = #{username}) and roleId = 3")
    public UserDTO loadUserManagerByEmployee(String username);

    @Select("select * from Account where groupId = (select groupId from Account where username = #{username}) and roleId = 1")
    public UserDTO loadUserAdminByManager(String username);

    @Select("select * from Account where username = #{username}")
    public UserDTO loadIdOfAdmin(String username);

    @Select("select id, username, name, phone, email, roleId from Account where groupId = (select groupId from Account where username = #{username}) and username != #{username} and roleId != 1")
    public List<EmployeeDTO> loadCurrentEmployeeOfGroup(String username);

    @Select("select id, username, name, phone, email, roleId from Account where username != #{username}")
    public List<EmployeeDTO> loadCurrentEmployeeByAdmin(String username);

    @Select("select top 1 u.userSolutionId from (select w.id from (select * from Task where id = #{taskId}) x left join WorkFlowTask w on x.id = w.taskId) a\n" +
            "left join UserTask u on a.id = u.workflowTaskId order by u.id desc")
    public int getIdOfUserDoingCurrentTaskWithTaskId(String taskId);

    @Select("select groupId from Account where username = #{username}")
    public String loadGroupIdByUserName(String username);

    @Select("update Account set status = 'active' where username = #{username}")
    public void activeUser(String username);

    @Select("update Account set status = 'deactive' where username = #{username}")
    public void deactiveUser(String username);
}
