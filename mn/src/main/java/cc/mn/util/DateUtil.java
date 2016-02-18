package cc.mn.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-29
 * Time: 09:56
 * Version 1.0
 *
 * 日期格式化
 */

public class DateUtil {

    //例：2015
    public static String FORMAT_Y = "yyyy";

    //例：09:58
    public static String FROMAT_HM = "HH:mm";

    //例：12-29 10：00
    public static String FORMAT_MDHM = "MM-dd HH:mm";

    //例：2015-12-29
    public static String FORMAT_YMD = "yyyy-MM-dd";

    //例：2015-12-29 10:02
    public static String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

    //例：2015-12-29 10:03:23
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    //精确到完整的时间
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    //精确到完整的时间
    public static String FROMAT_FULL_SN = "yyyyMMddHHmmss.S";

    //中文 例：2015年12月29日
    public static String FROMAT_YMD_CN = "yyyy年MM月dd日";

    //中文 例：2015年12月29日 10时
    public static String FROMAT_YMDH_CN = "yyyy年MM月dd日 HH时";

    //中文 例：2015年12月29日 10时25分
    public static String FROMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分";

    //中文 例：2015年12月29日 10时25分23秒
    public static String FROMAT_YMDHMS_CN = "yyyy年MM月dd日 HH时mm分ss秒";

    //中文 精确时间
    public static String FROMAT_FULL_CN = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";

    public static Calendar calendar = null;
    private static final String FROMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 字符串 转 Date
     * */
    public static Date strToDate(String str){
        return strToDate(str, null);
    }

    /**
     * 字符串 转 Date
     * */
    public static Date strToDate(String str, String format){
        if(TextUtils.isEmpty(str)) {
            return null;
        }

        if(TextUtils.isEmpty(format)){
            format = FROMAT;
        }

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public static Calendar strToCalendar(String str){
        return strToCalendar(str, null);
    }

    public static Calendar strToCalendar(String str, String format){
        Date date = strToDate(str, format);
        if(date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public static String dateToStr(Calendar c){
        return dateToStr(c.getTime());
    }

    public static String dateToStr(Calendar c, String format){
        if(c == null){
            return null;
        }
        return dateToStr(c.getTime(), format);
    }

    public static String dateToStr(Date d){
        return dateToStr(d, null);
    }

    public static String dateToStr(Date d, String format){
        if(d == null){
            return null;
        }

        if(TextUtils.isEmpty(format)){
            format = FROMAT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static String getCurDateStr(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) +1) + "-" +
                c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.HOUR_OF_DAY) + ":" +
                c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
    }

    /**
     * 格式化当前日期
     * */
    public static String getCurDateStr(String format){
        Calendar c = Calendar.getInstance();
        return dateToStr(c, format);
    }

    /**
     * 当前时间，格式化到秒
     * */
    public static String getMillon(long time){
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
    }

    /**
     * 当前时间，格式化到天
     * */
    public static String getDay(long time){
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取毫秒
     * */
    public static String getSMillon(long time){
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);
    }

    /**
     * 增加月数
     *
     * @param date 需要增加月数的时间
     * @param n 增加月数数量
     * */
    public static Date addMonth(Date date, int n){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, n);
        return c.getTime();
    }

    /**
     * 增加天数
     * */
    public static Date addDay(Date date, int n){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     * 获取距现在某一小时的时刻
     * @param h 距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * */
    public static String getNextHour(String format, int h){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(date.getTime() + h * 60 * 60 * 1000);
        return sdf.format(date);
    }

    /**
     * 获取时间戳
     * */
    public static String getTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FULL);
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    /**
     * 获取月份
     * */
    public static int getMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时
     * */
    public static int getHour(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     * */
    public static int getMinute(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取秒数
     * */
    public static int getSecond(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 获取毫秒数
     * */
    public static long getMillis(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 距离今天的天数
     * */
    public static int countDays(String date){
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int)(t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 距离今天的天数
     * */
    public static int countDays(long date){
        long t = Calendar.getInstance().getTime().getTime();
        return (int)(t / 1000 - date / 1000) / 3600 / 24;
    }

    public static Date parse(String strDate){
        return parse(strDate, FORMAT_YMDHMS);
    }

    public static Date parse(String strDate, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try{
            return sdf.parse(strDate);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
