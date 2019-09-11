package cn.edu.zzuli.wall.service;

import cn.edu.zzuli.wall.bean.Circle;
import cn.edu.zzuli.wall.mapper.CircleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CircleService {

    @Autowired
    CircleMapper circleMapper;

    public List<Circle> getCircles(){
        return circleMapper.getCircles();
    }

    @Transactional
    public boolean addCirlceFollow(Integer fromId,Integer followCId){
        try {
            if(fromId != null || followCId != null){
                circleMapper.addFollows(fromId,followCId);
                circleMapper.uptFollows(followCId);
            }
        }catch (DataAccessException d){
            return false;
        }
        return true;
    }

    public boolean jugeIsFollow(Integer fromId,Integer followCId){
        if(circleMapper.jugeIsFollow(fromId,followCId) == 0
                || fromId == null || followCId == null)
            return false;
        return true;
    }
}
