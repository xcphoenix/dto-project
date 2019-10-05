package com.xcphoenix.dto.crawler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import us.codecraft.webmagic.Spider;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/5 下午12:14
 */
@Slf4j
// @Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
class CrawlerTest {

    @Autowired
    private ElemeProcessor elemeProcessor;

    @Test
    void crawlerProcess() {
        Integer crawlerCount = 0, crawlerTimes = 10;

        String initUrl = "https://h5.ele.me/restapi/shopping/v3/restaurants?extras[]=&extras[]=&extra_filters=home&order_by=0&rank_id=&terminal=h5";

        for (; crawlerCount < crawlerTimes; crawlerCount++) {
            int offset = 0;
            int limit = 8;
            String[] location = GeoUtil.randomLonLat(108.63, 108.86, 34.21, 34.257);
            String url = initUrl + "&" + "offset=" + offset + "&" + "limit=" + limit + "&"
                    + "longitude=" + location[0] + "&" + "latitude=" + location[1];
            System.out.println("================= Crawler start... " + (crawlerCount + 1) + " times =================");
            log.info("[request url] " + url);

            Spider.create(elemeProcessor)
                    .addUrl(url)
                    .thread(1)
                    .run();
        }
    }
}
