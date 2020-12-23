package com.wyqian.dao.user;

import com.wyqian.pojo.Role;
import com.wyqian.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //获得登陆的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改密码
    public int updatePwd(Connection connection, int id, String password);

    //根据用户名或者角色查询用户总数
    public int getUserCount(Connection connection, String username, int userRole);

    //查询用户列表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize);

}
