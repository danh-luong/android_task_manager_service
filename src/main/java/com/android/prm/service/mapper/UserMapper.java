package com.android.prm.service.mapper;

import com.android.prm.service.accountdto.EmployeeDTO;
import com.android.prm.service.accountdto.UserDTO;
import com.android.prm.service.accountdto.UserInfoAdminDTO;
import com.android.prm.service.model.request.AccountRequest;
import com.android.prm.service.model.request.UserProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.sql.Date;
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

    @Select("select id, username, name, email, phone, address, groupId, roleId from Account where groupId = #{groupId}")
    public List<UserDTO> loadUserByGroupOfManager(String groupId);

    @Select("select id, username, name, email, phone, address, groupId, roleId from Account where username != #{username} and groupId= #{groupId}")
    public List<UserDTO> loadUserByGroupOfAdmin(String username, String groupId);

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

    @Select("select top 1 w.userSolutionId from WorkFlowTask w where taskId = #{taskId} order by w.id desc")
    public int getIdOfUserDoingCurrentTaskWithTaskId(String taskId);

    @Select("select groupId from Account where username = #{username}")
    public String loadGroupIdByUserName(String username);

    @Select("update Account set status = 'active' where username = #{username}")
    public void activeUser(String username);

    @Select("update Account set status = 'deactive' where username = #{username}")
    public void deactiveUser(String username);

    @Update("update Account set name = #{username}, phone = #{phone}, email = #{email}, address = #{address} where id = #{userId}")
    public void updateUserInfo(String username, String phone, String email, String address, String userId);

    @Insert("insert into UpdateDetail(userUpdatedId, updatedUserId, createdDate) values (#{userUpdatedId}, #{updatedUserId}, #{createdDate})")
    public void insertUpdateDetail(String userUpdatedId, String  updatedUserId, Date createdDate);

    @Select("select top 1 a.groupId from Account a right join \n" +
            "(select w.id, w.userSolutionId from Task t left join WorkFlowTask w on t.id = w.taskId where t.id = 1) x\n" +
            "on a.id = x.userSolutionId order by x.id desc")
    public String selectGroupIdFromTaskId(String taskId);
}
