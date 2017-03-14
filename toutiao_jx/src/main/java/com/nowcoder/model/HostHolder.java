package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by Jacinth on 2017/3/10.
 * 存储这次访问的用户是谁
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUsers(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
