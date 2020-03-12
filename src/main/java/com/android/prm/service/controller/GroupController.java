package com.android.prm.service.controller;

import com.android.prm.service.accountdto.GroupDTO;
import com.android.prm.service.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
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
}
