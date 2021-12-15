package com.xxx.mapper;

import com.xxx.base.BaseMapper;
import com.xxx.bean.User;
import com.xxx.query.UserQuery;

import java.util.List;

public interface UserMapper  extends BaseMapper<User,Integer> {

    User selectUserByName(String loginname);

}