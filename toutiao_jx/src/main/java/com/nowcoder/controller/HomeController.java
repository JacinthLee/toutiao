package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.ToutiaoService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Jacinth on 2017/2/25.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    NewsService newsService;

//    @Autowired
//    ToutiaoService toutiaoService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

//    //首页通过模板显示
//    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
//    public String index(HttpSession session) {
//        return "home";
//    }

    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newsList=newsService.getLatestNews(userId,offset,limit);

        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})//访问首页能看到连接
    public String index(Model model) {
        model.addAttribute("vos",getNews(0,0,10));//打开首页的位置
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})//访问user也能看到链接
    public String userindex(Model model,@PathVariable("userId") int userId,
                            @RequestParam(value = "pop",defaultValue = "0")int pop) {
        model.addAttribute("vos",getNews(userId,0,10));
        model.addAttribute("pop",pop);//pop是从前端参数解析出来的
        return "home";
    }

}
