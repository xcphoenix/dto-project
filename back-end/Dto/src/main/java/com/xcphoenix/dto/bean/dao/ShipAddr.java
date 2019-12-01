package com.xcphoenix.dto.bean.dao;

import com.alibaba.fastjson.annotation.JSONField;
import com.xcphoenix.dto.validator.ValidateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 收货地址
 * @author      xuanc
 * @date        2019/8/7 下午8:29
 * @version     1.0
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ShipAddr {

    private Long shipAddrId;
    private Long userId;

    @NotBlank(message = "联系人不能为空", groups = {ValidateGroup.AddData.class})
    private String contact;

    @NotBlank(message = "联系电话不存在", groups = {ValidateGroup.AddData.class})
    @Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式错误")
    private String phone;

    @JSONField(deserialize = false)
    private Integer countryId;

    @JSONField(serialize = false)
    private String countryCode;

    @Length(max = 100, message = "地址长度超出范围")
    private String address;

    @Pattern(regexp = "^[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})$",
            message = "无效的经度")
    private BigDecimal addrLng;
    @Pattern(regexp = "^[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})$",
            message = "无效的纬度")
    private BigDecimal addrLat;

    private String geohash;

    @Length(max = 50, message = "地址详情长度超出范围")
    private String addressDetail;

    @Size(max = 3, min = 0, message = "未知的类型")
    private Integer tagType;

    @JSONField(deserialize = false)
    private String tagName;

    private Boolean isDefault;

    public ShipAddr dataConvert() {
        String[] typeNames = new String[] {null, "家庭", "公司", "学校"};
        this.tagName = typeNames[this.tagType];
        return this;
    }

}
