package life.majiang.community.controller;

import life.majiang.community.dto.PageMsgDTO;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{active}")
    public String toProfile(@PathVariable(name = "active") String active,
                            Model model,
                            HttpServletRequest request,
                            @RequestParam(required = false, name = "page", defaultValue = "1")String  strPage,
                            @RequestParam(required = false, name = "pageSize", defaultValue = "5")String strPageSize) {

        User user = (User) request.getSession().getAttribute("user");  //经过拦截器处理后Session会携带User、token信息
        if(user == null){
            return "redirect:/";
        }
        Integer page = Integer.valueOf(strPage);
        Integer pageSize = Integer.valueOf(strPageSize);
        PageMsgDTO pageMsgDTO = questionService.getMyQuestionList(user.getId(),page,pageSize);
        model.addAttribute("pageMsgDTO", pageMsgDTO);

        if ("questions".equals(active)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(active)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }
}
