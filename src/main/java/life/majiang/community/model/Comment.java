package life.majiang.community.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Integer parentId;
    private Integer type;
    private Integer commentatorId;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private String content;
    private Integer commentCount;
}
