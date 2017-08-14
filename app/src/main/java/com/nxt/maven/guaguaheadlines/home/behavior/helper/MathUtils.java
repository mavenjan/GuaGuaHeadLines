package com.nxt.maven.guaguaheadlines.home.behavior.helper;

/**
 * Created by Jan Maven on 2017/8/3.
 * Email:cyjiang_11@163.com
 * Description: Copy from Android design library
 */

class MathUtils {

    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

}

