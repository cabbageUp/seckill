package com.xxxx.seckill.vo;

import com.xxxx.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

//登录参数
@Data
public class LoginVo {


    @IsMobile
    private String mobile;

    @Length(min=32)
    private String password;
}
