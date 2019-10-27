package life.majiang.community.service;

import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;


    public void addComment(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {      // 先只是单纯地判断该评论是否存在父类(父类指的是一级评论是否从属于某个问题，二级评论是否从属于某个一级评论)，但不一定与数据库中的parent_id一致
            throw new MyException(MyExceptionCodeEnum.TARGET_PARAM_NOT_FOUND);  // "未选中任何问题或评论进行回复！" --- 有两层意思：一是ParentId为空不存在(此处判断了)，二是ParentId虽有值但与数据库中不符(下面进行判断)
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {  // 判断评论的类型，即：是问题的一级评论还是二级评论的一级评论
            throw new MyException(MyExceptionCodeEnum.TYPE_PARAM_WRONG);                 // "评论类型错误或不存在！"
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复一级评论，即该评论为二级评论
            Comment dbComment = commentMapper.getCommentByParentId(comment.getParentId());
            if (dbComment == null) {  //一级评论不存在了(即:ParentId虽有值但与数据库中不符)
                throw new MyException(MyExceptionCodeEnum.COMMENT_NOT_FOUND);     //"操作的评论已不存在！"
            }
            commentMapper.addComment(comment);
        } else {
            //回复问题本身，即该评论为一级评论
            Question question = questionMapper.findQuestionById(comment.getParentId());
            if (question == null) {   //问题本身不存在了(即:ParentId虽有值但与数据库中不符)
                throw new MyException(MyExceptionCodeEnum.QUESTION_NOT_FOUND);     //"操作的问题已不存在！"
            }
            commentMapper.addComment(comment);
            synchronized (this){
                questionMapper.addComment(question);
            }

        }

    }


}
