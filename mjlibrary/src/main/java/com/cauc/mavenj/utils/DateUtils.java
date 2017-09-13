package com.cauc.mavenj.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description: 时间相关管理工具
 */

public class DateUtils {

    public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    static Calendar calendar = Calendar.getInstance();
    private static List<String> weeklist = new ArrayList<>();

    public static int getHour() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        return Integer.parseInt(format.format(date));
    }

    public static String getWeek(int day) {

        if (day == 7) {
            return "周六";
        } else if (day == 6) {
            return "周五";
        } else if (day == 5) {
            return "周四";
        } else if (day == 4) {
            return "周三";
        } else if (day == 3) {
            return "周二";
        } else if (day == 2) {
            return "周一";
        } else if (day == 1) {
            return "周日";
        }
        return null;

    }

    public static String getdate1() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public static String getdate2() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getweatherdate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(date);
    }

    public static List<String> getdatalist() {
        List<String> llist = new ArrayList<String>();
        List<String> weeklists = getoneweekdate();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int nextday;
        for (int i = 0; i < 7; i++) {
            nextday = day + i;
            if (nextday > 7) {
                nextday = nextday - 7;
            }
            if (i == 0) {
                llist.add("今天" + "\n" + weeklists.get(i));
            } else {
                llist.add(getWeek(nextday) + "\n" + weeklists.get(i));
            }
        }

        return llist;

    }

    public static List<String> getoneweekdate() {
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            if (i > 0) {
                cal.add(Calendar.DATE, 1);
            }
            weeklist.add(new SimpleDateFormat("MM-dd").format(cal.getTime()));
        }
        return weeklist;
    }

    public static boolean isday() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        int hour = Integer.valueOf(format.format(date));
        System.out.println("hour------------>" + hour);
        if (hour >= 18 || hour <= 3) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取短时间格式
     *
     * @param dateStr "2016-01-06T09:37:04"
     * @return
     */
    public static String getShortTime(String dateStr) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(dateStr);
            str=getShortTime(date.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 获取短时间格式
     *
     * @return
     */
    public static String getShortTime(long millis) {

        Date date = new Date(millis);
        Date curDate = new Date();


        String str = "";

        long durTime = curDate.getTime() - date.getTime();


        int dayStatus = calculateDayStatus(date, new Date());

        if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (durTime < ONE_HOUR_MILLIONS) {
            str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
        } else if (dayStatus == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        } else if (dayStatus == -1) {
            str = "昨天" + DateFormat.format("HH:mm", date);
        } else if (isSameYear(date, curDate) && dayStatus < -1) {
            str = DateFormat.format("MM-dd", date).toString();
        } else {
            str = DateFormat.format("yyyy-MM", date).toString();
        }



//        if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
//            str = "刚刚";
//        } else if (durTime < ONE_HOUR_MILLIONS) {
//            str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
//        } else if (durTime < ONE_HOUR_MILLIONS * 24) {
//            str = durTime / ONE_HOUR_MILLIONS + "小时前";
//        } else {
//            Date date = new Date(millis);
//            str = DateFormat.format("MM-dd HH:mm", date) + "";
//        }
        return str;
    }

    /**
     * @param dateStr "2016-01-06T09:37:04"
     * @return
     */
    public static Date getDate(String dateStr) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    public static String toDateStr(Date date) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取时间倒计时
     *
     * @param dateStr "2016-01-06T09:37:04"
     * @return
     */
    public static String getTimeDown(String dateStr) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(dateStr);
            Date curDate = new Date();

            long durTime = curDate.getTime() - date.getTime();
            int dayStatus = calculateDayStatus(date, curDate);

            if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
                str = "刚刚";
            } else if (durTime < ONE_HOUR_MILLIONS) {
                str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
            } else if (dayStatus == 0) {
                str = durTime / ONE_HOUR_MILLIONS + "小时前";
            } else if (dayStatus == -1) {
                str = "昨天" + DateFormat.format("HH:mm", date);
            } else if (isSameYear(date, curDate) && dayStatus < -1) {
                str = DateFormat.format("MM-dd", date).toString();
            } else {
                str = DateFormat.format("yyyy-MM", date).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static String getCurrentTimeDown(long currentTimeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date curDate = new Date(currentTimeMillis);
        String str = formatter.format(curDate);
        return getTimeDown(str);
    }

    /**
     * 返回 2016.1.1
     *
     * @return
     */
    public static String getTimePoint(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat tosdf = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date = sdf.parse(dateStr);
            return tosdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 生成时间  时:分:秒
     *
     * @param position
     * @return
     */
    public static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }

    /**
     * 生成时间  时:分'秒''
     *
     * @param position
     * @return
     */
    public static String generateTimeFormatte(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d’%02d’%02d”", hours, minutes,
                    seconds).toString();
        } else if (minutes > 0) {

            return String.format(Locale.US, "%02d’%02d”", minutes, seconds)
                    .toString();
        } else {
            return String.format(Locale.US, "%02d”", seconds)
                    .toString();
        }
    }

    /**
     * 生成时间  秒''
     *
     * @param position
     * @return
     */
    public static String generateSecFormatte(long position) {
        int totalSeconds = (int) (position / 1000);


        return String.format(Locale.US, "%02d”", totalSeconds)
                .toString();
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date one;
        String distanceTime = "-1";
        try {

            one = df.parse(str1);
            Date now = new Date();
            long time1 = one.getTime();
            long nowTime = now.getTime();
            long diff;
            if (time1 > nowTime) {
                diff = time1 - nowTime;
            } else {
                return "-1";
            }
            long day = 0;
            long hour = 0;
            long min = 0;
            long sec = 0;
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            distanceTime = day + "天" + hour + "小时" + min + "分" + sec + "秒";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return distanceTime;

    }


    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }

    public static int calculateDayStatus(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }

    /**
     * "2016-01-06T09:37:04"
     *
     * @param time
     * @return
     */
    public static String getFormatTime(String time) {
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            str = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 返回 0小时0分
     *
     * @param timeMillis 毫秒
     * @return
     */
    public static String getTimeString(long timeMillis) {
        long minutes = timeMillis / ONE_MINUTE_MILLIONS;
        if (minutes < 60) {
            return minutes + "分钟";
        } else {
            long remainder = minutes % 60;
            long hour = minutes / 60;
            if (remainder == 0) {

                return hour + "小时";
            } else {
                return hour + "小时" + remainder + "分";
            }
        }
    }


    /**
     * 根据月日判断星座
     *
     * @param m
     * @param d
     * @return int
     */
    public static String getConstellation(int m, int d) {

        final String[] constellationArr = {"魔羯座", "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

        final int[] constellationEdgeDay = {20, 18, 20, 20, 20, 21, 22, 22, 22, 22, 21, 21};
        int month = m;
        int day = d;
        if (day <= constellationEdgeDay[month - 1]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        //default to return 魔羯
        return constellationArr[11];

    }

    /**
     * 根据出生日期获得年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static String getFormatTime(long timeMin) {
        int min = (int) (timeMin / DateUtils.ONE_MINUTE_MILLIONS);
        StringBuilder time = new StringBuilder();
        //有分钟
        time.append(min > 0 ? min + ":" : "");
        int sec = (int) (timeMin - min * DateUtils.ONE_MINUTE_MILLIONS) / 1000;
        time.append(min > 0 ? sec + "" : sec + "秒");
        return time.toString();
    }

    public static String zeroTime(int time) {
        return time < 10 ? "0" + time : time + "";
    }

}
