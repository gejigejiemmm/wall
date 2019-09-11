package cn.edu.zzuli.wall.mapper;

import cn.edu.zzuli.wall.bean.Circle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CircleMapper {

    @Select("SELECT * FROM circle")
    public List<Circle> getCircles();

    @Update("UPDATE circle SET ccFollow = ccFollow + 1 WHERE ccId = #{ccId}")
    public Integer uptFollows(Integer ccId);

    @Insert("INSERT INTO followC(fromId,followCId) VALUES(#{fromId},#{followCId})")
    public boolean addFollows(Integer fromId,Integer followCId);

    @Select(" SELECT count( f.fId ) FROM followC f  WHERE fromId = #{fromId} AND followCId = #{followCId}")
    public Integer jugeIsFollow(Integer fromId,Integer followCId);
}
