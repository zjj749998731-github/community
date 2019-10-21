package life.majiang.community.model;

import lombok.Data;

@Data
public class Question {
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


     //User类中的属性备用，因为我没有用到QuestionDTO类(使用QuestionDTO更好)
//    private String accountId;
    private String name;
    private String avatarUrl;
//    private String token;
}
