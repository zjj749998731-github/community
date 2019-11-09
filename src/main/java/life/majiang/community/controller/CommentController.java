package life.majiang.community.controller;

import life.majiang.community.dto.CommentAddDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    //提交问题的一级回复或者一级回复的二级回复
    @ResponseBody  //@ResponseBody会把Java对象自动地序列化成JSON对象，并发送到前端Ajax的回调方法里
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object replyFirstComment(@RequestBody CommentAddDTO commentAddDTO, HttpServletRequest request) {  //@RequestBody会把前端请求传进来的JSON数据自动地反序列化成Java对象
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //throw new MyException(MyExceptionCodeEnum.USER_NOT_LOGIN);  //页面会跳转到error页面，对用户不友好
            return ResultDTO.errorOf(new MyException(MyExceptionCodeEnum.USER_NOT_LOGIN));
        }
        if (commentAddDTO == null || StringUtils.isBlank(commentAddDTO.getContent())) {
            return ResultDTO.errorOf(new MyException(MyExceptionCodeEnum.COMMENT_IS_EMPTY));
        }

        Comment comment = new Comment();
        comment.setParentId(commentAddDTO.getParentId());
        comment.setContent(commentAddDTO.getContent());
        comment.setType(commentAddDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentatorId(user.getId());
        commentService.addComment(comment,user);
        return ResultDTO.successOf();
    }


    //展示一级回复的二级回复
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> replySecondComment(@PathVariable(name = "id")Integer id){
        List<CommentDTO> secondComments = commentService.findCommentsByTargetId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDTO.successOf(secondComments);
    }


}
