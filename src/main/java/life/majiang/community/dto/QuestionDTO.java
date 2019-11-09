package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

/**
 * DTO是专门用来从后台向前端封装数据并传输的类
 * 视频采用该QuestionDTO，而我没有用到此类（注：以后开发要改用DTO类）
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer creatorId;
    private Long gmtCreate;
    private Long gmtModified;
    private String tag;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    //视频采用这种方式，我是在Question类中添加了private String avatarUrl;属性来直接获取User中的avatarUrl属性
    private User user;
}
