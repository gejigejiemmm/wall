package cn.edu.zzuli.wall.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Issue {
    private Integer isId;
    private String imgsUrl;
    private Integer fromId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private String content;
    private Integer like;
    private Integer share;

    private User user;

    public Issue(){}

    public Issue(Integer isId, String imgsUrl, Integer fromId, LocalDateTime date, String content, Integer like, Integer share) {
        this.isId = isId;
        this.imgsUrl = imgsUrl;
        this.fromId = fromId;
        this.date = date;
        this.content = content;
        this.like = like;
        this.share = share;
    }

    public Issue(Integer isId, String imgsUrl, Integer fromId, LocalDateTime date, String content, Integer like, Integer share, User user) {
        this.isId = isId;
        this.imgsUrl = imgsUrl;
        this.fromId = fromId;
        this.date = date;
        this.content = content;
        this.like = like;
        this.share = share;
        this.user = user;
    }
}
