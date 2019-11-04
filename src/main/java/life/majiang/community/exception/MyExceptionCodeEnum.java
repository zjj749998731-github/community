package life.majiang.community.exception;

public enum MyExceptionCodeEnum implements ExceptionCodeInterface{
    //2xxx:代表问题异常；3xxx：代表评论异常；4xxx：其他
    QUESTION_NOT_FOUND(2001,"你找的问题不存在，请重新查找!"),
    QUESTION_NOT_UPDATE(2002,"更新失败！"),
    QUESTION_NOT_DELETE(2003,"删除失败！"),
    TYPE_PARAM_WRONG(3001,"评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(3002,"操作的评论已不存在！"),
    COMMENT_IS_EMPTY(3003,"评论内容不能为空！"),
    TARGET_PARAM_NOT_FOUND(4001,"未选中任何问题或评论进行回复！"),
    USER_NOT_LOGIN(4002,"用户未登录，请先登录！"),
    SYSTEM_ERROR(4003,"服务器运行时发生异常！")
    ;

    private Integer code;
    private String message;

    MyExceptionCodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
