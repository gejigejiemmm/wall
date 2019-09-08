package cn.edu.zzuli.wall.service;

import cn.edu.zzuli.wall.bean.Comment;
import cn.edu.zzuli.wall.mapper.CommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    /**
     * 获取该动态的 所有回复
     * @param isId  要查询的 动态id
     * @return List<Comment>
     */
    public PageInfo getAllComments(Integer p , Integer isId){
        PageHelper.startPage(p,10);

        if(isId == 0 || isId == null){
           return null;
        }

        List<Comment> comments = commentMapper.getCommentByIsId(isId);
        PageInfo info = null;
        if(comments != null){
            info = new PageInfo(comments);
        }
        return info;
    }


}
