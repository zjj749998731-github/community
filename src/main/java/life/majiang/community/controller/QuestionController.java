package life.majiang.community.controller;

import life.majiang.community.model.Question;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id, Model model){
        //累加阅读数
        questionService.incView(id);
        Question question = questionService.findQuestionById(id);
        model.addAttribute("question",question);
        return "question";
    }



}
