package cn.edu.zzuli.wall.controller;

import cn.edu.zzuli.wall.service.CommentService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
@Api(tags = "动态的回复文档")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ApiOperation(value = "获取该动态下所有的回复",httpMethod = "GET")
    @ResponseBody
    @RequestMapping("/allComments")
    public PageInfo getAllComments(@RequestParam(value = "p",required = false,defaultValue = "1")
                                   @ApiParam(value = "页数",example = "1") Integer p,
                                   @RequestParam(value = "isId", required = true) Integer isId){
        return commentService.getAllComments(p,isId);
    }
}
