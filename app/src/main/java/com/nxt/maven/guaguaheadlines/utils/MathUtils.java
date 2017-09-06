package com.nxt.maven.guaguaheadlines.utils;

/**
 * Created by Jan Maven on 2017/8/3.
 * Email:cyjiang_11@163.com
 * Description: Copy from Android design library
 */

public class MathUtils {

    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

}

