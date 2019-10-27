package life.majiang.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 这个类实现ErrorController后，会覆盖ErrorMvcAutoConfiguration里面的BasicErrorController，即BasicErrorController不起作用了
 * 在这里该类的作用是：由于BasicErrorController不起作用了，我们要对其他未能处理的异常做统一处理
 */

@Controller //停用此注解，则BasicErrorController就起作用了，一般我们会使用默认的BasicErrorController ---> 一是实现ErrorController这种方式太复杂了,二是BasicErrorController自带有许多自适应等功能
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class MyErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }


    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, Model model) {
        HttpStatus status = this.getStatus(request);
        if(status.is4xxClientError()){
            model.addAttribute("code","4XX");
            model.addAttribute("message","客户端出现问题");
        }
        if(status.is5xxServerError()){
            model.addAttribute("code","5XX");
            model.addAttribute("message","服务器端发生错误，转发到/error,由自定义的MyErrorController进行处理");
        }
        return  new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
