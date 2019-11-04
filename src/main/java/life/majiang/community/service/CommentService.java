package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;


    @Transactional
    public void addComment(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {      // 先只是单纯地判断该评论是否存在父类(父类指的是一级评论是否从属于某个问题，二级评论是否从属于某个一级评论)，但该参数不一定与数据库中的parent_id一致
            throw new MyException(MyExceptionCodeEnum.TARGET_PARAM_NOT_FOUND);  // "未选中任何问题或评论进行回复！"
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {  // 判断评论的类型，即：是问题的一级评论还是二级评论的一级评论
            throw new MyException(MyExceptionCodeEnum.TYPE_PARAM_WRONG);                 // "评论类型错误或不存在！"
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复一级评论，即该评论为二级评论  (在数据库中：当`type`为2的时候，`parent_id`代表一级回复在回复表中的id )
            Comment dbComment = commentMapper.getCommentById(comment.getParentId());
            if (dbComment == null) {  //一级评论不存在了
                throw new MyException(MyExceptionCodeEnum.COMMENT_NOT_FOUND);     //"操作的评论已不存在！"
            }
            commentMapper.addComment(comment);

            //如果要添加二级回复，则其所对应的一级回复中的评论数要加1
            synchronized (this){
                Comment parentComment = commentMapper.getCommentById(comment.getParentId());
                commentMapper.addCommentCount(parentComment);
            }
        } else {
            //回复问题本身，即该评论为一级评论  (在数据库中：当`type`为1的时候，`parent_id`代表问题表中该问题的id )
            Question question = questionMapper.findQuestionById(comment.getParentId());
            if (question == null) {   //问题本身不存在了
                throw new MyException(MyExceptionCodeEnum.QUESTION_NOT_FOUND);     //"操作的问题已不存在！"
            }
            commentMapper.addComment(comment);
            synchronized (this) {
                questionMapper.addComment(question);
            }
        }

    }


    public List<CommentDTO> findCommentsByTargetId(Integer id, Integer type) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        List<Comment> commentList = commentMapper.findCommentsByTargetId(id,type);  //获取问题的一级回复或一级回复的二级回复
        if(commentList == null || commentList.size() == 0){
            return commentDTOList;
        }


//        for (Comment comment : commentList) {
//            User user = userMapper.findById(comment.getCommentatorId());
//            CommentDTO commentDTO = new CommentDTO();
//            BeanUtils.copyProperties(comment, commentDTO);
//            commentDTO.setUser(user);
//            commentDTOList.add(commentDTO);
//        }


        Set<Integer> commentators = commentList.stream().map(comment -> comment.getCommentatorId()).collect(Collectors.toSet());   //获取不重复的评论者Id
        Iterator<Integer> iterator = commentators.iterator();  //获取这些评论者对应的用户
        List<User> users = new ArrayList<>();
        while(iterator.hasNext()){
            Integer uid = iterator.next();
            User user = userMapper.findById(uid);
            users.add(user);
        }
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        commentDTOList = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentatorId()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOList;
    }


}
