package com.wyqian.servlet.user;

import com.wyqian.pojo.User;
import com.wyqian.service.user.UserServiceImpl;
import com.wyqian.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从Session中拿id
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String newpassword = req.getParameter("newpassword");

        boolean flag = false;

        if(o != null && newpassword != null && newpassword.length() > 0){
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
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
