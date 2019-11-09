package life.majiang.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private Integer notifier;     //通知发起者的id
    private String notifierName;  //通知发起者的名字
    private String outerTitle;    //发起者所回复的问题标题
    private Integer outerId;      //发起者所回复的问题id
    private Integer type;         //类型可能是问题类型或评论类型
    private String joinStr;       //"回复了问题"或者"回复了评论"
}
