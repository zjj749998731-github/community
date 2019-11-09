package life.majiang.community.service;

import com.sun.org.apache.regexp.internal.RE;
import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PageMsgDTO;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Notification;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    NotificationMapper notificationMapper;


    public PageMsgDTO<NotificationDTO> getMyNotificationList(Integer id, Integer page, Integer pageSize) {
        PageMsgDTO<NotificationDTO> pageMsgDTO = new PageMsgDTO<>();
        Integer totalCount = questionMapper.getMyTotalCount(id);         //"我的问题"的小数字
        Integer unReadCount = notificationMapper.countNotification(id);  //"最新回复"的小数字
        Integer totalNotificationCount = notificationMapper.getAllNotifications(id); //通知的总数
        Integer totalPage;  //总页数
        if (totalNotificationCount % pageSize == 0) {
            totalPage = totalNotificationCount / pageSize;
        } else {
            totalPage = totalNotificationCount / pageSize + 1;
        }
        //设置页码异常时的值
        if(page <= 0){
            page = 1;
        }else if(page > totalPage && totalPage > 0){
            page = totalPage;
        }
        Integer offset = ( page - 1) * pageSize;  //任意页的第一行，即起始偏移量
        List<Notification> notifications = notificationMapper.findMyNotifications(id,offset, pageSize); //对"最新回复"进行分页
        if (notifications.size() == 0){
            return pageMsgDTO;
        }
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setJoinStr(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOs.add(notificationDTO);
        }
        pageMsgDTO.setDataList(notificationDTOs);
        pageMsgDTO.setTotalCount(totalCount);
        if (unReadCount == 0){
            pageMsgDTO.setUnReadCount(0);
        }else {
            pageMsgDTO.setUnReadCount(unReadCount);
        }
        pageMsgDTO.setPageMsg(totalPage,page); //根据总数、总页数、当前页码、页面大小，获取分页的其他信息
        return pageMsgDTO;
    }


    public NotificationDTO readNotification(Integer id, User user) {
        Notification notification = notificationMapper.findNotificationById(id);
        if (notification == null){
            throw new MyException(MyExceptionCodeEnum.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()){
            throw new MyException(MyExceptionCodeEnum.READ_NOTIFICATION_FAIL);
        }

        //更新通知表中的状态status为1
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.readNotification(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setJoinStr(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    public void ajaxRead(Integer id,User user) {
        Notification notification = notificationMapper.findNotificationById(id);
        if (notification == null){
            throw new MyException(MyExceptionCodeEnum.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()){
            throw new MyException(MyExceptionCodeEnum.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.readNotification(notification);
    }

    public Integer countNotification(Integer id) {
        return notificationMapper.countNotification(id);
    }

}
