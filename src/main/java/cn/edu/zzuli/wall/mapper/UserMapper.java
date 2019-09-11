package cn.edu.zzuli.wall.mapper;

import cn.edu.zzuli.wall.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;

@Mapper
public interface UserMapper {

    public User getUserByName(@Param("username") String username);

    public User getUserByUId(@Param("uId")Integer uId);

    public boolean addUser(User user);

    /**
     * 记录 关注信息
     * @param fromId 发起关注的用户
     * @param followUId 被关注的用户
     * @return
     */
    public boolean follow(@Param("fromId")Integer fromId,@Param("followUId")Integer followUId);

    //获取用户总的点赞数,粉丝数，关注数
    public HashMap<Integer,Integer> initUserInfo(Integer uId);

    @Select(" SELECT count( f.fId ) FROM followU f  WHERE fromId = #{fromId} AND followUId = #{followUId}")
    public Integer jugeIsFollow(Integer fromId,Integer followUId);
}
