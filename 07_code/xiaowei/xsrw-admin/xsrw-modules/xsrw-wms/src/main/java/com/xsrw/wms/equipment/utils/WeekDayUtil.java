package com.xsrw.wms.equipment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekDayUtil {
    public static void main(String[] args) throws Exception {
//        getDates("2023-05-06", "2023-06-14","星期一,星期二,星期日");
        List<String> everyDay = findEveryDay("2023-05-06", "2023-06-01",3);
        System.out.println(everyDay);
    }

    /**
     * 获取某一时间段特定星期几的日期
     * @param dateFrom 开始时间
     * @param dateEnd 结束时间
     * @param weekDays 星期
     * @return 返回时间数组
     */
    public static String[] getDates(String dateFrom, String dateEnd, String weekDays) {
        long time = 1l;
        long perDayMilSec = 24 * 60 * 60 * 1000;
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //需要查询的星期系数
        String strWeekNumber = weekForNum(weekDays);
        try {
            dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
            while (true) {
                time = sdf.parse(dateFrom).getTime();
                time = time + perDayMilSec;
                Date date = new Date(time);
                dateFrom = sdf.format(date);
                if (dateFrom.compareTo(dateEnd) <= 0) {
                    //查询的某一时间的星期系数
                    Integer weekDay = dayForWeek(date);
                    //判断当期日期的星期系数是否是需要查询的
                    if (strWeekNumber.indexOf(weekDay.toString())!=-1) {
                        System.out.println(dateFrom);
                        dateList.add(dateFrom);
                    }
                } else {
                    break;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        String[] dateArray = new String[dateList.size()];
        dateList.toArray(dateArray);
        return dateArray;
    }

    //等到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
    public static Integer dayForWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 得到对应星期的系数  星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
     * @param weekDays 星期格式  星期一|星期二
     */
    public static String weekForNum(String weekDays){
        //返回结果为组合的星期系数
        String weekNumber = "";
        //解析传入的星期
        if(weekDays.indexOf(",")!=-1){//多个星期数
            String []strWeeks = weekDays.split(",");
            for(int i=0;i<strWeeks.length;i++){
                weekNumber = weekNumber + "" + getWeekNum(strWeeks[i]).toString();
            }
        }else{//一个星期数
            weekNumber = getWeekNum(weekDays).toString();
        }

        return weekNumber;

    }

    //将星期转换为对应的系数  星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
    public static Integer getWeekNum(String strWeek){
        Integer number = 1;//默认为星期日
        if("星期日".equals(strWeek)){
            number = 1;
        }else if("星期一".equals(strWeek)){
            number = 2;
        }else if("星期二".equals(strWeek)){
            number = 3;
        }else if("星期三".equals(strWeek)){
            number = 4;
        }else if("星期四".equals(strWeek)){
            number = 5;
        }else if("星期五".equals(strWeek)){
            number = 6;
        }else if("星期六".equals(strWeek)){
            number = 7;
        }
        return number;
    }

    /**
     * 传入两个时间范围，返回这两个时间范围内的所有日期，并保存在一个集合中
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public static List<String> findEveryDay(String beginTime, String endTime,Integer day)
            throws Exception {
        //创建一个放所有日期的集合
        List<String> dates = new ArrayList();

        //创建时间解析对象规定解析格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //将传入的时间解析成Date类型,相当于格式化
        Date dBegin = sdf.parse(beginTime);
        Date dEnd = sdf.parse(endTime);

        //将格式化后的第一天添加进集合
        dates.add(sdf.format(dBegin));

        //使用本地的时区和区域获取日历
        Calendar calBegin = Calendar.getInstance();

        //传入起始时间将此日历设置为起始日历
        calBegin.setTime(dBegin);

        //判断结束日期前一天是否在起始日历的日期之后
        while (dEnd.after(calBegin.getTime())) {

            //根据日历的规则:月份中的每一天，为起始日历加一天
            calBegin.add(Calendar.DAY_OF_MONTH, day);

            //得到的每一天就添加进集合
            dates.add(sdf.format(calBegin.getTime()));
            //如果当前的起始日历超过结束日期后,就结束循环
        }
        return dates;
    }

}
