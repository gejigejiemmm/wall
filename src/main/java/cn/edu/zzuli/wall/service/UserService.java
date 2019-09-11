package cn.edu.zzuli.wall.service;

import cn.edu.zzuli.wall.bean.User;
import cn.edu.zzuli.wall.mapper.UserMapper;
import cn.edu.zzuli.wall.utils.BaseUtils;
import cn.edu.zzuli.wall.utils.OSSClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private OSSClientUtil ossClientUtil;

    /**
     * 如果用户点进去查看用户信息时要获取的信息
     * @param uId 查看的 id
     * @param sessionId 登陆用户的id
     * @return
     */
    public Map<String,Object> findUserInfo(Integer uId,Integer sessionId){
        if(uId == null || sessionId == null)
            return null;
        //首先获取用户信息
        Map<String ,Object> info = new HashMap<>();
        try {
            User user = userMapper.getUserByUId(uId);
            user = removePasswd(user);
            info.put("user", user);
            //获取当前用户的关注数，点赞数等基本信息
            info.put("initInfo", userMapper.initUserInfo(uId));
            if(userMapper.jugeIsFollow(uId,sessionId) == 0){
                info.put("isFollow",false);
            }else {
                info.put("isFollow",true);
            }
        }catch (DataAccessException d){
            return null;
        }
        return info;
    }

    /**
     * 获取当前用户点赞数,关注数，粉丝数
     * @param uId
     * @return
     */
    public HashMap<Integer,Integer> getLikes(Integer uId){
        if(uId != null){
           return userMapper.initUserInfo(uId);
        }
        return null;
    }

    /**
     * 关注用户
     * @param fromId
     * @param followUId
     * @return
     */
    public boolean follow(Integer fromId,Integer followUId){
        if(followUId == null || fromId == null)
            return false;
        try {
            userMapper.follow(fromId,followUId);
        }catch (DataAccessException d){
            return false;
        }
        return true;
    }

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
     * 移除 用户 中的密码和用户字段信息
     * @param user
     * @return
     */
    public User removePasswd(User user){
        if(user != null){
            user.setPassword(null);
            user.setUsername(null);
        }
        return user;
    }
}
