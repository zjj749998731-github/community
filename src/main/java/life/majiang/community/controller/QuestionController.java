package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.model.Question;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;


    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id, Model model){
        //累加阅读数
        questionService.incView(id);
        //获取问题本身
        Question question = questionService.findQuestionById(id);
        //获取问题的一级回复
        List<CommentDTO> comments = commentService.findCommentsByTargetId(id ,CommentTypeEnum.QUESTION.getType());

        //获取一级回复各自对应的二级回复(自己想要获取的，没用上)
//        List<List<CommentDTO>> secondComments = new ArrayList<>();
//        Iterator<CommentDTO> iterator = comments.iterator();
//        while (iterator.hasNext()){
//            CommentDTO firstComment = iterator.next();
//            long firstCommentIdLong = firstComment.getId();
//            int firstCommentId = (int)firstCommentIdLong;
//            List<CommentDTO> secondComment = commentService.findCommentsByTargetId(firstCommentId ,CommentTypeEnum.COMMENT.getType());
//            if (secondComment != null && !secondComment.isEmpty()){  //不为空说明该条一级回复有二级回复
//                secondComments.add(secondComment);
//            }
//        }
//        System.out.println(comments);
//        System.out.println(secondComments);
//        model.addAttribute("secondComments",secondComments);

        //获取与原问题相关的问题
        List<Question> relatedQuestions = questionService.findRelatedQuestions(question);

        model.addAttribute("question",question);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);

        return "question";
    }



}
