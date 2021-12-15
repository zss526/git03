package com.xxx.controller;

import com.xxx.base.BaseController;
import com.xxx.bean.User;
import com.xxx.mapper.UserMapper;
import com.xxx.service.UserService;
import com.xxx.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;

    @RequestMapping("/index")
    public String index(){
        return "/index";
    }

    @RequestMapping("/main")
    public String main(HttpServletRequest request){
        //从req中获取userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据id查找用户
        User user = userService.selectByPrimaryKey(userId);
        //将用户存储到作用域中
        request.setAttribute("user",user);
        return "/main";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "/welcome";
    }


}
