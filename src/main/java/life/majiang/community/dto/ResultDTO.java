package life.majiang.community.dto;

import life.majiang.community.exception.MyException;

import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO successOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
        return resultDTO;
    }

    //该方法可以携带数据返回
    public static <T> ResultDTO successOf(T data) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
        resultDTO.setData(data);
        return resultDTO;
    }

//    public static ResultDTO errorOf(MyExceptionCodeEnum myExceptionCodeEnum) {
//        ResultDTO resultDTO = new ResultDTO();
//        resultDTO.setCode(myExceptionCodeEnum.getCode());
//        resultDTO.setMessage(myExceptionCodeEnum.getMessage());
//        return resultDTO;
//    }

    public static ResultDTO errorOf(MyException e) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(e.getCode());
        resultDTO.setMessage(e.getMessage());
        return resultDTO;
    }
}
