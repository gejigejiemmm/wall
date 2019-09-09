package cn.edu.zzuli.wall.service;

import cn.edu.zzuli.wall.bean.Issue;
import cn.edu.zzuli.wall.bean.User;
import cn.edu.zzuli.wall.mapper.IssueMapper;
import cn.edu.zzuli.wall.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IssueService {

    @Autowired
    IssueMapper issueMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 获取全部的动态信息，包括发布的用户的信息
     * @param p 页数
     * @param uId 加入该参数则为 查找特定的 用户动态
     * @return
     */
    public PageInfo getAllIssues(Integer p,Integer uId){
        HashMap<String,Object> issuesInfo = new HashMap<>();
        if(uId != null){
            issuesInfo.put("uId",uId);
        }
        PageHelper.startPage(p,5);
        List<Issue> issues = issueMapper.getAllIssues(issuesInfo);
        PageInfo info = new PageInfo(issues);
        return info;
    }

    /**
     * 发布新的动态
     * @param issue
     * @return 返回一个带有 id 的 issue 对象
     */
    public Issue addIssue(Issue issue){
        if(issue != null){
            issueMapper.addIssue(issue);
        }
        //需要从数据库中读取 发布该动态的用户信息加以显示
        User user = userMapper.getUserByUId(issue.getFromId());
        if(user != null){
            issue.setUser(user);
        }else{
            issue.setFromId(0);//如果为 0 说明用户没有登陆
        }
        //数据库中默认的值为0,但是接收的参数中并没有该信息
        issue.setLike(0);
        issue.setShare(0);
        return issue;
    }

    /**
     * 动态的 点赞数增加 ---
     * @param isId issue的 isId 动态的 id
     */
    public boolean Tags(Integer isId){
        //使用id来更改 issue 的 点赞数
        if(isId != null && isId != 0){
            return issueMapper.tags(isId);
        }
        return false;
    }

    /**
     * 动态的转发  1.0
     * 转发可以评论，鸽子
     * 可以添加表情 鸽子
     * @param isId issue的 isId 动态的 id
     * @param session 用来获取用户信息
     */
    public Map<String,Issue> forward(Integer isId, HttpSession session){
        //用来存放转发与被转发的 动态信息
        Map<String,Issue> issueMap = new HashMap<>();

        //从数据库中获取该 动态的 所有信息
        Issue oldIssue = issueMapper.getIssueBYIsId(isId);
        Integer shares = oldIssue.getShare();
        issueMap.put("oldIssue",oldIssue);

        //获取当前登陆的用户
        User user = (User) session.getAttribute("user");
        if(isId == null ||user == null || oldIssue == null){
            return null;
        }

        //使用当前用户发布新的信息
        //并存入map 和 数据库
        Issue newIssue = updForwardIssue(oldIssue,user);
        issueMapper.addIssue(newIssue);
        issueMap.put("newIssue",newIssue);

        //该动态的转发数 +1
        issueMapper.share(isId);
        //issueMap.get("oldIssue").setShare(shares + 1);
        return issueMap;
    }


    /**
     * 转发与被转发的动态信息 并不一致
     * 对用户 需要发布的新动态 进行调整
     * @param oldIssue 被转发的动态
     * @param user 发布新动态的用户信息
     * @return 发布的动态
     */
    public Issue updForwardIssue(Issue oldIssue,User user){
        Issue issue = new Issue();
        issue.setDate(LocalDateTime.now());
        //更换为新发布动态的 用户 uId
        issue.setFromId(user.getUId());
        issue.setUser(user);
        issue.setLike(0);
        issue.setShare(0);
        issue.setContent(oldIssue.getContent());
        issue.setImgsUrl(oldIssue.getImgsUrl());
        return issue;
    }

}
