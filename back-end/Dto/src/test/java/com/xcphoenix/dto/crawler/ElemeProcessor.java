package com.xcphoenix.dto.crawler;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.bean.FoodCategory;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.mapper.FoodCategoryMapper;
import com.xcphoenix.dto.mapper.FoodMapper;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.service.GeoCoderService;
import com.xcphoenix.dto.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/3 下午8:43
 */
@Component
@Slf4j
public class ElemeProcessor implements PageProcessor {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private GeoCoderService geoCoderService;

    private static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    private static String getTel() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

     private Long createRUser() {
        log.info("创建用户...");
        Long tmpUserId;
        do {
            User tmpUser = new User();
            tmpUser.setUserPhone(getTel());
            tmpUser.setUserPassword("123456");
            tmpUser.setUserName(RandomStringUtils.randomAlphanumeric(2) + System.currentTimeMillis());
            tmpUserId = loginService.registerByPhonePass(tmpUser);
        } while (tmpUserId == null);
        log.info("用户创建成功...");
        return tmpUserId;
    }

    private Long makeTmpRst(JSONObject rst) throws ParseException {
        log.info("创建店铺...");
        // log.info("rst => " + rst.toJSONString());
        Restaurant tmpRst = new Restaurant();
        tmpRst.setUserId(createRUser());
        tmpRst.setRestaurantPhone(getTel());
        tmpRst.setContactMan("testMan");
        Double lat = rst.getDouble("latitude");
        Double lng = rst.getDouble("longitude");
        tmpRst.setRestaurantDesc(rst.getString("description"));
        tmpRst.setDeliveryPrice(rst.getFloat("float_delivery_fee"));
        tmpRst.setMinPrice(rst.getFloat("float_minimum_order_amount").intValue());
        tmpRst.setRestaurantName(rst.getString("name"));
        tmpRst.setAddress(rst.getString("address"));
        try {
            tmpRst.setDeliveryRange(rst.getFloat("closing_count_down") / 1000);
        } catch (NullPointerException npe) {
            log.info("获取配送距离失败...使用默认配送范围");
            tmpRst.setDeliveryRange(5.321f);
        }
        tmpRst.setBulletin(rst.getString("promotion_info"));

        JSONArray flavors = rst.getJSONArray("flavors");
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < flavors.size(); i++) {
            String name = flavors.getJSONObject(i).getString("name");
            tags.append(',').append(name);
        }
        tags.deleteCharAt(0);
        tmpRst.setTag(tags.toString());
        tmpRst.setAddrLat(BigDecimal.valueOf(lat));
        tmpRst.setAddrLng(BigDecimal.valueOf(lng));
        tmpRst.setCountryId(geoCoderService.getAreaId(BigDecimal.valueOf(lat), BigDecimal.valueOf(lng)));
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lng, 12);
        tmpRst.setGeohash(geoHash.toBase32());
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        // 设置开业时间
        tmpRst.setBhStart(new Time(format.parse("09:00").getTime()));
        tmpRst.setBhEnd(new Time(format.parse("21:00").getTime()));
        tmpRst.setLogo("http://demo.com/logo.png");
        tmpRst.setBannerImg("http://demo.com/banner.png");
        tmpRst.setStoreImg("http://demo.com/store.png");
        tmpRst.setInstoreImg("http://demo.com/instore.png");
        log.info("店铺创建成功");

        restaurantMapper.addRestaurant(tmpRst);
        log.info("店铺添加成功");
        return tmpRst.getRestaurantId();
    }

    private void makeTmpFood(JSONObject food, FoodCategory category) throws ParseException {
        log.info("创建商品信息...");
        // log.info("food => " + food.toJSONString());

        Food tmpFood = new Food();
        tmpFood.setRestaurantId(category.getRestaurantId());
        tmpFood.setCategoryId(category.getCategoryId());
        tmpFood.setName(food.getString("name"));
        tmpFood.setDescription(food.getString("description"));
        tmpFood.setSellingPrice(food.getFloat("lowest_price"));

        JSONArray specFoods = food.getJSONArray("specfoods");
        if (specFoods == null) {
            tmpFood.setOriginalPrice(food.getFloat("lowest_price"));
        } else {
            tmpFood.setOriginalPrice(specFoods.getJSONObject(0).getFloat("original_price"));
        }
        if (tmpFood.getOriginalPrice() == null) {
            tmpFood.setOriginalPrice(food.getFloat("lowest_price"));
        }
        tmpFood.setTotalNumber(1000);
        tmpFood.setResidualAmount(1000);
        tmpFood.setCoverImg("http://demo.com/food.png");

        log.info("商品创建成功");
        foodMapper.addFood(tmpFood);
        log.info("商品添加成功");
    }

    private Set<String> resId = new HashSet<>(110);

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
            .setCharset("UTF-8")
            .addHeader("Content-Type", "application/json")
            // set your cookie
            .addHeader("Cookie", System.getProperty("Cookie"));

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    @Override
    public void process(Page page) {
        // log.info(page.getRawText());
        if (page.getUrl().regex("https://h5.ele.me/restapi/shopping/v3/restaurants.+").match()) {
            log.info("## page type 1 ##");

            List<String> nextUrl = new ArrayList<>();
            JSONArray items = JSONObject.parseObject(page.getJson().toString()).getJSONArray("items");
            for (int i = 0; i < items.size(); i++) {
                String restaurantId = items.getJSONObject(i)
                        .getJSONObject("restaurant")
                        .getString("id");
                if (!resId.contains(restaurantId)) {
                    resId.add(restaurantId);
                    nextUrl.add(restaurantId);
                }
            }
            String sourceUrl = page.getUrl().toString();
            String target = sourceUrl.substring(0, sourceUrl.indexOf('?') + 1);
            String params = null;
            String lat = null, lng = null;
            if (sourceUrl.indexOf('?') != -1) {
                params = sourceUrl.substring(sourceUrl.indexOf('?') + 1);
                String[] paramArr = params.split("&");
                for (int j = 0; j < paramArr.length; j++) {
                    if (paramArr[j].contains("offset")) {
                        String[] kv = paramArr[j].split("=");
                        kv[1] = (Integer.valueOf(Integer.parseInt(kv[1]) + 8)).toString();
                        paramArr[j] = StringUtils.join(kv, '=');
                    } else if (paramArr[j].contains("latitude")) {
                        String[] kv = paramArr[j].split("=");
                        lat = kv[1];
                    } else if (paramArr[j].contains("longitude")) {
                        String[] kv = paramArr[j].split("=");
                        lng = kv[1];
                    }
                }
                params = StringUtils.join(paramArr, "&");
            }

            String idTag = "USERID=";
            String cookieStr = System.getProperty("Cookie");
            String userId = cookieStr.substring(cookieStr.indexOf(idTag) + idTag.length());
            userId = userId.substring(0, userId.indexOf(';'));

            for (int i = 0; i < nextUrl.size(); i++) {
                String nextUrlElement = "https://h5.ele.me/pizza/shopping/restaurants/" + nextUrl.get(i)
                        + "/batch_shop?user_id=" + userId + "&code=0.26042264537648885&extras=%5B%22activities%22%2C%22albums%22%2C%22license%22%2C%22identification%22%2C%22qualification%22%5D&terminal=h5"
                        + "&latitude=" + lat + "&longitude=" + lng;
                nextUrl.set(i, nextUrlElement);
            }

            log.info("本次获得" + nextUrl.size() + "条店铺url记录");
            if (nextUrl.size() != 0) {
                page.addTargetRequests(nextUrl);
                log.info("next url ==> " + JSON.toJSONString(nextUrl));
            }

            // check if has next data
            if (!(JSONObject.parseObject(page.getJson().toString()).getBoolean("has_next"))) {
                page.setSkip(true);
                log.info("无数据..跳过");
            } else {
                page.addTargetRequest(target + params);
            }
        } else {
            log.info("## page type 2 ##");

            JSONObject jsonObject = JSON.parseObject(page.getJson().toString());
            JSONObject rst = jsonObject.getJSONObject("rst");
            Long rstId = null;
            try {
                rstId = makeTmpRst(rst);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            JSONArray menus = jsonObject.getJSONArray("menu");
            for (int i = 0; i < menus.size(); i++) {
                JSONObject menu = menus.getJSONObject(i);
                FoodCategory foodCategory = new FoodCategory();
                foodCategory.setRestaurantId(rstId);
                foodCategory.setName(menu.getString("name"));
                foodCategory.setDescription(menu.getString("description"));
                foodCategoryMapper.addNewCategory(foodCategory);
                log.info("商品分类添加成功!");
                Long categoryId = foodCategory.getCategoryId();

                JSONArray foods = menu.getJSONArray("foods");
                for (int j = 0; j < foods.size(); j++) {
                    JSONObject food = foods.getJSONObject(j);
                    try {
                        makeTmpFood(food, foodCategory);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    @Override
    public Site getSite() {
        return site;
    }

}
