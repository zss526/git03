package com.xxx.mapper;

import com.xxx.base.BaseMapper;
import com.xxx.bean.RoleMenu;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu,Integer> {

    int countMenusByRoleId(Integer roleId);

    int deleteMenuByRoleId(Integer roleId);

    List<Integer> queryRoleHasMenusByRoleId(Integer roleId);
}