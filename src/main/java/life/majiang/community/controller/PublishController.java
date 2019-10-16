package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String addQuestion(Question question, HttpServletRequest request,Model model){
        User user = null;
        Cookie[] cookies = request.getCookies();
        if(null != cookies && cookies.length != 0){   //当页面清理痕迹时，Cookie会清空掉
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        if(user == null){   //用户不存在时，直接返回发布页面
            model.addAttribute("msg","用户未登录");
            return "publish";
        }

        question.setCreatorId(user.getId());   //用户存在时，取出用户信息
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.addQuestion(question);
        model.addAttribute("question",question);
        return "redirect:/";
    }


}
