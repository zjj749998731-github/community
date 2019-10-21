package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Select("select count(1) from `question`")
    Integer getTotalCount();

    @Select("select q.*,u.avatar_url from `question` q inner join `user` u on q.creator_id = u.id limit #{offset},#{pageSize}")
    List<Question> findQuestions(@Param(value = "offset") Integer offset, @Param(value = "pageSize")Integer pageSize);


    @Select("select count(1) from `question` where `creator_id` = #{id}")
    Integer getMyTotalCount(Integer id);

    @Select("select q.*,u.avatar_url from `question` q " +
            "inner join `user` u on q.creator_id = u.id " +
            "WHERE u.id = #{id} " +
            "limit #{offset},#{pageSize}")
    List<Question> findMyQuestions(@Param(value = "id") Integer id, @Param(value = "offset") Integer offset, @Param(value = "pageSize") Integer pageSize);

    @Select("select q.*,u.avatar_url,u.name from `question` q " +
            "inner join `user` u on q.creator_id = u.id " +
            "WHERE q.id = #{id} ")
    Question findQuestionById(Integer id);

    @Insert("insert into `question`(title,description,creator_id,gmt_create,gmt_modified,tag) " +
            "values(#{title},#{description},#{creatorId},#{gmtCreate},#{gmtModified},#{tag})")
    void addQuestion(Question question);


}
