package com.xcphoenix.dto.utils.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.sql.Time;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/13 下午6:24
 */
public class SqlTimeDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object val = parser.parse();
        if (val != null) {
            String timeStr = val.toString();
            //格式为 HH:ss
            if (timeStr.indexOf(":") == 2 && timeStr.length() == 5) {
                return (T) Time.valueOf(val + ":00");
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

}
