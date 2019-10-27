package life.majiang.community.enums;

public enum CommentTypeEnum {
    QUESTION(1), //问题的评论
    COMMENT(2)   //评论的评论
    ;

    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType(){
        return this.type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }
}
