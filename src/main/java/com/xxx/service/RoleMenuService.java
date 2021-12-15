package com.xxx.service;

import com.xxx.base.BaseService;
import com.xxx.bean.RoleMenu;
import com.xxx.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class RoleMenuService extends BaseService<RoleMenu,Integer> {

    @Resource
    private RoleMenuMapper roleMenuMapper;
}
