package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into `question`(title,description,creator_id,gmt_create,gmt_modified,tag) " +
            "values(#{title},#{description},#{creatorId},#{gmtCreate},#{gmtModified},#{tag})")
    void addQuestion(Question question);

    @Select("select q.*,u.avatar_url from `question` q inner join `user` u on q.creator_id = u.id ")
    List<Question> findQuestions();
}
