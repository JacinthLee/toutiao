package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Jacinth on 2017/3/10.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportInterceptor passportInterceptor;//注册拦截器

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;//第一步注册拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(passportInterceptor);//回调拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");//第二步看看返回的页面是否符合要求
                                                                                       //只调用一部分，setting
        super.addInterceptors(registry);
    }
}
