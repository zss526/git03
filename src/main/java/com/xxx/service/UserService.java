package com.xxx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.base.BaseService;
import com.xxx.bean.User;
import com.xxx.mapper.UserMapper;
import com.xxx.model.UserModel;
import com.xxx.query.UserQuery;
import com.xxx.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.security.MessageDigestSpi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class UserService extends BaseService<User,Integer>{
    @Resource
    private UserMapper userMapper;

    public UserModel userLogin(String loginname,String pwd){
        //用户名密码不能为空
        checkLoginParams(loginname,pwd);
        //根据用户名查询用户对象
        User temp=userMapper.selectUserByName(loginname);
        //判断用户是否存在
        AssertUtil.isTrue(temp==null,"用户不存在");
        //若用户存在，校验密码
        checkPwd(pwd,temp.getPwd());
        //校验完毕 返回结果
        return buildUserInfo(temp);

    }

    /**
     * 将用户对象转为UserModel
     * @param temp
     * @return
     */
    private UserModel buildUserInfo(User temp) {
        UserModel userModel = new UserModel();
        //将userId加密
        userModel.setUserid(UserIDBase64.encoderUserID(temp.getUserid()));
        userModel.setLoginname(temp.getLoginname());
        userModel.setRealname(temp.getRealname());
        return userModel;
    }

    /**
     * 校验密码
     * @param pwd
     * @param pwd1
     */
    private void checkPwd(String pwd, String pwd1) {
        //对输入密码加密
        AssertUtil.isTrue(!Md5Util.encode(pwd).equals(pwd1),"用户密码不正确");
    }

    /**
     * 校验用户名和密码
     * @param loginname
     * @param pwd
     */
    private void checkLoginParams(String loginname, String pwd) {
        AssertUtil.isTrue(StringUtils.isBlank(loginname),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(pwd),"密码不能为空");

    }

    /**
     * 修改基本资料
     * @param user
     */
    @Transactional
    public void updateUser(User user) {
        //校验用户是否存在
        AssertUtil.isTrue(user.getUserid()==null|| userMapper.selectByPrimaryKey(user.getUserid())==null,"用户不存在");
        //校验数据
        //手机号不为空且符合手机号格式 身份证号不能为空且为正确格式
        checkPhoneAndIdentity(user.getPhone(),user.getIdentity());
        //真实姓名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(user.getRealname()),"用户真实姓名不能为空");
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }

    /**
     * 校验手机号和身份证号
     * @param phone
     * @param identity
     */
    private void checkPhoneAndIdentity(String phone, String identity) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"请输入正确的手机号");
        AssertUtil.isTrue(StringUtils.isBlank(identity),"身份证号不能为空");
        AssertUtil.isTrue(!IdentityUtil.isMobile(identity),"请输入正确的身份证号");
    }

    /**
     * 修改密码
     * @param userid
     * @param oldPwd
     * @param newPwd
     * @param confirmPwd
     */
    @Transactional
    public void updatePassword(Integer userid,String oldPwd,String newPwd,String confirmPwd){
        //查询用户
        User user = userMapper.selectByPrimaryKey(userid);
        //校验
        checkPassword(user,oldPwd,newPwd,confirmPwd);
        //将密码设为新密码
        user.setPwd(Md5Util.encode(newPwd));

        //判定是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }

    /**
     * 校验用户和密码
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param confirmPwd
     */
    private void checkPassword(User user, String oldPwd, String newPwd, String confirmPwd) {
        AssertUtil.isTrue(user==null,"用户不存在");
        //密码均不为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原密码不为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不为空");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不为空");
        //原密码与数据库中的相同
        AssertUtil.isTrue(!Md5Util.encode(oldPwd).equals(user.getPwd()),"原密码不正确");
        //新密码必须与确认密码相同
        AssertUtil.isTrue(!newPwd.equals(confirmPwd),"确认密码与新密码不一致");
        //新密码不能包含新密码
        AssertUtil.isTrue(newPwd.contains(oldPwd),"新密码中不能包含旧密码");

    }

    public Map<String, Object> queryAllUsersByparams(UserQuery userQuery) {
        HashMap<String, Object> map = new HashMap<>();
        //初始化分页
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());

        //查询结果
        List<User> ulist=userMapper.selectByParams(userQuery);
        //开始分页
        PageInfo<User> userPageInfo = new PageInfo<>(ulist);
        map.put("code",0);
        map.put("msg","success");
        map.put("count",userPageInfo.getTotal());
        map.put("data",userPageInfo.getList());
        return map;
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional
    public void saveUser(User user){
        //校验
        checkUserParams(user.getRealname(),user.getLoginname(),user.getIdentity(),user.getPhone(),user.getAddress());
        //登录名不能与数据库中的相同
        User temp = userMapper.selectUserByName(user.getLoginname());
        AssertUtil.isTrue(temp!=null,"用户名已存在");
        //默认值
        user.setPwd(Md5Util.encode("123456"));
        user.setAvailable(1);
        user.setPosition("小年轻");
        //判断是否添加成功
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败");

    }

    /**
     * 校验数据
     * @param realname
     * @param loginname
     * @param identity
     * @param phone
     * @param position
     */
    private void checkUserParams(String realname, String loginname, String identity, String phone, String position) {
        //军不能为空
        AssertUtil.isTrue(StringUtils.isBlank(realname),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(loginname),"登录姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(identity),"身份证号不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"电话不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(position),"地址不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"请输入正确的手机号码");
        AssertUtil.isTrue(!IdentityUtil.isMobile(identity),"请输入正确的身份证号");
    }


    public void updateUserMsg(User user){
        //判断用户是否存在
        User temp = userMapper.selectByPrimaryKey(user.getUserid());
        AssertUtil.isTrue(user.getUserid()==null||temp==null,"待修改用户不存在");
        //校验参数
        checkUserParams(user.getRealname(),user.getLoginname(),user.getIdentity(),user.getPhone(),user.getAddress());
        //用户名不能重复
        User temp2 = userMapper.selectUserByName(user.getLoginname());
        AssertUtil.isTrue(temp2!=null && !temp2.getUserid().equals(user.getUserid()),"用户名已存在");

        //校验是否更新成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }

    /**
     * 删除用户
     * @param userId
     */
    public void deleteUser(Integer[] userId){
        AssertUtil.isTrue(null==userId || userId.length == 0,"请选择待删除的用户记录!");
        AssertUtil.isTrue(deleteBatch(userId) != userId.length,"用户记录删除失败!");
    }

}
