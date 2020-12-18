package com.wyqian.servlet.user;

import com.wyqian.pojo.User;
import com.wyqian.service.user.UserServiceImpl;
import com.wyqian.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    //控制层：调用业务层代码

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        UserServiceImpl userService = new UserServiceImpl();
        User login = userService.login(userCode, userPassword);

        if(login != null && login.getUserPassword().equals(userPassword)){
            //查出来的用户存在，并且密码验证成功，才可以走正常登陆~

            //将用户信息放入Session中
            req.getSession().setAttribute(Constant.USER_SESSION, login);
            //跳转到主页
            resp.sendRedirect("jsp/frame.jsp");
        }else{
            //否则，转发至login.jsp

            //请求转发可以携带信息，后端将提示信息放入error中，供前端读取
            req.setAttribute("error", "用户名或者密码错误！");
            //请求转发至登陆页面
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
