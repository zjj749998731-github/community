package life.majiang.community.dto;

import life.majiang.community.exception.MyException;

import lombok.Data;

@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO successOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
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
