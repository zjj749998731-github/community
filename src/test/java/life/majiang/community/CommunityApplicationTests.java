package life.majiang.community;

import life.majiang.community.dto.AccessTokenDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityApplicationTests {

    @Autowired
    AccessTokenDTO accessTokenDTO;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;  //JdbcTemplate类可以直接操作数据库,执行增删改查

    @Test
    public void contextLoads() {
        System.out.println(accessTokenDTO.getClient_id());
        System.out.println(accessTokenDTO.getClient_secret());
        System.out.println(accessTokenDTO.getRedirect_uri());
    }

    @Test
    public void testConnect() throws SQLException {
        System.out.println("------->" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("------->" + connection);
        connection.close();
    }

    @Test
    public void testData() throws SQLException {
        List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from `user` ");
        System.out.println(users.get(0));

        /**
         * 以下代码说明当查询结果没有对应的类时,会用Map<String, Object>封装数据
         * 其中:String为字段名,Object为对应的值
         */
//        Map<String, Object> map = users.get(0);
//        Set<Map.Entry<String, Object>> entries = map.entrySet();
//        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
//        while(iterator.hasNext()){
//            Map.Entry<String, Object> next = iterator.next();
//            System.out.println(next.getKey() + ":" + next.getValue());
//        }

    }

}
