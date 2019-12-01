package com.xcphoenix.dto.bean.ao;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author      xuanc
 * @date        2019/11/30 下午8:28
 * @version     1.0
 */
@Data
public class OrderModify {
    @Size(max = 100, message = "地址长度超出限制")
    private String addr;
    @Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式错误")
    private String phone;
    @Size(max = 100, message = "备注长度超出限制")
    private String remark;
}
