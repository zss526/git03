package com.xxx.controller;

import com.xxx.base.BaseController;
import com.xxx.dto.MenuDto;
import com.xxx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    /**
     * 授权页面
     */
    @RequestMapping("/grantPage")
    public String grant(Integer roleId, Model model){
        if(roleId!=null){
            model.addAttribute("roleId",roleId);
        }
        return "role/grant";
    }

    /**
     * 获取树
     */
    @RequestMapping("getTree")
    @ResponseBody
    public List<MenuDto> queryAllMenus(Integer roleId){
        return menuService.queryAllMenus(roleId);
    }
}
