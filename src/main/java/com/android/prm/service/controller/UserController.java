package com.android.prm.service.controller;

import com.android.prm.service.accountdto.EmployeeDTO;
import com.android.prm.service.accountdto.TaskCreatedDTO;
import com.android.prm.service.accountdto.UserDTO;
import com.android.prm.service.accountdto.UserTaskIdSpinner;
import com.android.prm.service.mapper.UserMapper;
import com.android.prm.service.model.request.AccountRequest;
import com.android.prm.service.model.request.UserProfile;
import com.android.prm.service.model.response.AccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
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

    @PostMapping("/loadEmployeeByGroupInAssigningTask")
    public List<UserDTO> loadEmployeeByGroupInAssigningTask(@RequestBody UserTaskIdSpinner userTaskIdSpinner) {
        List<UserDTO> templistUser;
        List<UserDTO> listUser = new ArrayList<>();
        try {
            UserDTO userDTO = userMapper.loadProfileUserWithGroupId(userTaskIdSpinner.getUsername());
            templistUser = userMapper.loadUserByGroupOfManager(userDTO.getGroupId());
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
}
