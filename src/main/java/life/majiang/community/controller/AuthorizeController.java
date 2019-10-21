package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.model.User;
import life.majiang.community.provider.GitHubProvider;
import life.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    GitHubProvider gitHubProvider;

    @Autowired
    AccessTokenDTO accessTokenDTO;

    @Autowired
    UserService userService;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        //以下设置已经写入配置文件中了
//        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
//        accessTokenDTO.setClient_id("8543c922478e7c7fe5ca");
//        accessTokenDTO.setClient_secret("13deee834efb6e508248f60dd288b850dba97808");
//        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        if(null != githubUser ){
            //登录成功
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());  //保存头像图片
            /**
             * 根据用户的accountId进行判断：
             * 如果accountId在数据库中已存在,则对该用户进行更新操作；
             * 如果accountId在数据库中不存在,则对该用户进行添加操作。
             */
            userService.addOrUpdate(user);
            //将token写入Cookie中，响应返回给浏览器 --- 为了在登录首页时，能够判断是否存在本用户
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }
}
