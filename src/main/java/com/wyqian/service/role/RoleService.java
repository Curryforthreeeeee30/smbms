package com.wyqian.service.role;

import com.wyqian.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleService {
    //获取用户列表
    public List<Role> getRoleList();
}
