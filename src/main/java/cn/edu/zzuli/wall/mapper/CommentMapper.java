package cn.edu.zzuli.wall.mapper;

import cn.edu.zzuli.wall.bean.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 获取 该用户下的所有信息，按点赞降序，时间升序
     * @param isId
     * @return
     */
    public List<Comment> getCommentByIsId(Integer isId);
}
