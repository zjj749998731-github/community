package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Notification;
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

    @Autowired
    NotificationMapper notificationMapper;


    @Transactional
    public void addComment(Comment comment , User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {      // 先只是单纯地判断该评论是否存在父类(父类指的是一级评论是否从属于某个问题，二级评论是否从属于某个一级评论)，但该参数不一定与数据库中的parent_id一致
            throw new MyException(MyExceptionCodeEnum.TARGET_PARAM_NOT_FOUND);  // "未选中任何问题或评论进行回复！"
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {  // 判断评论的类型，即：是问题的一级评论还是二级评论的一级评论
            throw new MyException(MyExceptionCodeEnum.TYPE_PARAM_WRONG);                 // "评论类型错误或不存在！"
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //获取回复的一级评论，即该评论为二级评论  (在数据库中：当`type`为2的时候，`parent_id`代表一级回复在回复表中的id )
            Comment parentComment = commentMapper.getCommentById(comment.getParentId());
            if (parentComment == null) {  //一级评论不存在了
                throw new MyException(MyExceptionCodeEnum.COMMENT_NOT_FOUND);     //"操作的评论已不存在！"
            }
            //添加二级回复
            commentMapper.addComment(comment);
            synchronized (this){
                //如果添加了二级回复，则其所对应的一级回复中的评论数要加1
                commentMapper.addCommentCount(parentComment);
                //根据一级评论的parentId获取对应问题
                Question question = questionMapper.findQuestionById(parentComment.getParentId());
                if (question == null) {
                    throw new MyException(MyExceptionCodeEnum.QUESTION_NOT_FOUND);
                }
                /**
                 * 现用户回复了原问题的一级回复，则原问题用户要添加通知
                 * 不论是通知是一级回复或者是二级回复，都跳转到对应的问题页面上
                 * 所以，通知的type不论是1还是2，outerId都是问题本身的id
                 */
                this.addNotification(comment,parentComment.getCommentatorId(),commentator.getName(),question.getTitle(),question.getId());
            }

        } else {
            //获取回复的问题本身，即该评论为一级评论  (在数据库中：当`type`为1的时候，`parent_id`代表问题表中该问题的id )
            Question question = questionMapper.findQuestionById(comment.getParentId());
            if (question == null) {   //问题本身不存在了
                throw new MyException(MyExceptionCodeEnum.QUESTION_NOT_FOUND);     //"操作的问题已不存在！"
            }
            //添加一级回复
            commentMapper.addComment(comment);
            synchronized (this) {
                //如果添加了一级回复，则其所对应的问题中的评论数要加1
                questionMapper.addComment(question);
                /**
                 * 现用户回复了原问题的用户，则原问题用户要添加通知
                 * 不论是通知是一级回复或者是二级回复，都跳转到对应的问题页面上
                 * 所以，通知的type不论是1还是2，outerId都是问题本身的id
                 */
                this.addNotification(comment,question.getCreatorId(),commentator.getName(),question.getTitle(),question.getId());
            }
        }

    }

    /**
     * 在添加回复的同时，要进行通知的操作
     * @param comment
     * @param receiver
     * @param notifierName
     * @param outerTitle
     */
    private void addNotification(Comment comment , Integer receiver,String notifierName,String outerTitle,Integer outerId){
        //如果接受者和当前用户是同一个人，则不需要通知操作
        if (receiver == comment.getCommentatorId()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        if (comment.getType() == 1){
            notification.setType(NotificationTypeEnum.REPLY_QUESTION.getType());
        }else if (comment.getType() == 2){
            notification.setType(NotificationTypeEnum.REPLY_COMMENT.getType());
        }
        notification.setOuterId(outerId);
        notification.setOuterTitle(outerTitle);           //此视频的想法是：通知都是针对问题发布者而言的 ---- 一级回复当然要通知问题发布者；二级回复也是要通知问题发布者，而不是一级回复的发布者
        notification.setNotifier(comment.getCommentatorId());   //现用户（当前登录者，即一级回复的创建者 或 二级回复的创建者）
        notification.setNotifierName(notifierName);      //现用户的名字
        notification.setReceiver(receiver);                     //原用户（              问题的创建者     或 一级回复的创建者）
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.addNotification(notification);
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
