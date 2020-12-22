package com.wyqian.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.wyqian.pojo.User;
import com.wyqian.service.user.UserServiceImpl;
import com.wyqian.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("savepwd")){
            updatePwd(req, resp);
        }else if(method != null && method.equals("pwdmodify")){
            pwdModify(req, resp);
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
        System.out.println(newpassword);

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
}
