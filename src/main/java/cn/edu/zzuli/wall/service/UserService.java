package cn.edu.zzuli.wall.service;

import cn.edu.zzuli.wall.bean.User;
import cn.edu.zzuli.wall.mapper.UserMapper;
import cn.edu.zzuli.wall.utils.BaseUtils;
import cn.edu.zzuli.wall.utils.OSSClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private OSSClientUtil ossClientUtil;

    /**
     * 通过名字获取用户信息
     * @param username
     * @param password
     * @param session
     * @return
     */
    public User getUserByName(String username, String password, HttpSession session){
        if(username != null || !username.equals("")){
            User user = userMapper.getUserByName(username);
            //判断密码是否一致
            password = BaseUtils.Md5Encode(password);

            if(password.equals(user.getPassword())){
                //为了安全考虑，返回给前端一个不带有密码的用户信息
                user = removePasswd(user);
                session.setAttribute("user",user);
                return user;
            }
        }
        return null;
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    public User addUser(User user){
        if(isHaveUsername(user.getUsername())){
            //使用m5对 密码字段进行加盐
            String password = BaseUtils.Md5Encode(user.getPassword());
            user.setPassword(password);
            //添加到数据库
            if(userMapper.addUser(user)){
                //去除返回的密码字段
                return removePasswd(user);
            }
        }
        return null;
    }

    /**
     * 判断用户名是否存在
     * @param username
     * @return true or false
     */
    public boolean isHaveUsername(String username){
        User user = userMapper.getUserByName(username);
        if(user == null)
            return true;
        return false;
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

    /**
     * 移除 用户 中的密码字段信息
     * @param user
     * @return
     */
    public User removePasswd(User user){
        if(user != null){
            user.setPassword(null);
        }
        return user;
    }
}
