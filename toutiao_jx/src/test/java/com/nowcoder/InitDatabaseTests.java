package com.nowcoder;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.News;

import com.nowcoder.model.User;
import com.sun.org.apache.xml.internal.security.Init;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
//@WebAppConfiguration  这行会修改默认的启动路径需要注释掉
@Sql({"/init-schema.sql"})
public class InitDatabaseTests {

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Test
    public void InitDataBase() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();//现在的时间
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);//毫秒计的，所以每条咨询隔五个小时
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setLink(String.format("http://www.nowcoder.com/link/{%d}.html", i));
            news.setTitle(String.format("Title {%d} ", i));
            news.setUserId(i+1);
            newsDAO.addNews(news);
            System.out.println(news.getId());

            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            LoginTicket ticket=new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d",i+1));
            loginTicketDAO.addTicket(ticket);//插入成功的话，数据库会有数据

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);//更新一条数据
        }

        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));

        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());
    }
}
