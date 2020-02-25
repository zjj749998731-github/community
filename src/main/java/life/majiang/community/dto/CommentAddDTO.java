package life.majiang.community.dto;

import lombok.Data;

@Data
public class CommentAddDTO {
    private Integer parentId;
    private Integer type;
    private String content;
    private Integer commentCount;

}
