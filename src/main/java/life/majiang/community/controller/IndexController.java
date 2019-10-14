package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;


    @GetMapping("/")
    public String index(HttpServletRequest request){  //浏览器发送过来的请求会携带Cookie信息
        Cookie[] cookies = request.getCookies();
        if(null != cookies){   //当页面清理痕迹时，Cookie会清空掉
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(user != null){
                        request.getSession().setAttribute("user",user);  //前端页面需要Session
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
