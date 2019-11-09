package life.majiang.community.mapper;

import life.majiang.community.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into `comment`(parent_id,type,commentator_id,gmt_create,gmt_modified,like_count,content)" +
            "values(#{parentId},#{type},#{commentatorId},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void addComment(Comment comment);

    @Select("select * from `comment` where `id` = #{parentId}")
    Comment getCommentById(Integer parentId);

    @Select("select * from `comment` where `parent_id` = #{parentId} and `type` = #{type} order by `gmt_create` desc")
    List<Comment> findCommentsByTargetId(@Param("parentId") Integer id, @Param("type") Integer type);

    @Update("update `comment` set `comment_count` = #{commentCount}+1 where id = #{id}")
    void addCommentCount(Comment comment);

}
