package cn.edu.zzuli.wall.controller;

import cn.edu.zzuli.wall.bean.Msg;
import cn.edu.zzuli.wall.utils.OSSClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Api(tags = "工具接口文档")
@RequestMapping("/utils")
public class UtilsController {

    @Autowired
    OSSClientUtil ossClientUtil;

    @ApiOperation(value = "图片上传接口",httpMethod = "POST")
    @RequestMapping("/upload")
    @ResponseBody
    public Msg avataUpload(MultipartFile imgFile){
        String url = null;
        try{
             url = upload(imgFile);
        }catch (Exception e){
            return Msg.fail().add("error","上传失败");
        }

        return Msg.success().add("url",url);

    }


    /**
     * 上传 图片到阿里云
     * @param file
     * @return
     */
    public String upload(MultipartFile file){
        String url = null;
        try {
            if(file != null){
                //上传图片 并获取图片路径
                url = ossClientUtil.checkImage(file);
            }
        }catch (Exception e){
            System.out.println("图片上传出错");
            url = null;
        }
        return url;
    }

}
