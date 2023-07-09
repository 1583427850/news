package xyz.linyh.model.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录请求dto
 */
@Data
public class LoginDto {

    @ApiModelProperty(value ="手机号",required = true)
    private String phone;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
