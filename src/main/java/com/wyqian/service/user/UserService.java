package com.wyqian.service.user;

import com.wyqian.pojo.User;

import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode, String password);
    //修改用户密码
    public boolean updatePwd(int id, String password);
    //查询记录数
    public int getUserCount(String username, int userRole);
    //查询用户列表
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize);
}
