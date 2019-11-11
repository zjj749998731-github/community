package life.majiang.community.mapper;

import life.majiang.community.dto.QuestionQueryDTO;
import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

//    @Select("select count(1) from `question`")
//    Integer getTotalCount();
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    @Select("select q.*,u.avatar_url from `question` q inner join `user` u on q.creator_id = u.id order by `gmt_create` desc limit #{offset},#{pageSize}")
    List<Question> findQuestions(@Param(value = "offset") Integer offset, @Param(value = "pageSize")Integer pageSize);
    List<Question> findQuestionsBySearch(QuestionQueryDTO questionQueryDTO);


    @Select("select count(1) from `question` where `creator_id` = #{id}")
    Integer getMyTotalCount(Integer id);

    @Select("select q.*,u.avatar_url from `question` q " +
            "inner join `user` u on q.creator_id = u.id " +
            "WHERE u.id = #{id} " +
            "order by `gmt_create` desc " +
            "limit #{offset},#{pageSize}")
    List<Question> findMyQuestions(@Param(value = "id") Integer id, @Param(value = "offset") Integer offset, @Param(value = "pageSize") Integer pageSize);

    @Select("select q.*,u.avatar_url,u.name from `question` q " +
            "inner join `user` u on q.creator_id = u.id " +
            "WHERE q.id = #{id} ")
    Question findQuestionById(Integer id);

    @Insert("insert into `question`(title,description,creator_id,gmt_create,gmt_modified,tag) " +
            "values(#{title},#{description},#{creatorId},#{gmtCreate},#{gmtModified},#{tag})")
    void addQuestion(Question question);

    @Update("update `question` set `title` = #{title},`description` = #{description},`gmt_modified` = #{gmtModified},`tag` = #{tag} where id = #{id}")
    Integer updateQuestion(Question question);

    @Update("update `question` set `view_count` = #{viewCount}+1 where id = #{id}")
    void addView(Question question);

    @Update("update `question` set `comment_count` = #{commentCount}+1 where id = #{id}")
    void addComment(Question question);

    @Select("select q.id,q.title,q.tag,u.avatar_url,u.name " +
            "from `question` q inner join `user` u on q.creator_id = u.id " +
            "WHERE q.tag regexp #{tag} and q.id != #{id}")
    List<Question> findRelatedQuestions(Question question);




}
