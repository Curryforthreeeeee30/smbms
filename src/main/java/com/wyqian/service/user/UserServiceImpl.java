package com.wyqian.service.user;

import com.wyqian.dao.BaseDao;
import com.wyqian.dao.user.UserDao;
import com.wyqian.dao.user.UserDaoImpl;
import com.wyqian.pojo.User;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{

    //业务层都会调用Dao层，所以我们要引入Dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User admin = null;

        try {
            connection = BaseDao.getConnection();
            admin = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return admin;
    }

    @Override
    public boolean updatePwd(int id, String password) {
        Connection connection = null;
        boolean flag = false;
        //修改密码
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection,id,password)>0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        System.out.println("进入业务层");
        return flag;
    }

    @Override
    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        connection = BaseDao.getConnection();
        int count = userDao.getUserCount(connection, username, userRole);
        BaseDao.closeResource(connection, null, null);
        return count;
    }

    @Override
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = new ArrayList<User>();
        connection = BaseDao.getConnection();

        userList = userDao.getUserList(connection, username, userRole, currentPageNo,pageSize);
        BaseDao.closeResource(connection, null, null);
        return userList;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
//        User admin = userService.login("admin", "1234567");
//        System.out.println(admin.getUserPassword());
        int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }
}
