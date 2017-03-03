package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.ToutiaoService;
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
 * Created by Jacinth on 2017/2/20.
 */
//@Controller//通过java注解的方式来指定这个类是干嘛的。通过注解将定义的很多个类关联起来
public class IndexController {

    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);
    @Autowired//当Spring逻辑启动时，它的核心部分会把类文件ToutiaoService复制到下面的成员函数toutiaoService
    private ToutiaoService toutiaoSevrice;//控制反转/依赖注入的方式设置变量

    //@RequestMapping(path = "/"+"/index")//指定访问的url地址
    @RequestMapping("/")
    @ResponseBody
    public String index(HttpSession session){
        logger.info("Visit Index");
        return "Hello World!!!"+session.getAttribute("msg")
                +"<br> Say:"+toutiaoSevrice.say();
    }

    @RequestMapping(value = "/profile/{groupId}/{useId}")//value与path作用一致。传参数进来
    @ResponseBody
    public String profile(@PathVariable("groupId")String groupId,
                          @PathVariable("useId")int useId,
                          @RequestParam(value = "type",defaultValue = "1")int type,
                          @RequestParam(value = "key",defaultValue = "nowcoder")String key){
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,useId,type,key);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vv1");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        Map<String,String> map=new HashMap<String,String>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("colors",colors);
        model.addAttribute("map",map);

        //新建model包和User类
        model.addAttribute("user", new User("Jim"));

        return "news";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headerNames=request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");

        }

        for (Cookie cookie : request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }

        sb.append("getMethod:" + request.getMethod() + "<br>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br>");
        sb.append("getQueryString:" + request.getQueryString() + "<br>");//引号、问号后面的
        sb.append("getRequestURI:" + request.getRequestURI() + "<br>");
        return sb.toString();
    }

    @RequestMapping(value = "/response")
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderid",defaultValue = "a")String nowcoderId,
                           @RequestParam(value = "key",defaultValue = "key")String key,
                           @RequestParam(value = "value",defaultValue = "value")String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key, value));
        response.addHeader(key,value);
        return "NowCoderId From Cookie:"+nowcoderId;
    }

//    @RequestMapping("/redirect/{code}")
//    public RedirectView redirect(@PathVariable("code") int code){
//        RedirectView red=new RedirectView("/",true);
//        if(code==301){
//            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);//判断如果是301，就是强制性跳转，如果不是301则为临时性跳转
//        }
//        return red;
//    }

    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                           HttpSession session){
//        RedirectView red=new RedirectView("/",true);
//        if(code==301){
//            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);//判断如果是301，就是强制性跳转，如果不是301则为临时性跳转
//        }
//        return red;
        session.setAttribute("msg","Jump from redirect.");//session表示浏览器一次打开网页,跟服务器完整的长期的交互
        //http://127.0.0.1:8080/redirect/302为Hello World!!!Jump from redirect.
        //http://127.0.0.1:8080/为Hello World!!!null
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false)String key){
        if ("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("Key 错误");//抛出异常
    }

    //自己定义@ExceptionHandler，用来拦截异常
    //异常统一跳转到一个显示异常的页面
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}
