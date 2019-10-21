package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void addOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());  //dbUser是旧数据，形参user是新的GithubUser
        if (dbUser != null) {  //用户存在，更新旧数据
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            userMapper.updateUser(dbUser);
        } else {    //用户不存在，添加
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.addUser(user);
        }
    }

}
