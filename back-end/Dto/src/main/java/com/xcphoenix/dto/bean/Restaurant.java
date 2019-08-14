package com.xcphoenix.dto.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.xcphoenix.dto.util.SqlTimeDeserializer;
import com.xcphoenix.dto.util.SqlTimeSerializer;
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
 *
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

    @Length(max = 50, message = "店铺名称字数超出范围")
    @NotBlank(message = "店铺名称不能为空")
    private String restaurantName;
    @NotBlank(message = "联系人不能为空")
    private String contactMan;

    @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
    @NotBlank(message = "手机号码不能为空")
    private String restaurantPhone;

    @Length(max = 256, message = "店铺描述字数超出范围")
    private String restaurantDesc;
    @Length(max = 300, message = "公告字数超出范围")
    private String bulletin;

    /**
     * tag 用于转化为 tags，序列化时序列化 tags，反序列化时忽略 tags
     * tags = tag.split(",");
     */
    @JSONField(serialize = false)
    @Length(max = 100, message = "标签超出范围")
    private String tag;

    @JSONField(deserialize = false)
    private String[] tags;

    /**
     * bhStart < bhEnd
     */
    // @JSONField(deserializeUsing = SqlTimeDeserializer.class, serializeUsing = SqlTimeSerializer.class)
    private Time bhStart;
    // @JSONField(deserializeUsing = SqlTimeDeserializer.class, serializeUsing = SqlTimeSerializer.class)
    private Time bhEnd;

    /**
     * Table country 主键 _id
     */
    private int countryId;

    @Length(max = 200, message = "地址长度超出范围")
    private String address;
    private BigDecimal addrLng;
    private BigDecimal addrLat;

    /**
     * 只是存储在数据库中，不进行序列化和反序列化的操作
     */
    @JSONField(serialize = false, deserialize = false)
    @Length(max = 1024, message = "图片长度超出范围")
    private String instoreImg;
    private String storeImg;
    private String logo;
    private String bannerImg;

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

    public void dataConvertToShow() {
        tags = tag.split(",");
        instoreImgs = instoreImg.split(",");
    }

}
