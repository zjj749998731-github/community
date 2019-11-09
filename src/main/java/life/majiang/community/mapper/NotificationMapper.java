package life.majiang.community.mapper;

import life.majiang.community.model.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into `notification`(notifier,receiver,outerId,type,gmt_create,status,notifier_name,outer_title) " +
            "values(#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void addNotification(Notification notification);

    @Select("select * from `notification` where `id` = #{id}")
    Notification findNotificationById(Integer id);

    @Select("select count(1) from `notification` where `receiver` = #{id}")
    Integer getAllNotifications(Integer id);

    @Select("select count(1) from `notification` where `receiver` = #{userId} and status = 0")
    Integer countNotification(Integer userId);

    //"WHERE `receiver` = #{id} and status = 0 " +
    @Select("select * from `notification` " +
            "WHERE `receiver` = #{id} " +
            "order by `gmt_create` desc " +
            "limit #{offset},#{pageSize}")
    List<Notification> findMyNotifications(@Param("id") Integer id,@Param("offset") Integer offset,@Param("pageSize") Integer pageSize);

    @Update("update `notification` set `status` = #{status} where `id` = #{id}")
    void readNotification(Notification notification);


}
