package com.android.prm.service.controller;

import com.android.prm.service.accountdto.*;
import com.android.prm.service.mapper.UserMapper;
import com.android.prm.service.model.request.UserProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class UserController implements Serializable {

    public static UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/profile")
    public UserProfile users(@RequestBody String usernameJson){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(usernameJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userMapper.loadUserProfileByUsername(node.get("username").asText());
    }

    @PostMapping("/loadUserDetailFromAdmin")
    public UserInfoAdminDTO loadUserDetailFromAdmin(@RequestBody String usernameJson){
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
            listUser = userMapper.loadUserByGroupOfManager(userDTO.getUsername());
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
        List<UserDTO> listUser = null;
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
}
