package com.xxx.mapper;

import com.xxx.base.BaseMapper;
import com.xxx.bean.Menu;
import com.xxx.dto.MenuDto;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu,Integer> {

    List<MenuDto> selectAllMenus();
}