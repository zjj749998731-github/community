package life.majiang.community.dto;

import life.majiang.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageMsgDTO {
    List<Question> questionList;
    /**
     * 正式开发中，后端只要返回给前端List<Question>以及总数、当前页、页大小等就可以了。此处，把前端的部分分页功能也写到后端了
     * 下列属性是为了做页面回显的
     */
    private boolean showFirstPage;
    private boolean showPreviousPage;
    private boolean showNextPage;
    private boolean showLastPage;
    private Integer selectedPage;
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();  //分页图标里的数字

    public void setPageMsg(Integer totalPage,Integer page) {
        //为当前页码高亮做准备
        this.setSelectedPage(page);
        //为点击末页时做准备
        this.setTotalPage(totalPage);

        //设置分页图标里的数字
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);  //以当前页为中心，补齐左边3个数字。同时，该数值要插入ArrayList的前部
            }
            if (page + i <= totalPage) {
                pages.add(page + i);  //以当前页为中心，补齐右边3个数字。同时，该数值要插入ArrayList的后部
            }
        }

        //是否展示上一页
        if (page == 1) {
            showPreviousPage = false;  //当前页为1时，< 符号不显示
        } else {
            showPreviousPage = true;   //当前页不为1时，< 符号显示
        }
        //是否展示下一页
        if (page == totalPage) {
            showNextPage = false;  //当前页为末页时，> 符号不显示
        } else {
            showNextPage = true;   //当前页不为末页时，> 符号显示
        }
        //是否展示首页
        if (pages.contains(1)) {
            showFirstPage = false;  //分页图标包含1时，<< 符号不显示
        } else {
            showFirstPage = true;   //分页图标不包含1时，<< 符号显示
        }
        //是否展示末页
        if (pages.contains(totalPage)) {
            showLastPage = false;  //分页图标包含末页时，>> 符号不显示
        } else {
            showLastPage = true;   //分页图标不包含末页时，>> 符号显示
        }

    }
}
