package life.majiang.community.mapper;

import life.majiang.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper {

    @Insert("insert into `comment`(parent_id,type,commentator_id,gmt_create,gmt_modified,like_count,content)" +
            "values(#{parentId},#{type},#{commentatorId},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void addComment(Comment comment);

    @Select("select * from `comment` where `parent_id` = #{parentId}")
    Comment getCommentByParentId(Integer parentId);


}
