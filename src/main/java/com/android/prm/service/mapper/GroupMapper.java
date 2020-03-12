package com.android.prm.service.mapper;

import com.android.prm.service.accountdto.GroupDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}
