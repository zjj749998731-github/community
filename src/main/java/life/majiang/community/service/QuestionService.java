package life.majiang.community.service;

import life.majiang.community.dto.PageMsgDTO;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    public PageMsgDTO getQuestionList(Integer page, Integer pageSize) {
        Integer totalCount = questionMapper.getTotalCount(); //总数
        Integer totalPage;  //总页数
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }
        //设置页码异常时的值
        if(page <= 0){
            page = 1;
        }else if(page > totalPage){
            page = totalPage;
        }
        int offset = (page - 1) * pageSize;  //任意页的第一行，即起始偏移量
        List<Question> questions = questionMapper.findQuestions(offset, pageSize);

        PageMsgDTO pageMsgDTO = new PageMsgDTO();
        pageMsgDTO.setQuestionList(questions);
        pageMsgDTO.setPageMsg(totalPage,page); //根据总数、总页数、当前页码、页面大小，获取分页的其他信息
        return  pageMsgDTO;
    }

    public PageMsgDTO getMyQuestionList(Integer id, Integer page, Integer pageSize) {
        Integer totalCount = questionMapper.getMyTotalCount(id); //我的问题总数
        Integer totalPage;  //总页数
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }
        //设置页码异常时的值
        if(page <= 0){
            page = 1;
        }else if(page > totalPage && totalPage > 0){
            page = totalPage;
        }
        Integer offset = (page - 1) * pageSize;  //任意页的第一行，即起始偏移量
        List<Question> questions = questionMapper.findMyQuestions(id,offset, pageSize); //对我的问题进行分页
        PageMsgDTO pageMsgDTO = new PageMsgDTO();
        pageMsgDTO.setQuestionList(questions);
        pageMsgDTO.setTotalCount(totalCount);
        pageMsgDTO.setPageMsg(totalPage,page); //根据总数、总页数、当前页码、页面大小，获取分页的其他信息
        return pageMsgDTO;
    }


    public Question findQuestionById(Integer id) {
        Question question = questionMapper.findQuestionById(id);
        if(question == null){
            throw new MyException(MyExceptionCodeEnum.QUESTION_NOT_FOUND);
        }
        return question;
    }


    public void createOrUpdate(Question question, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");  //经过拦截器处理后Session会携带User、token信息
        if (question.getId() == null) {   //提交新的问题，保存操作
            question.setCreatorId(user.getId());   //取出并保存用户信息
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.addQuestion(question);
        } else { //修改问题，更新操作
            //question.setCreatorId(user.getId());   //为什么不写该行代码，creator_id的值还是之前的值，而不是null？
            question.setGmtModified(System.currentTimeMillis());
            int result = questionMapper.updateQuestion(question);
            if (result == 0) {
                throw new MyException(MyExceptionCodeEnum.QUESTION_NOT_UPDATE);
            }
        }
    }

    public void incView(Integer id) {
        synchronized (this){
            Question question = questionMapper.findQuestionById(id);
            questionMapper.addView(question);
        }
    }
}
