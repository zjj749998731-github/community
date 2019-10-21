package life.majiang.community.controller;

import life.majiang.community.dto.PageMsgDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.service.QuestionDTOService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionDTOService questionDTOService;


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false, name = "page", defaultValue = "1")String  strPage,
                        @RequestParam(required = false, name = "pageSize", defaultValue = "5")String strPageSize){

        Integer page = Integer.valueOf(strPage);
        Integer pageSize = Integer.valueOf(strPageSize);
        PageMsgDTO pageMsgDTO = questionService.getQuestionList(page,pageSize);
//        List<QuestionDTO> questions = questionDTOService.getQuestionDTOList(currentPage,pageSize); //视频采用此方式
        model.addAttribute("pageMsgDTO",pageMsgDTO);
        return "index";
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) { //注销
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }


}
