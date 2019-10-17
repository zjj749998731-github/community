package life.majiang.community.service;

import life.majiang.community.dto.PageMsgDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 视频采用该QuestionService，而我没有用到此类
 */
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
}
