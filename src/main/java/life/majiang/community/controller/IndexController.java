package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;


    @GetMapping("/")
    public String index(HttpServletRequest request,Model model){  //浏览器发送过来的请求会携带Cookie信息
        Cookie[] cookies = request.getCookies();
        if(null != cookies && cookies.length != 0){   //当页面清理痕迹时，Cookie会清空掉
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
        List<Question> questions = questionMapper.findQuestions();
//        List<QuestionDTO> questions = questionService.getQuestionDTOList(); //视频采用此方式
        model.addAttribute("questions",questions);
        return "index";
    }

    @GetMapping("/layout")
    public String layout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    userMapper.deleteUser(token);
                    request.getSession().setAttribute("user", null);
                    break;
                }
            }
        }
        return "redirect:/";
    }
}
