package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    GitHubProvider gitHubProvider;

    @Autowired
    AccessTokenDTO accessTokenDTO;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request){
        //以下设置已经写入配置文件中了
//        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
//        accessTokenDTO.setClient_id("8543c922478e7c7fe5ca");
//        accessTokenDTO.setClient_secret("13deee834efb6e508248f60dd288b850dba97808");
//        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        if(null != githubUser){
            //登录成功
            User user = new User();
            user.setName(githubUser.getName());
            user.setToken(UUID.randomUUID().toString());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.addUser(user);

            request.getSession().setAttribute("user",githubUser);

            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }
}
