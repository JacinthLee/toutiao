package com.nowcoder.controller;

import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jacinth on 2017/3/8.
 * 用户登录
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,//用户名
                      @RequestParam("password") String password,//密码
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,//记住密码，defaultValue=0为非登录状态
                      HttpServletResponse response) {  //在response里面写cookie
        try {
            Map<String, Object> map = userService.register(username, password);
            //JSONChar格式数据  {"code":0, "msg":"xxxx"}
//            if (map.isEmpty()) {  //注册成功返回就不是Empty了，返回ticket
//                return ToutiaoUtil.getJSONString(0, "注册成功");
//            } else {
//                return ToutiaoUtil.getJSONString(1, map);
//            }
            if (map.containsKey("ticket")) {//注册成功返回就不是Empty了，返回ticket，所以包含ticket就是注册成功，否则注册失败
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());//方法成功，生成cookie
                cookie.setPath("/");//设置cookie全站有效
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);//cookie的有效时间，设置为五天，不设置就会是浏览器关闭就没了
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");//系统异常导致的
        }
    }

    //登出
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";//登出后，自动跳到首页
    }

}
