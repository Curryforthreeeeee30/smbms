package com.wyqian.dao.user;

import com.mysql.jdbc.StringUtils;
import com.wyqian.dao.BaseDao;
import com.wyqian.pojo.User;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyPermission;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        User user = null;

        if(connection != null){
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};

            rs = BaseDao.execute(connection, preparedStatement, rs, sql, params);

            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResource(null, preparedStatement, rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String password) {
        PreparedStatement pstm = null;
        int execute = 0;
        if (connection!=null){
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object params[] = {password,id};
            try {
                execute = BaseDao.execute(connection, pstm, sql, params);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            BaseDao.closeResource(null,pstm,null);
        }
        System.out.println("进入Dao层");
        return execute;
    }

    @Override
    public int getUserCount(Connection connection, String username, int userRole) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int count = 0;

        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user as u, smbms_role as r where u.userRole = r.id");

            ArrayList<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");//index：0
            }
            if(userRole > 0){
                sql.append(" and userRole = ?");
                list.add(userRole);//index：1
            }

            //把list转换成数组
            Object [] params = list.toArray();
            try {
                rs = BaseDao.execute(connection, preparedStatement, rs, sql.toString(), params);
                if(rs.next()){
                    count = rs.getInt("count");//从结果集中获取最终的参数
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                BaseDao.closeResource(null,preparedStatement,rs);
            }

        }
        return count;

    }

    @Override
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        ArrayList<User> userList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select u.*, r.roleName from smbms_user as u, smbms_role as r where u.userRole = r.id");

        ArrayList<Object> list = new ArrayList<>();
        if(!StringUtils.isNullOrEmpty(username)){
            sql.append(" and u.userName like ?");
            list.add("%" + username + "%");
        }
        if(userRole > 0){
            sql.append(" and userRole = ?");
            list.add(userRole);
        }
        sql.append(" order by creationDate DESC limit ?,?");
        currentPageNo = (currentPageNo - 1) * pageSize;
        list.add(currentPageNo);
        list.add(pageSize);

        Object [] params = list.toArray();
        System.out.println("UserDaoImpl-->sql:" + sql.toString());
        try {
            rs = BaseDao.execute(connection, preparedStatement, rs, sql.toString(), params);
            while(rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("RoleName"));
                _user.setAge(rs.getDate("birthday"));
                userList.add(_user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(null,preparedStatement,rs);
        }
        return userList;
    }
}
