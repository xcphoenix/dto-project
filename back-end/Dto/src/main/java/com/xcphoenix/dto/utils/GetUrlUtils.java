package com.xcphoenix.dto.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/10 下午5:48
 */
@Getter
public class GetUrlUtils {

    private String url;
    private Map<String, String> params;

    public GetUrlUtils(String url) {
        int index = url.indexOf('?');
        this.params = new HashMap<>();
        if (index != -1) {
            this.url = url.substring(0, index);

            String paramStr = url.substring(index + 1);
            String[] paramArr = paramStr.split("&");
            for (String paramItem : paramArr) {
                String[] kv = paramItem.split("=");
                if (kv.length > 2) {
                    throw new RuntimeException("参数解析错误");
                }
                if (kv.length == 1) {
                    params.put(kv[0], null);
                } else {
                    params.put(kv[0], kv[1]);
                }
            }
        } else {
            this.url = url;
        }
    }

    private String getParamsStr() {
        StringBuilder paramsStr = new StringBuilder();
        for (Map.Entry<String, String> entry: params.entrySet()) {
            if (entry.getValue() == null) {
                paramsStr.append(entry.getKey());
            }
            else {
                paramsStr.append(entry.getKey()).append("=").append(entry.getValue());
            }
            paramsStr.append("&");
        }
        paramsStr.deleteCharAt(paramsStr.length() - 1);
        return paramsStr.toString();
    }

    public String getValue(String key) {
        return params.get(key);
    }

    public void setValue(String key, Object value) {
        params.put(key, String.valueOf(value));
    }

    public boolean delKey(String key) {
        return params.remove(key) != null;
    }

    @Override
    public String toString() {
        return url + "?" + getParamsStr();
    }

}
