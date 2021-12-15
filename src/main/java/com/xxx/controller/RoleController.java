package com.xxx.controller;

import com.xxx.base.BaseController;
import com.xxx.base.ResultInfo;
import com.xxx.bean.Role;
import com.xxx.query.RoleQuery;
import com.xxx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 跳转视图
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "role/role";
    }


    /**
     * 条件查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(RoleQuery roleQuery){
        return roleService.queryRoles(roleQuery);
    }


    /**
     * 跳转添加或修改界面
     */
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(Integer roleId, Model model){
        //判断id是否为空
        if(roleId !=null){
            //将其对应角色存至作用域
            model.addAttribute("role",roleService.selectByPrimaryKey(roleId));
        }
        return "role/add_update";
    }

    /**
     * 添加用户
     * @param role
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(Role role){
        roleService.saveRole(role);
        return success("添加成功");
    }

    /**
     * 修改用户
     * @param role
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.updateRole(role);
        return success("修改成功");
    }

    /**
     * 删除用户
     * @param roleId
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer roleId){
        roleService.deleteRoleById(roleId);
        return success("删除成功");
    }


    /**
     *授权
     */
    @RequestMapping("/grant")
    @ResponseBody
    public ResultInfo grant(Integer[] mids,Integer roleId){
         roleService.addGrant(mids,roleId);
         return success("权限添加成功");
    }


    @RequestMapping("getList")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(){
        return roleService.queryAllRoles();
    }

}
