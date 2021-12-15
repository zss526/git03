package com.xxx.service;

import com.xxx.base.BaseService;
import com.xxx.bean.Menu;
import com.xxx.dto.MenuDto;
import com.xxx.mapper.MenuMapper;
import com.xxx.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class MenuService extends BaseService<Menu,Integer> {

    @Autowired(required = false)
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    public List<MenuDto> queryAllMenus(Integer roleId) {
        //获取所有的menu
        List<MenuDto> menus=menuMapper.selectAllMenus();
        //根据id查询角色拥有的菜单id
        List<Integer> roleHasMids =roleMenuMapper.queryRoleHasMenusByRoleId(roleId);
        if(null!=roleHasMids && roleHasMids.size()>0){
            for(MenuDto m:menus){
                if(roleHasMids.contains(m.getId())){
                    m.setChecked(true);
                }
            }
        }
        return menus;
    }
}
