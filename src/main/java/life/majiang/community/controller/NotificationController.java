package life.majiang.community.controller;

import com.alibaba.fastjson.JSONArray;
import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PageMsgDTO;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    /**
     * Ajax是在同一页面进行异步局部刷新的
     * 本项目中：Ajax用在一级评论和二级评论的提交中
     * 以下的Ajax只是实现了异步局部刷新"最新回复"后面的那个小数字（自己写的，可以实现功能），但不能跳转问题页面
     */
//    @ResponseBody
//    @GetMapping("/ajaxRead")
//    public Object ajaxRead(HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");  //经过拦截器处理后Session会携带User、token信息
//        if(user == null){
//            return "redirect:/";
//        }
//        String idStr = request.getParameter("id");
//        Integer id = Integer.valueOf(idStr);
//        notificationService.ajaxRead(id,user);
//        Integer unReadCount = notificationService.countNotification(user.getId());
//        Map<String,Integer> result =  new HashMap<>();
//        result.put("unReadCount",unReadCount);
//        return JSONArray.toJSONString(result);
//    }


    /**
     *  不论是通知是一级回复或者是二级回复，都跳转到对应的问题页面上
     *  所以，通知的type不论是1还是2，outerId都是问题本身的id
     */
    @GetMapping("/notification/{id}")
    public String readNotification(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");  //经过拦截器处理后Session会携带User、token信息
        if(user == null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.readNotification(id,user);
        if (notificationDTO.getType() == NotificationTypeEnum.REPLY_QUESTION.getType() ||
                notificationDTO.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()  ){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }
    }

}
