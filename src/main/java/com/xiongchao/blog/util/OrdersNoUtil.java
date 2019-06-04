package com.xiongchao.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by cachee on 5/3/2018.
 */
public class OrdersNoUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

    private static Random random = new Random();

    public static String ordersNo() {
        String num = sdf.format(new Date());
        num += (random.nextInt(8888) + 1111);
        return num;
    }
}
