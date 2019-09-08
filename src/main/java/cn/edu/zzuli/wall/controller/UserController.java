package cn.edu.zzuli.wall.controller;

import cn.edu.zzuli.wall.bean.Msg;
import cn.edu.zzuli.wall.bean.User;
import cn.edu.zzuli.wall.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@Controller
@Api(tags = "用户相关文档")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "获取当前登陆状态的用户信息",httpMethod = "GET")
    @RequestMapping("/getUser")
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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

    @ApiIgnore
    public User getUser(HttpSession session){
        //获取当前 seesion 中存在deseesion
        User user = (User) session.getAttribute("user");
        return user;
    }

    @ApiIgnore
    @RequestMapping("/index")
    public String toIndex(){
        return "index";
    }

}
