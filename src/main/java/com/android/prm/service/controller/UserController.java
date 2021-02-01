package com.android.prm.service.controller;

import com.android.prm.service.accountdto.*;
import com.android.prm.service.mapper.GroupMapper;
import com.android.prm.service.mapper.UserMapper;
import com.android.prm.service.model.request.UserProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class UserController implements Serializable {

    public static UserMapper userMapper;

    @Autowired
    public GroupMapper groupMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/profile")
    public UserProfileForScanQR users(@RequestBody String usernameJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userMapper.loadUserProfileByUsernameForScanQR(node.get("username").asText());
    }

    @PostMapping("/loadUserDetailFromAdmin")
    public UserInfoAdminDTO loadUserDetailFromAdmin(@RequestBody String usernameJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userMapper.loadUserProfileByUsernameFromAdmin(node.get("username").asText());
    }

    @PostMapping("/loadEmployeeByGroup")
    public List<UserDTO> loadUserByGroup(@RequestBody String usernameJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> listUser = null;
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            UserDTO userDTO = userMapper.loadProfileUserWithGroupId(node.get("username").asText());
            listUser = userMapper.loadUserByGroupOfManager(userDTO.getGroupId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listUser;
    }

    @PostMapping("/loadEmployeeByGroupAdmin")
    public List<UserDTO> loadEmployeeByGroupAdmin(@RequestBody String usernameJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> listUser = null;
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
            UserDTO userDTO = userMapper.loadProfileUserWithGroupId(node.get("username").asText());
            listUser = userMapper.loadUserByGroupOfManager(userDTO.getGroupId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listUser;
    }

    @PostMapping("/currentEmployee")
    public List<EmployeeDTO> loadCurrentEmployeeOfGroup(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> listUser = null;
        JsonNode node = null;
        try {
            node = objectMapper.readTree(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMapper.loadCurrentEmployeeOfGroup(node.get("username").asText());
    }

    @PostMapping("/currentEmployeeAdmin")
    public List<EmployeeDTO> loadCurrentEmployeeByAdmin(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> listUser = null;
        JsonNode node = null;
        try {
            node = objectMapper.readTree(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMapper.loadCurrentEmployeeByAdmin(node.get("username").asText());
    }

    @PostMapping("/loadEmployeeByGroupInAssigningTask")
    public List<UserDTO> loadEmployeeByGroupInAssigningTask(@RequestBody UserTaskIdSpinner userTaskIdSpinner) {
        List<UserDTO> templistUser;
        List<UserDTO> listUser = new ArrayList<>();
        try {
            UserDTO userDTO = userMapper.loadProfileUserWithGroupId(userTaskIdSpinner.getUsername());
            if (!userDTO.getRoleId().equals("1")) {
                templistUser = userMapper.loadUserByGroupOfManager(userDTO.getGroupId());
            } else {
                String groupId = userMapper.selectGroupIdFromTaskId(userTaskIdSpinner.getTaskId());
                templistUser = userMapper.loadUserByGroupOfAdmin(userTaskIdSpinner.getUsername(), groupId);
            }
            int id = userMapper.getIdOfUserDoingCurrentTaskWithTaskId(userTaskIdSpinner.getTaskId());
            for (int i = 0; i < templistUser.size(); i++) {
                if (Integer.parseInt(templistUser.get(i).getId()) != id && !templistUser.get(i).getUsername().equals(userTaskIdSpinner.getUsername())) {
                    if (Integer.parseInt(templistUser.get(i).getRoleId()) != 1) {
                        listUser.add(templistUser.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listUser;
    }

    @PostMapping("/deactiveUser")
    public void deactiveUser(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> listUser = null;
        JsonNode node = null;
        try {
            node = objectMapper.readTree(username);
            userMapper.deactiveUser(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/activeUser")
    public void activeUser(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(username);
            userMapper.activeUser(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/updateUserInfo")
    public void updateUserInfo(@RequestBody UpdatedUserInfoDTO updatedUserInfoDTO) {
        userMapper.updateUserInfo(updatedUserInfoDTO.getName(), updatedUserInfoDTO.getPhone(), updatedUserInfoDTO.getEmail(),
                updatedUserInfoDTO.getAddress(), updatedUserInfoDTO.getId());
        int userUpdateId = userMapper.loadIdOfUserByUsername(updatedUserInfoDTO.getUserUpdated());
        java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        userMapper.insertUpdateDetail(updatedUserInfoDTO.getId(), String.valueOf(userUpdateId), currentDateSQL);
    }

    @PostMapping("/spnGroupDTO")
    public List<GroupSpnDTO> getCurrentGroup() {
        List<GroupSpnDTO> groupDTOList = null;
        groupDTOList = groupMapper.getListCurrentGroup();
        return groupDTOList;
    }

    @PostMapping("/spnRoleDTO")
    public List<RoleSpnDTO> getCurrentRole() {
        List<RoleSpnDTO> roleDTOList = null;
        roleDTOList = groupMapper.getListCurrentRole();
        return roleDTOList;
    }

    @PostMapping("/createNewEmployee")
    public void createNewEmployee(@RequestBody NewEmployeeDTO newEmployeeDTO, HttpServletResponse httpServletResponse) {
        java.sql.Date currentDateSQL = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        List<String> usernameList = userMapper.getAllCurrentUserName();
        for (int i = 0; i < usernameList.size(); i++) {
            if (usernameList.get(i).equalsIgnoreCase(newEmployeeDTO.getTxtUserName())) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        if (newEmployeeDTO.getTxtGroupId() != null) {
            if (Integer.parseInt(newEmployeeDTO.getTxtRoleId()) == 3) {
                int isCurrentManager = 0;
                try {
                    isCurrentManager = groupMapper.isCurrentHasManger(newEmployeeDTO.getTxtGroupId());
                } catch (Exception e) {
                    isCurrentManager = -1;
                }
                if (isCurrentManager == 3) {
                    String managerId = userMapper.getManagerIdByGroupId(newEmployeeDTO.getTxtGroupId());
                    groupMapper.deroleFromManagerToEmployee(managerId);
                    userMapper.createNewEmployee(newEmployeeDTO.getTxtUserName(), bCryptPasswordEncoder.encode(newEmployeeDTO.getTxtPassword()),
                            currentDateSQL, newEmployeeDTO.getTxtStatus(), newEmployeeDTO.getTxtName(), newEmployeeDTO.getTxtEmail(),
                            newEmployeeDTO.getTxtAddress(), newEmployeeDTO.getTxtPhone(), newEmployeeDTO.getTxtRoleId(), newEmployeeDTO.getTxtGroupId());
                    int idOfInsertedCurrentUser = userMapper.getIdOfInsertedCurrentUser();
                    groupMapper.setNewManagerToGroup(String.valueOf(idOfInsertedCurrentUser), newEmployeeDTO.getTxtGroupId());
                } else {
                    userMapper.createNewEmployee(newEmployeeDTO.getTxtUserName(), bCryptPasswordEncoder.encode(newEmployeeDTO.getTxtPassword()),
                            currentDateSQL, newEmployeeDTO.getTxtStatus(), newEmployeeDTO.getTxtName(), newEmployeeDTO.getTxtEmail(),
                            newEmployeeDTO.getTxtAddress(), newEmployeeDTO.getTxtPhone(), newEmployeeDTO.getTxtRoleId(), newEmployeeDTO.getTxtGroupId());
                    int idOfInsertedCurrentUser = userMapper.getIdOfInsertedCurrentUser();
                    groupMapper.setNewManagerToGroup(String.valueOf(idOfInsertedCurrentUser), newEmployeeDTO.getTxtGroupId());
                }
            } else {
                userMapper.createNewEmployee(newEmployeeDTO.getTxtUserName(), bCryptPasswordEncoder.encode(newEmployeeDTO.getTxtPassword()),
                        currentDateSQL, newEmployeeDTO.getTxtStatus(), newEmployeeDTO.getTxtName(), newEmployeeDTO.getTxtEmail(),
                        newEmployeeDTO.getTxtAddress(), newEmployeeDTO.getTxtPhone(), newEmployeeDTO.getTxtRoleId(), newEmployeeDTO.getTxtGroupId());
            }
        } else {
            userMapper.createNewEmployee(newEmployeeDTO.getTxtUserName(), bCryptPasswordEncoder.encode(newEmployeeDTO.getTxtPassword()),
                    currentDateSQL, newEmployeeDTO.getTxtStatus(), newEmployeeDTO.getTxtName(), newEmployeeDTO.getTxtEmail(),
                    newEmployeeDTO.getTxtAddress(), newEmployeeDTO.getTxtPhone(), newEmployeeDTO.getTxtRoleId(), newEmployeeDTO.getTxtGroupId());
        }
    }

    @PostMapping("/profileWithIdManager")
    public UserProfile getUserProfileWithId(@RequestBody ScanDTO scanDTO, HttpServletResponse httpServletResponse) {
        UserProfile employeeProfile = null;
        try {
            UserDTO managerProfile = userMapper.loadProfileUserWithGroupId(scanDTO.getUserNameManager());
            employeeProfile = userMapper.loadUserProfileByUserId(scanDTO.getEmployeeId(), managerProfile.getGroupId());
            if (employeeProfile == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeProfile;
    }

    @PostMapping("/profileWithAdmin")
    public UserProfile getUserProfileWithAdmin(@RequestBody String userId, HttpServletResponse httpServletResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        UserProfile employeeProfile = null;
        try {
            node = objectMapper.readTree(userId);
            employeeProfile = userMapper.loadUserProfileByUserIdWithAdmin(node.get("userId").asText());
            if (employeeProfile == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeProfile;
    }
}
