package com.xcphoenix.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author      xuanc
 * @date        2019/8/8 下午6:18
 * @version     1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private int code;
    private String msg;
    private Object data;

}
