package com.xxx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.base.BaseService;
import com.xxx.bean.Role;
import com.xxx.bean.RoleMenu;
import com.xxx.mapper.RoleMapper;
import com.xxx.mapper.RoleMenuMapper;
import com.xxx.query.RoleQuery;
import com.xxx.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 */
@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * 条件查询
     * @param roleQuery
     * @return
     */
    public Map<String, Object> queryRoles(RoleQuery roleQuery) {
        HashMap<String, Object> map = new HashMap<>();
        //初始化分页
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        //查询结果
        List<Role> rlist=roleMapper.queryRolesByNameAndDesc(roleQuery);
        //开始分页
        PageInfo<Role> pi = new PageInfo<>(rlist);
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pi.getTotal());
        map.put("data",pi.getList());
        return map;
    }

    /*
    添加用户
     */
    @Transactional
    public void saveRole(Role role) {
        //角色名和角色描述描述不能为空
        checkRoleSaveOrUpDate(role.getRolename(),role.getRoledesc());
        //角色名不能与与数据库中的同名
        Role temp=roleMapper.selectRoleByName(role.getRolename());
        AssertUtil.isTrue(temp!=null,"角色已存在");
        //默认参数
        role.setAvailable(1);
        role.setCreattime(new Date());
        role.setUpdatetime(new Date());
        //判断是否添加成功
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"添加失败");
    }

    /**
     * 校验角色名和角色备注
     *
     */
    public void checkRoleSaveOrUpDate(String roleName,String roleDesc){
        //角色名和角色描述描述不能为空
        AssertUtil.isTrue(StringUtils.isBlank(roleName),"角色名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(roleDesc),"角色备注不能为空");
    }
    /**
     * 修改用户
     */
    @Transactional
    public void updateRole(Role role){
        //判断用户是否存在
        AssertUtil.isTrue(role.getRoleid()==null||roleMapper.selectByPrimaryKey(role.getRoleid())==null,"待修改记录不存在");
        //校验角色名和角色备注
        checkRoleSaveOrUpDate(role.getRolename(),role.getRoledesc());
        //判定用户名
        Role temp=roleMapper.selectRoleByName(role.getRolename());
        AssertUtil.isTrue(temp !=null && !temp.getRoleid().equals(role.getRoleid()),"角色名已存在");
        //设置修改参数
        role.setUpdatetime(new Date());
        //判断是否修改成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"修改失败");
    }

    /**
     * 删除用户
     * @param roleId
     */
    @Transactional
    public void deleteRoleById(Integer roleId) {
        //id不能为空 且需要数据库有记录
        AssertUtil.isTrue(roleId==null || roleMapper.selectByPrimaryKey(roleId)==null,"待删除记录不存在");
        //判断是否删除成功
        AssertUtil.isTrue(roleMapper.deleteRoleById(roleId)<1,"删除失败");
    }

    /**
     * 添加权限
     * @param mids
     * @param roleId
     */
    @Transactional
    public void addGrant(Integer[] mids, Integer roleId) {
        AssertUtil.isTrue(roleId==null || selectByPrimaryKey(roleId)==null,"待授权的角色不存在");
        int count=roleMenuMapper.countMenusByRoleId(roleId);
        if(count>0){
            //删除所有原有的权限
            AssertUtil.isTrue(roleMenuMapper.deleteMenuByRoleId(roleId)!=count,"删除角色原有权限失败");
        }
        //添加角色权限
        if(null!=mids && mids.length>0){
            List<RoleMenu> rlist = new ArrayList<>();
            for(Integer m:mids){
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMid(m);
                roleMenu.setRid(roleId);
                rlist.add(roleMenu);
            }
            AssertUtil.isTrue(roleMenuMapper.insertBatch(rlist)!=rlist.size(),"角色授权失败");
        }
    }

    public List<Map<String, Object>> queryAllRoles() {
        return roleMapper.queryAllRoles();
    }
}
