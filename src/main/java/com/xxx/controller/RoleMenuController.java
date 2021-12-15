package com.xxx.controller;

import com.xxx.base.BaseController;
import com.xxx.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("roleMenu")
public class RoleMenuController extends BaseController {

    @Autowired
    private RoleMenuService roleMenuService;
}
