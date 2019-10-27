package life.majiang.community.config;

import life.majiang.community.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 1. 一个配置类相当于一个配置文件，可以配置Servlet、Filter、Listener三大组件、拦截器、国际化区域解析器等等
 * 2. @Component容器类会自动生成并放在Ioc容器中 --- 即：当配置类需要某个容器类的时候，直接从Ioc容器中拿就可以了
 * 3. 使用WebMvcConfigurerAdapter可以扩展SpringMvc的功能
 */
//@EnableWebMvc   //使用此注解用户本人将完全接管SpringMvc，默认的WebMvcAutoConfiguration类将不起作用
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    SessionInterceptor sessionInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器发送 localhost:8081/atguigu   , 依据Thymeleaf模板引擎映射到/templates/success.html
        // registry.addViewController("/atguigu").setViewName("success");
    }

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //在之前的SpringMVC中，对于静态资源的访问，拦截器要去除掉静态资源的访问路径
        //而SpringBoot已经做好了静态资源的映射，无需处理了
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**")
                .excludePathPatterns("https://github.com/login/oauth/authorize","/callback","/logout");
    }






}
