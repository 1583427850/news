package xyz.linyh.model.webmedia.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WmLoginDto {
    @ApiModelProperty(value ="手机号",required = true)
    private String name;
    @ApiModelProperty(value = "密码",required = true)
    private String password;

}
