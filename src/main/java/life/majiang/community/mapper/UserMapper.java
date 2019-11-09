package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM `user`")
    List<User> findAll();    //Select会根据返回结果的个数自动封装成单个类还是List集合

    @Select("SELECT * FROM `user` WHERE `id` = #{creatorId}")
    User findById(Integer creatorId);  //若形参只有一个，@Param注解可以不用写。但是当有多个形参时，@Param必须写上

    @Select("SELECT * FROM `user` WHERE `token` = #{token}")
    User findByToken(String token);

    @Select("SELECT * FROM `user` WHERE `account_id` = #{accountId}")
    User findByAccountId(String accountId);

    @Insert("INSERT INTO `user`(`account_id`,`name`,`token`,`gmt_create`,`gmt_modified`,`avatar_url`) VALUES(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void addUser(User user);

    @Update("UPDATE `user` SET `name` = #{name},`token` = #{token},`gmt_modified` = #{gmtModified},`avatar_url` = #{avatarUrl} WHERE `account_id` = #{accountId} ")
    void updateUser(User user);

    @Delete("DELETE FROM `user` WHERE `token` = #{token}")
    void deleteUser(String token);

}
