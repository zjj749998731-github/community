package life.majiang.community.controller;

import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice  //在SpringMVC中，要想成为异常处理器，要加上此注解
public class MyExceptionHandler {

    //2.转发到/error进行自适应响应效果处理
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e, HttpServletRequest request){
//        //一定要设置自定义的错误状态码4xx或5xx，否则状态码默认为200，不会跳转到异常页面
//        request.setAttribute("javax.servlet.error.status_code",404);
//        //定义异常信息
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("code","user not exist");
//        map.put("message","验证自定义异常数据");
//
//        //将该异常信息放在request域中，以便调用
//        request.setAttribute("ext",map);
//
//        //转发到/error  ---  由BasicErrorController类进行自适应处理
//        return  "forward:/error";
//    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    Object handle(Exception e, Model model, HttpServletRequest request) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回JSON
            if( e instanceof MyException){
                System.out.println("进入application/json中的自定义异常");
                return ResultDTO.errorOf((MyException) e);
            }else{
                System.out.println("进入application/json中的系统异常");
                return ResultDTO.errorOf(new MyException(MyExceptionCodeEnum.SYSTEM_ERROR));
            }
        }else {
            //返回错误页面，默认的text/html
            if( e instanceof MyException){
                System.out.println("进入text/html中的自定义异常");
                MyException myException = (MyException) e;
                model.addAttribute("code",myException.getCode());
                model.addAttribute("message", myException.getMessage());
            }else{
                System.out.println("进入text/html中的系统异常");
                model.addAttribute("code","5xx");
                model.addAttribute("message", MyExceptionCodeEnum.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }







}
