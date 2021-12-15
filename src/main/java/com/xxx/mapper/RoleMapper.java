package com.xxx.mapper;

import com.xxx.base.BaseMapper;
import com.xxx.bean.Role;
import com.xxx.query.RoleQuery;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    List<Role> queryRolesByNameAndDesc(RoleQuery roleQuery);


    Role selectRoleByName(String rolename);

    int deleteRoleById(Integer roleId);

    @MapKey("")
    List<Map<String, Object>> queryAllRoles();
}