package com.wyqian.service.user;

import com.wyqian.dao.BaseDao;
import com.wyqian.dao.user.UserDao;
import com.wyqian.dao.user.UserDaoImpl;
import com.wyqian.pojo.User;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.SQLException;

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

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User admin = userService.login("admin", "1234567");
        System.out.println(admin.getUserPassword());
    }
}
