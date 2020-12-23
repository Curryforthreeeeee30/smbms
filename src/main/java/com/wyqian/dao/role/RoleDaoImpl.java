package com.wyqian.dao.role;

import com.mysql.jdbc.StringUtils;
import com.wyqian.dao.BaseDao;
import com.wyqian.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    @Override
    public List<Role> getRoleList(Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Role> roleList = new ArrayList<Role>();

        String sql = "select * from smbms_role";
        Object[] params = {};

        try {
            rs = BaseDao.execute(connection, preparedStatement, rs, sql, params);
            while(rs.next()){
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                roleList.add(_role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(null, preparedStatement, rs);
        }
        return roleList;

    }
}
