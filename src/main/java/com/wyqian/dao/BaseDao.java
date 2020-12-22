package com.wyqian.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {
    //四个参数：driver,url,username,password，用来接收db.properties里的参数
    public static String driver = null;
    public static String url = null;
    public static String username = null;
    public static String password = null;

    //静态代码块，类加载的时候就会运行
    static{
        Properties properties = new Properties();
        //通过类加载器读取对应的资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");

        try {
            //加载db.properties资源文件
            properties.load(is);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载驱动、连接数据库
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写公共查询方法
    public static ResultSet execute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet,  String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for(int i=0; i < params.length; i++){
            preparedStatement.setObject(i+1, params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //编写公共增删改方法----重载
    public static int execute(Connection connection, PreparedStatement preparedStatement, String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for(int i=0; i < params.length; i++){
            preparedStatement.setObject(i+1, params[i]);
        }
        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    //释放资源
    public static boolean closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        boolean flag = true;

        if(connection != null){
            try {
                connection.close();
                //GC回收
                connection = null;
            } catch (SQLException throwables) {
                flag = false;
                throwables.printStackTrace();
            }
        }

        if(preparedStatement != null){
            try {
                preparedStatement.close();
                //GC回收
            } catch (SQLException throwables) {
                flag = false;
                throwables.printStackTrace();
            }
        }

        if(resultSet != null){
            try {
                resultSet.close();
                //GC回收
                resultSet = null;
            } catch (SQLException throwables) {
                flag = false;
                throwables.printStackTrace();
            }
        }

        return flag;
    }
}
