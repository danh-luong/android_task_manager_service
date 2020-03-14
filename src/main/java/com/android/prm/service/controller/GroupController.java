package com.android.prm.service.controller;

import com.android.prm.service.accountdto.AcceptDeclineDTO;
import com.android.prm.service.accountdto.EmployeeDTO;
import com.android.prm.service.accountdto.GroupDTO;
import com.android.prm.service.accountdto.GroupUserDTO;
import com.android.prm.service.mapper.GroupMapper;
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
public class GroupController implements Serializable {

    @Autowired
    private GroupMapper groupMapper;

    @PostMapping("/getCurrentGroup")
    public List<GroupDTO> getCurrentGroup() {
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
    public void addAvailableEmployeeToGroup(@RequestBody AcceptDeclineDTO acceptDeclineDTO) {
        groupMapper.addAvailableEmployeeToGroup(acceptDeclineDTO.getTaskId(), acceptDeclineDTO.getUserId());
    }
}
