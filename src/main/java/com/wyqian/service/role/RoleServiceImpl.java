package com.wyqian.service.role;

import com.wyqian.dao.BaseDao;
import com.wyqian.dao.role.RoleDao;
import com.wyqian.dao.role.RoleDaoImpl;
import com.wyqian.pojo.Role;

import java.sql.Connection;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;

    public RoleServiceImpl(){
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        connection = BaseDao.getConnection();

        List<Role> roleList = roleDao.getRoleList(connection);

        BaseDao.closeResource(connection, null, null);
        return roleList;
    }
}
