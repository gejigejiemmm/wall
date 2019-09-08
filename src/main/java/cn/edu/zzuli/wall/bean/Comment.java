package cn.edu.zzuli.wall.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer cId;
    private String content;
    private String imgurl;
    private Integer like;
    private Integer fromId;//用户id
    private Integer toIsId;//回复的动态id
    private LocalDateTime date;

    private User user;//用户信息

    public Comment(){};

    public Comment(Integer cId, String content, String imgurl, Integer like, Integer fromId, Integer toIsId, LocalDateTime date) {
        this.cId = cId;
        this.content = content;
        this.imgurl = imgurl;
        this.like = like;
        this.fromId = fromId;
        this.toIsId = toIsId;
        this.date = date;
    }

    public Comment(Integer cId, String content, String imgurl, Integer like, Integer fromId, Integer toIsId, LocalDateTime date, User user) {
        this.cId = cId;
        this.content = content;
        this.imgurl = imgurl;
        this.like = like;
        this.fromId = fromId;
        this.toIsId = toIsId;
        this.date = date;
        this.user = user;
    }
}
