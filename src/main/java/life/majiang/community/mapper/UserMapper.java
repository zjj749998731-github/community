package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO `user`(`account_id`,`name`,`token`,`gmt_create`) VALUES(#{accountId},#{name},#{token},#{gmtCreate})")
    void addUser(User user);

    @Select("SELECT * FROM `user` WHERE `token` = #{token}")
    User findByToken(String token);

    @Select("SELECT * FROM `user`")
    List<User> findAll();    //Select会根据返回结果的个数自动封装成单个类还是List集合
}
