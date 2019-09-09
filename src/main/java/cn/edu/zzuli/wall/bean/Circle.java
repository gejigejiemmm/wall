package cn.edu.zzuli.wall.bean;

import lombok.Data;

@Data
public class Circle {

    private Integer ccId;
    private String ccTitle;
    private String ccInfo;
    private Integer ccFollow;
    private String ccAvata;

    public Circle(){}

    public Circle(Integer ccId, String ccTitle, String ccInfo, Integer ccFollow, String ccAvata) {
        this.ccId = ccId;
        this.ccTitle = ccTitle;
        this.ccInfo = ccInfo;
        this.ccFollow = ccFollow;
        this.ccAvata = ccAvata;
    }
}
