package cn.edu.zzuli.wall.controller;

import cn.edu.zzuli.wall.bean.Circle;
import cn.edu.zzuli.wall.bean.Msg;
import cn.edu.zzuli.wall.service.CircleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circle")
@Api(tags = "圈子接口文档")
public class CircleController {

    @Autowired
    CircleService circleService;

    @GetMapping("/getCircles")
    public List<Circle> getCircles(){
        return circleService.getCircles();
    }

    @PostMapping("/follow")
    public Msg follow(Integer fromId , Integer followCId){
        if(circleService.addCirlceFollow(fromId,followCId)){
            return Msg.success();
        }
        return Msg.fail();
    }

    @GetMapping("/jugeFollow")
    public boolean judgeFollow(Integer fromId,Integer followCId){
        return circleService.jugeIsFollow(fromId,followCId);
    }
}
