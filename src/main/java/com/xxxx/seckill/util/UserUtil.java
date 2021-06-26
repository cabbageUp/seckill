package com.xxxx.seckill.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {
    private static void createUser(int count) throws SQLException, ClassNotFoundException, IOException {
        List<User> users=new ArrayList<>();
        for(int i=0;i<count;i++){
            User user=new User();
            user.setId(13000000000L+i);
            user.setNickname("user"+i);
            user.setSlat("1a2b3c4d");
            user.setLogingCount(1);
            user.setRegisterDate(new Date());
            user.setPassword(MD5Util.inputPassToDBPass("123456",user.getSlat()));
            users.add(user);
        }
        System.out.println("create users");
        //插入到数据库
//        Connection conn=getConnection();
//        String sql="insert into t_user (loging_count,nickname,register_date,slat,password,id) values(?,?,?,?,?,?)";
//        PreparedStatement preparedStatement=conn.prepareStatement(sql);
//        for(int i=0;i<users.size();i++){
//            User user=users.get(i);
//            preparedStatement.setInt(1,user.getLogingCount());
//            preparedStatement.setString(2,user.getNickname());
//            preparedStatement.setTimestamp(3,new Timestamp(user.getRegisterDate().getTime()));
//            preparedStatement.setString(4,user.getSlat());
//            preparedStatement.setString(5,user.getPassword());
//            preparedStatement.setLong(6,user.getId());
//            preparedStatement.addBatch();
//
//        }
//        preparedStatement.executeBatch();
//        preparedStatement.clearParameters();
//        conn.close();
//        System.out.println("insert into db");
        //登录，生成userTicket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\cabbage\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObj());
            System.out.println("create userTicket : " + user.getId());

            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");





    }
    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        String url="jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username="root";
        String password="123456";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        createUser(5000);
    }
}
