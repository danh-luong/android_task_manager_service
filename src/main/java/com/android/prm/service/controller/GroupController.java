package com.android.prm.service.controller;

import com.android.prm.service.accountdto.*;
import com.android.prm.service.mapper.GroupMapper;
import com.android.prm.service.mapper.UserMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GroupController implements Serializable {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/getCurrentGroup")
    public List<GroupDTO> getCurrentGroup(@RequestBody String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        UserDTO userDTO = null;
        try {
            node = objectMapper.readTree(username);
            userDTO = userMapper.loadProfileUserWithGroupId(node.get("username").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupMapper.getCurrentGroup();
    }

    @PostMapping("/createNewGroup")
    public void createNewGroup(@RequestBody GroupDTO groupDTO) {
        groupMapper.createNewGroupDetail(groupDTO.getGroupName(), groupDTO.getGroupDescription());
    }

    @PostMapping("/getCurrentEmployeeInGroup")
    public List<GroupUserDTO> getCurrentEmployeeInGroup(@RequestBody String groupIdJson) {
        List<GroupUserDTO> list = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(groupIdJson);
            list = groupMapper.getCurrentUserInGroup(node.get("groupId").asText());
            if (list.get(0).getEmployeeId() == null) {
                list = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @PostMapping("/getAvailableEmployee")
    public List<EmployeeDTO> getAvailableEmployee() {
        return groupMapper.getAvailableEmployee();
    }

    @PostMapping("/addEmployeeToSpecificGroup")
    public void addAvailableEmployeeToGroup(@RequestBody AssignEmployeeDTO assignEmployeeDTO, HttpServletResponse response) {
        int isCurrentHasManager = 0;
        if (Integer.parseInt(assignEmployeeDTO.getRoleId()) == 3) {
            try {
                isCurrentHasManager = groupMapper.isCurrentHasManger(assignEmployeeDTO.getGroupId());
            } catch (Exception e) {
                isCurrentHasManager = -1;
            }
            if (isCurrentHasManager == 3) {
                String managerId = userMapper.getManagerIdByGroupId(assignEmployeeDTO.getGroupId());
                groupMapper.deroleFromManagerToEmployee(managerId);
                groupMapper.addAvailableEmployeeToGroup(assignEmployeeDTO.getGroupId(), assignEmployeeDTO.getUserId(), assignEmployeeDTO.getRoleId());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                groupMapper.addAvailableEmployeeToGroup(assignEmployeeDTO.getGroupId(), assignEmployeeDTO.getUserId(), assignEmployeeDTO.getRoleId());
            }
            groupMapper.setNewManagerToGroup(assignEmployeeDTO.getUserId(), assignEmployeeDTO.getGroupId());
        } else {
            groupMapper.addAvailableEmployeeToGroup(assignEmployeeDTO.getGroupId(), assignEmployeeDTO.getUserId(), assignEmployeeDTO.getRoleId());
        }
    }
}
