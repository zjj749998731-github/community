package life.majiang.community.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer parentId;
    private Integer type;
    private String content;
}
