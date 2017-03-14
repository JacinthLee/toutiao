package com.nowcoder.service;

//import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
//import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import com.sun.xml.internal.ws.server.ServerRtException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.ref.SoftReference;
import java.util.*;

/**
 * Created by Jacinth on 2017/2/25.
 */
@Service
public class UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String,Object> register(String username, String password) {//先判断用户名，在判断密码的合法性
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空！");
           return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空！");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user != null) {//用户名已经被注册
            map.put("msgname", "用户名已经被注册！");
            return map;
        }

        //密码强度
        user = new User();//插入用户
        user.setName(username);
        //user.setPassword(password);//保存密码，明文，不安全弃用
        //user.setPassword(md5(password));//保存密码，一次md5加密，同样不够安全弃用
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//保存密码，盐加密
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));//随机生成一个用户头像
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);//保存一个用户。因为注册实质就是增加一个用户

        //登陆
        //给登陆的用户下发一个ticket
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    public Map<String,Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空！");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {//用户名不存在
            map.put("msgname", "用户名不存在！");
            return map;
        }

        if(ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确！");
            return map;
        }
//        //给登陆的用户下发一个ticket
//        String ticket=addLoginTicket(user.getId());
//        map.put("ticket",ticket);

        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    //已经知道用户是谁，登出就是让ticket过期
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);//ticket，0是正常，1是过期
    }
}
