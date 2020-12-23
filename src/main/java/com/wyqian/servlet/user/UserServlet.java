package com.wyqian.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.wyqian.dao.role.RoleDaoImpl;
import com.wyqian.pojo.Role;
import com.wyqian.pojo.User;
import com.wyqian.service.role.RoleServiceImpl;
import com.wyqian.service.user.UserServiceImpl;
import com.wyqian.util.Constant;
import com.wyqian.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("savepwd")){
            updatePwd(req, resp);
        }else if(method != null && method.equals("pwdmodify")){
            pwdModify(req, resp);
        }else if(method != null && method.equals("query")){
            query(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        //从Session中拿id
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map
        HashMap<String, String> map = new HashMap<>();
        if(o == null){
            //当前Session过期，传一个sessionerror过去
            map.put("result", "sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            //旧密码为空，传一个error过去
            map.put("result", "error");
        }else{
            User user = (User) o;
            if(oldpassword.equals(user.getUserPassword())){
                //旧密码输入正确
                map.put("result", "true");
            }else{
                //旧密码输入错误
                map.put("result","false");
            }
        }
        resp.setContentType("application/json");
        /*
        阿里巴巴JSON工具类：JSONArray, 转换格式，需引入fastjson的maven依赖
         */
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(map));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePwd(HttpServletRequest req, HttpServletResponse resp){
        //从Session中拿id
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String newpassword = req.getParameter("newpassword");

        boolean flag = false;

        if(o != null && newpassword != null){
            UserServiceImpl userService = new UserServiceImpl();
            User user = (User) o;
            flag = userService.updatePwd(user.getId(), newpassword);

            if(flag){
                req.setAttribute("message","修改密码成功！请退出，使用新密码登录！");
                //密码修改成功，移除当前Session
                req.removeAttribute(Constant.USER_SESSION);
            }else{
                req.setAttribute("message", "对不起，密码修改失败！");
            }
        }else{
            req.setAttribute("message","新密码有问题！");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //query方法
    private void query(HttpServletRequest req, HttpServletResponse resp){
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        int queryUserRole = 0;//默认为0是因为正常不选择的时候是展示全部页面，这里和Dao层保持一致

        //处理分页问题
        int currentPageNo = 1;
        int pageSize = 5;//这里其实可以写进配置文件，方便用户修改
        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }
        if(pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        UserServiceImpl userService = new UserServiceImpl();
        //获得用户的总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        //总页数支持
        PageSupport pageSupport = new PageSupport();

        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int tmp = totalCount % pageSize;
        int totalPageCount = (tmp == 0) ? (totalCount / pageSize) : (totalCount / pageSize)+1;

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }
        if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        //获取用户列表展示
        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount",totalCount);

        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
