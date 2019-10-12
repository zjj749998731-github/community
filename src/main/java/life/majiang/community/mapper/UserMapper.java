package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO `user`(`account_id`,`name`,`token`,`gmt_create`) VALUES(#{accountId},#{name},#{token},#{gmtCreate})")
    public void addUser(User user);
}
