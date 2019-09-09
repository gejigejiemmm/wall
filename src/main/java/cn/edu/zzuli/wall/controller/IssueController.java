package cn.edu.zzuli.wall.controller;

import cn.edu.zzuli.wall.bean.Issue;
import cn.edu.zzuli.wall.bean.Msg;
import cn.edu.zzuli.wall.service.IssueService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "发布动态相关文档")
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    IssueService issueService;

    @ApiOperation(value = "获取所有的动态", httpMethod = "GET")
    @RequestMapping("/getAll")
    @ResponseBody
    public PageInfo getAllIssues(Integer p,@RequestParam(value = "uId",required = false) Integer uId){
        return issueService.getAllIssues(p,uId);
    }

    @ApiOperation(value = "发布动态", httpMethod = "POST")
    @ResponseBody
    @RequestMapping("/add")
    public Msg addIssue(Issue issue){
        issue = issueService.addIssue(issue);
        if(issue.getFromId() != 0)
            return Msg.success().add("issue",issue);
        return Msg.fail().add("error","发送失败,请先登录");
    }

    @ApiOperation(value = "动态的转发", httpMethod = "POST")
    @RequestMapping("/forward")
    @ResponseBody
    public Msg forward(Integer isId, @ApiIgnore() HttpSession session){
        Map<String,Issue> issueMap = issueService.forward(isId,session);
        if(issueMap != null && issueMap.containsKey("oldIssue") && issueMap.containsKey("newIssue"))
            return Msg.success().add("issueFowd",issueMap);
        return Msg.fail().add("error","转发失败");
    }

    @ApiOperation(value = "点赞数 + 1",httpMethod = "POST")
    @RequestMapping("/tags")
    @ResponseBody
    public Msg tags(Integer isId){
        if(issueService.Tags(isId)){
            return Msg.success();
        }
        return Msg.fail();
    }

}
