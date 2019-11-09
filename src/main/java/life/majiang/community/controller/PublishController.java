package life.majiang.community.controller;

import life.majiang.community.cache.TagCache;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
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
    QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String showQuestion(@PathVariable(name = "id")Integer id,Model model){
        Question question = questionService.findQuestionById(id);
        model.addAttribute("question",question);
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    @PostMapping("/publish")
    public String addOrUpdateQuestion(Question question, HttpServletRequest request,Model model){
        User user = (User) request.getSession().getAttribute("user");  //经过拦截器处理后Session会携带User、token信息
        model.addAttribute("tags", TagCache.getTags());
        if(user == null){   //用户不存在时，直接返回发布页面，提示用户未登录
            model.addAttribute("msg","用户未登录");
            return "publish";
        }
        String tagValid = TagCache.isTagValid(question.getTag());
        if (StringUtils.isNotBlank(tagValid) ){
            model.addAttribute("msg",tagValid + "标签无效或者含有空标签,请重新输入标签！");
            return "publish";
        }
        questionService.createOrUpdate(question,request);
        model.addAttribute("question",question);
        return "redirect:/";
    }


}
