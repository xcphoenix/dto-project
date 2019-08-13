package com.xcphoenix.dto.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * 店铺 - bean
 * @author xuanc
 * @version 1.0
 * @date 2019/8/7 下午4:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Restaurant {

    private Integer restaurantId;
    private Integer userId;

    @NotBlank(message = "店铺名称不能为空")
    private String restaurantName;
    @NotBlank(message = "联系人不能为空")
    private String contactMan;

    @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
    @NotBlank(message = "手机号码不能为空")
    private String restaurantPhone;

    @Length(max = 256, message = "店铺描述超出范围")
    private String restaurantDesc;
    @Length(max = 300, message = "公告字数超出范围")
    private String bulletin;

    /**
     * tags 用于转化为 tagArray，序列化时序列化 tagArray，反序列化时忽略 tagArray
     * tagArray = tags.split(",");
     */
    @JSONField(serialize = false)
    @Length(max = 512, message = "标签超出范围")
    private String tag;

    @JSONField(deserialize = false)
    private String[] tags;

    /**
     * bhStart < bhEnd
     */
    private Time bhStart;
    private Time bhEnd;

    /**
     * Table country 主键 _id
     */
    private Integer countryId;

    @Length(max = 200, message = "地址长度超出范围")
    private String address;
    private BigDecimal addrLng;
    private BigDecimal addrLat;

    @JSONField(serialize = false)
    @Length(max = 1024, message = "图片个数超出范围")
    private String instoreImg;
    private String storeImg;
    private String logo;
    private String bannerImg;

    @JSONField(deserialize = false)
    private String[] instoreImgs;

    private Integer deliveryTime;
    private BigDecimal score;
    private BigDecimal deliveryScore;
    private BigDecimal foodScore;
    private BigDecimal packageScore;

    @Min(value = 0, message = "配送价不能为负")
    private Float deliveryPrice;
    @Min(value = 0, message = "起送价不能为负")
    private Integer minPrice;

    private Integer totalSale;
    private Integer monthSale;

    private Float totalRevenue;
    private Float monthRevenue;
    private Timestamp gmtCreate;

}
