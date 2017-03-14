package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Jacinth on 2017/2/24.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, "+INSERT_FIELDS;

    //通过注解的方式插入一条数据
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    //判断用户名是否已经被注册
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    //用户登出后，更新状态
    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})//两个参数，区分两个参数用Param
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);

    //然后写个测试用例验证一下
}
