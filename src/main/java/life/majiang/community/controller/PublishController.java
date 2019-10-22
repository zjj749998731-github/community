package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;


    @GetMapping("/publish")
    public String publish(@PathVariable(name = "id" ,required = false)Integer id){
        return "publish";
    }

    @PostMapping("/publish")
    public String addQuestion(Question question, HttpServletRequest request,Model model){

        User user = (User) request.getSession().getAttribute("user");  //经过拦截器处理后Session会携带User、token信息
        if(user == null){   //用户不存在时，直接返回发布页面，提示用户未登录
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
