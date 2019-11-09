$(document).ready(function () {

        /**
         * 提交问题的一级回复
         */
        $(".btn-comment").click(function () {
            var questionId = $("#question_id").val(); //$("#parent_id").value; 报错undefined
            var content = $(".comment").val();
            console.log(questionId); //在浏览器的console元素中查看
            console.log(content);
            replyQuestionOrComment(questionId,1,content);
        })


        //以下开闭二级回复的写法会对全部的二级回复起作用，需要配合id="btn-secondComment + 序号"进行改善
        // $("#comment").click(function () {
        //     $(".collapse").toggle();
        // });


    }
);

/**
 * 对一级回复和二级回复的统一处理
 * @param targetId
 * @param type
 * @param content
 */
function replyQuestionOrComment(targetId,type,content) {
    if(!content){
        alert("输入内容不能为空！");
        return; //这里return的作用：直接在前端结束，不会进入后台了
    }

    $.ajax({
        url: "/comment",
        type: "POST",
        contentType: "application/json",   //更改响应头里的Content-Type
        data: JSON.stringify({"parentId": targetId, "content": content, "type": type}),  //在JS中发送JSON对象所使用的方法:该方法会将JavaScript对象转换为字符串
        dataType: "json",
        success: callback,
        error: function (result) {
            alert("服务异常，请重新回复！");
        }
    });
}
//上述Ajax的回调方法
function callback(result) {
    console.log(result);
    if (result.code == 200) {   //评论提交成功
        //$("#comment_section").hide();
        window.location.reload(); //添加成功后刷新页面，即：重新发送一遍/question/{id}来获取最新数据
    } else {
        if (result.code == 4002) {  //提交评论时，用户还未登录
            var doLogin = confirm(result.message);
            if (doLogin) {
                window.open("https://github.com/login/oauth/authorize?client_id=8543c922478e7c7fe5ca&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                //将某些服务器用不到的信息直接存储到浏览器中去，使用localStorage 和 sessionStorage
                window.localStorage.setItem("closeable", "true");  //关闭新打开的页面
            }
        } else {
            alert(result.message);
        }
    }
}


/**
 * 开闭二级回复
 */
function showSecondComment(e) {
    var id = e.getAttribute("data-id");
    var secondComment = $("#comment-" + id);

    //在<span>评论</span>这个标签里自定义一个属性data-collapse="in"，用来作为二级回复开闭的标志
    //collapse空是展开状态还是不空是展开状态，要看<span>评论</span>标签里有没有写了data-collapse="in"，因为取决于e.getAttribute("data-collapse");最开始能否取到值
    var collapse = e.getAttribute("data-collapse");
    if (collapse){  //直接写collapse是不为空的意思。二级回复已展开,把它关闭
        secondComment.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("comment-active");
    }else {
        $.getJSON("/comment/"+id , function (data) {
            //console.log(data);

            var secondCommentBody = $("#comment-" + id);
            if (secondCommentBody.children().length > 1){   //如果页面之前已加载过了，就不需要重新获取数据了
                //二级回复已关闭,把它展开
                secondComment.addClass("in");
                e.setAttribute("data-collapse","in");
                e.classList.add("comment-active");
            }else {
                //循环获取对应的二级回复
                $.each(data.data.reverse(), function (index, comment) {
                    //console.log(comment);

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "img-rounded headPic",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<span/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "comment-menu",
                    }).append($("<span/>", {
                        "style": "float: right;color: #999999",
                        "html": moment(comment.gmtCreate).format("YYYY-MM-DD")
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 secondCommentList",
                    }).append(mediaElement);

                    secondCommentBody.prepend(commentElement);
                });

                //二级回复已关闭,把它展开
                secondComment.addClass("in");
                e.setAttribute("data-collapse", "in");
                e.classList.add("comment-active");
            }
        });
    }
}


/**
 * 提交一级回复的二级回复
 */
function replySecondComment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    replyQuestionOrComment(commentId,2,content);
}



/**
 * 点击标签时可以放入标签框中
 */
function selectTag(e) {
    var tagValue = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(tagValue) != -1 ){
        $("#tag").val(previous);
    }else {
        if (previous){
            $("#tag").val(previous + ',' + tagValue);
        }else {
            $("#tag").val(tagValue);
        }
    }
}


/**
 * 展示标签库
 */
function showSelectTags() {
   // $("#tags").css({"display":"block"});
    $("#tags").show();
    $("#msg").hide();
}

/**
 * 关闭标签库
 */
function closeSelectTags() {
    //$("#tags").hide();
}


/**
 * 以下的Ajax只是实现了异步局部刷新"最新回复"后面的那个小数字（自己写的，可以实现功能），但不能跳转问题页面
 */
function readNotification(e) {
    var id = e.getAttribute("data-notificationId");
    $.ajax({
        url: "/ajaxRead",
        type: "GET",
        contentType: "application/json",   //更改响应头里的Content-Type
        data: "id="+id,
        dataType: "json",
        success: function (result) {
            //console.log(result);
           $("#advice").html(result.unReadCount);
        },
        error: function (result) {
            alert("服务异常，请重新回复！");
        }
    });
}