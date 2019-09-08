package cn.edu.zzuli.wall.mapper;

import cn.edu.zzuli.wall.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    public User getUserByName(@Param("username") String username);

    public User getUserByUId(@Param("uId")Integer uId);

    public boolean addUser(User user);
}
