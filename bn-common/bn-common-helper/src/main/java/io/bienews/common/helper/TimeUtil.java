package io.bienews.common.helper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ywd on 2019/12/13.
 */
public class TimeUtil {

    private static final long TEN_MINUTE = 60*10;
    private static final long ONE_HOUR = 60*60;
    private static final long ONE_DAY = 24*60*60;
    private static final long ONE_MINUTE = 60;

    //今天剩余秒数
    public static int getLastSeconds() {
        // 得到今天 晚上的最后一刻 最后时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        int second = (int) (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        if (second <= 0) {
            second = 1;
        }
        return second;
    }

    /**
     * 计算时间差
     * @param start
     * @return
     * @throws ParseException
     */
    public static String getDiff(Date start) {
        long now = new Date().getTime();
        long diff = (now - start.getTime())/1000;//秒
        long rs;
        if (diff>ONE_DAY) {
            rs = diff / ONE_DAY;
            return rs+"天前";
        }else if(diff>ONE_HOUR){
            rs = diff / ONE_HOUR;
            return rs+"小时前";
        }else if (diff>TEN_MINUTE){
            rs = diff / ONE_MINUTE;
            return rs+"分钟前";
        }else {
            return "刚刚";
        }
    }

    public static void main(String[] args) {
        System.out.println(getLastSeconds());
    }
}
