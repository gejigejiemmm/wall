package cn.edu.zzuli.wall.controller;

import cn.edu.zzuli.wall.bean.Msg;
import cn.edu.zzuli.wall.bean.User;
import cn.edu.zzuli.wall.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "用户相关文档")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "获取当前登陆状态的用户信息",httpMethod = "GET")
    @RequestMapping("/getUser")
    public Msg getUserByName(@ApiIgnore() HttpSession session){
        //判断是否处于登陆状态
        User user = getUser(session);
        if(user != null){
            return Msg.success().add("user",user);
        }
        return Msg.fail().add("error","您尚未登陆！");
    }

    @ApiOperation(value = "用户登陆",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" , value = "账号", required = true),
            @ApiImplicitParam(name = "password" , value = "密码",required = true)
    })
    @RequestMapping("/login")
    public Msg login(@RequestParam(value = "username",required = true) String username,
                     @RequestParam(value = "password",required = true)String password,
                     @ApiIgnore() HttpSession session){
        //判断用户信息是否正确
        User user = userService.getUserByName(username,password,session);
        //正确的话放入session里边
        if(user != null){
            return Msg.success().add("user",user);
        }
        return Msg.fail().add("error","账号或者密码错误");
    }

    @ApiOperation(value = "退出登陆状态",httpMethod = "GET")
    @RequestMapping("/loginout")
    public Msg loginOut(@ApiIgnore() HttpSession session){
        //判断是否处于登陆状态
        User user = getUser(session);
        if(user != null){
            session.removeAttribute("user");
            return Msg.success();
        }
        return Msg.fail();
    }

    @ApiOperation(value = "用户注册",httpMethod = "POST")
    @RequestMapping("/register")
    public Msg register(User user ,@ApiParam(value = "头像文件") @RequestParam(value = "avata",required = false)
            MultipartFile avata){
        //先上传头像
        String avataUrl = userService.upload(avata);
        user.setAvataUrl(avataUrl);
        user =  userService.addUser(user);
        if(user == null){
            return Msg.fail().add("error","注册失败，请重新注册");
        }
        return Msg.success().add("user",user);
    }

    @ApiOperation(value = "获取用户的点赞数,关注数,粉丝数",httpMethod = "GET")
    @RequestMapping("/getTags")
    public Msg getTags(Integer uId){
        HashMap<Integer,Integer> likes = userService.getLikes(uId);
        if(likes != null){
            return Msg.success().add("tags",likes);
        }
        return Msg.fail().add("error","获取点赞数失败" );
    }

    @ApiOperation(value = "关注用户",httpMethod = "POST")
    @RequestMapping("/follow")
    public Msg follow(Integer fromId,Integer followUId){
        if(userService.follow(fromId,followUId))
            return Msg.success().add("success","关注成功");
        return Msg.fail().add("error","关注失败" );
    }

    @ApiOperation(value = "用于点进用户界面，查找信息，其中包括用户的点赞数等和是否被关注",httpMethod = "GET")
    @RequestMapping("/findUser")
    public Msg findUserInfo(Integer uId, Integer sessionId){
        Map<String,Object> info = userService.findUserInfo(uId,sessionId);
        if(info != null){
            return Msg.success().add("info",info);
        }
        return Msg.fail().add("error","查找失败");
    }

    @ApiIgnore
    public User getUser(HttpSession session){
        //获取当前 seesion 中存在deseesion
        User user = (User) session.getAttribute("user");
        return user;
    }


}
