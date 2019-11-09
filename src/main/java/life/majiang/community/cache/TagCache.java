package life.majiang.community.cache;

import life.majiang.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    public static List<TagDTO> getTags(){
        List<TagDTO> tagDTOs = new ArrayList<>();

        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript","php","css","html5","java","node.js","python","c++","c","golang","objective-c","typescript","shell","swift","c#","sass","ruby","bash","less","asp.net","lua","scala","rust","erlang","perl","coffeescript","actionscript"));

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存","tomcat","负载均衡","unix","hadoop","windows-server"));

        TagDTO database = new TagDTO();
        database.setCategoryName("数据库和缓存");
        database.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql","sqlserver","postgresql","sqlite"));

        TagDTO delTools = new TagDTO();
        delTools.setCategoryName("开发工具");
        delTools.setTags(Arrays.asList("github","maven","svn"));


        tagDTOs.add(program);
        tagDTOs.add(framework);
        tagDTOs.add(server);
        tagDTOs.add(database);
        tagDTOs.add(delTools);
        return tagDTOs;
    }

    public static String isTagValid(String pageTags){
        String[] split = StringUtils.split(pageTags, ",");
        List<TagDTO> tagDTOs = getTags();

        List<String> tagList = tagDTOs.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalidTag = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return invalidTag;
    }

}
