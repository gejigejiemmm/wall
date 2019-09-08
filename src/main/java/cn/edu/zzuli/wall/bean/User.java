package cn.edu.zzuli.wall.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="user对象",description="用户对象user")
public class User {
    @ApiModelProperty(value="用户ID",dataType = "int",example = "1")
    private Integer uId;
    @ApiModelProperty(value="用户性别")
    private String sex;
    @ApiModelProperty(value="头像的url")
    private String avataUrl;
    @ApiModelProperty(value="用户昵称")
    private String nickName;
    @ApiModelProperty(value="账号")
    private String username;
    @ApiModelProperty(value="密码")
    private String password;

    public User(){}

    public User(Integer uId, String sex, String avataUrl, String nickName, String username, String password) {
        this.uId = uId;
        this.sex = sex;
        this.avataUrl = avataUrl;
        this.nickName = nickName;
        this.username = username;
        this.password = password;
    }
}
