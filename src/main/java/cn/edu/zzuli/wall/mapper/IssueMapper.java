package cn.edu.zzuli.wall.mapper;

import cn.edu.zzuli.wall.bean.Issue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface IssueMapper {

    public List<Issue> getAllIssues(HashMap<String,Object> issuesInfo);

    //获取该用户发布的所有动态
    public List<Issue> getUserAllIssues(@Param("uId")Integer uId);

    public boolean addIssue(Issue issue);

    /**
     * 点赞数 +1
     * @param isId
     * @return
     */
    public boolean tags(@Param("isId") Integer isId);

    /**
     * 转发数 +1
     * @param isId
     * @return
     */
    public boolean share(Integer isId);

    public Issue getIssueBYIsId(Integer isId);
}
