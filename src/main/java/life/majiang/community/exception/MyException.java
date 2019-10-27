package life.majiang.community.exception;

public class MyException extends RuntimeException {
    private Integer code;
    private String message;

//    public MyException(Integer code,String message) {
//        this.code = code;
//        this.message = message;
//    }

    public MyException(ExceptionCodeInterface errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    //我感觉直接用枚举类做形参也行
//    public MyException(MyExceptionCodeEnum errorCode) {
//        this.code = errorCode.getCode();
//        this.message = errorCode.getMessage();
//    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
