package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @ResponseBody  //@ResponseBody会把Java对象自动地序列化成JSON对象，并发送到前端
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {  //@RequestBody会把前端请求传进来的JSON数据自动地反序列化成Java对象
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
           //throw new MyException(MyExceptionCodeEnum.USER_NOT_LOGIN);  //页面会跳转到error页面，对用户不友好
            return ResultDTO.errorOf(new MyException(MyExceptionCodeEnum.USER_NOT_LOGIN));
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentatorId(user.getId());
        commentService.addComment(comment);
        return ResultDTO.successOf();
    }
}
