package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Jacinth on 2017/3/20.
 * 增加评论，某个实体有多少个评论，
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";

    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";

    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{userId},#{content},#{createdDate},#{entityId}, #{entityType},#{status})"
    })
    int addComment(Comment comment);//成功返回int，失败返回0.成功后comment被赋值

    //删除一条评论实际上不是删掉，而是更新
    @Update({"update",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}" })
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

    //评论是时间逆序
    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc "})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType );

    @Select({"select count(id) from", TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType}" })
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType );
}

