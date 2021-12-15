package com.xxx.controller;

import com.xxx.base.BaseController;
import com.xxx.base.ResultInfo;
import com.xxx.bean.User;
import com.xxx.model.UserModel;
import com.xxx.query.UserQuery;
import com.xxx.service.UserService;
import com.xxx.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param loginname
     * @param pwd
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResultInfo login(String loginname,String pwd){
        UserModel userModel = userService.userLogin(loginname, pwd);
        return success("登陆成功",userModel);
    }


    /**
     * 设置界面
     * @param request
     * @return
     */
    @RequestMapping("/toSettingPage")
    public String toSettingPage(HttpServletRequest request){
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询
        User user = userService.selectByPrimaryKey(userId);
        //存储到作用域
        request.setAttribute("user",user);
        return "/user/setting";
    }

    /**
     * 修改信息
     * @param user
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo update(User user){
        userService.updateUser(user);
        return  success("修改成功");
    }

    /**
     * 修改密码界面
     * @return
     */
    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "/user/password";
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,String oldPwd,String newPwd,String confirmPwd){
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        userService.updatePassword(userId,oldPwd,newPwd,confirmPwd);
        return success("修改成功");

    }

    /**
     * 跳转页面
     */
    @RequestMapping("/index")
    public String index(){
        return "user/user";
    }

    /**
     * 条件查询
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){
        return userService.queryAllUsersByparams(userQuery);
    }


    @RequestMapping("toUpdateOrSavePage")
    public String toUpdateOrSavePage(Integer userId, Model model){
        if(userId!=null){
            //将其对应的角色存至作用域
            model.addAttribute("user",userService.selectByPrimaryKey(userId));
        }
        return "user/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user){
        userService.saveUser(user);
        return success("添加成功");
    }

    @RequestMapping("updateUserMsg")
    @ResponseBody
    public ResultInfo updateUserMsg(User user){
        userService.updateUserMsg(user);
        return success("添加成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] userId){
        userService.deleteUser(userId);
        return success("删除成功");
    }
}

