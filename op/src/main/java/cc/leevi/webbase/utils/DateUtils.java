package cc.leevi.webbase.utils;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式转换
     * @param date
     * @param kind 0 (yyyy-MM-dd)    1 (HH:mm:ss)   2 ...(yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static Date stringToDate(String date, int kind){
        if(date!=null&&date.trim().length()!=0){
            String k = null;
            if(kind==0){
                k = "yyyy-MM-dd";
            }else if(kind==9){
                k = "yyyy-MM-dd HH:mm";
            }else if(kind==1){
                k = "HH:mm:ss";
            }else if(kind==2){
                k = "yyyyMMddHHmmss";
            }else if(kind==3){
                k = "yyyyMMdd";
            }else if(kind==101){
                k = "yyyy/MM/dd HH:mm";
            }else {
                k = "yyyy-MM-dd HH:mm:ss";
            }
            Date d = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat(k);
            try {
                d = dateFormat.parse(date);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return d;
        }else{
            return null;
        }
    }

    /**
     * 时间格式转换
     * type 1 yyyy-MM-dd   2 yyyyMMddHHmmss
     * @param date
     * @return
     */
    public static String dateToString(Date date,int type){
        if(date!=null){
            SimpleDateFormat dateFormat = null;
            if(type==1){
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            }else if(type==2){
                dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            }else if(type==3){
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }else if(type==4){
                dateFormat = new SimpleDateFormat("yyyyMMdd");
            }else if(type==9){
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }else if(type==10){
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            }
            return dateFormat.format(date);
        }else{
            return "";
        }
    }

    /**
     * 时间格式转换
     * @param date
     * @return
     */
    public static String dateHourToString(Date date){
        if(date!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(date);
        }else{
            return "";
        }
    }

    /**
     * 获取和之前存储的某个时间比时间相差几分钟
     * @param lastTime 之前存储的某个时间
     * @return
     * @throws Exception
     */
    public static Long getTimeDifferenceMin(String lastTime) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        Date lsDate = df.parse(lastTime);
        long l=nowDate.getTime()-lsDate.getTime();
        long min=((l/(60*1000)));
        return min;
//		long day=l/(24*60*60*1000);
//		long hour=(l/(60*60*1000)-day*24);
//		long min=((l/(60*1000))-day*24*60-hour*60);
//		long s=(l/1000-day*24*60*60-hour*60*60-min*60);
//		System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
    }
    /**
     * 计算时间差
     * @param d1
     * @param d2
     * @return
     */
    public static int dateCalSubtraction(Date d1, Date d2) {
        Calendar d1Cal = Calendar.getInstance();
        Calendar d2Cal = Calendar.getInstance();
        d1Cal.set(Calendar.HOUR_OF_DAY, 0);
        d1Cal.set(Calendar.MINUTE, 0);
        d1Cal.set(Calendar.SECOND, 0);
        d2Cal.set(Calendar.HOUR_OF_DAY, 0);
        d2Cal.set(Calendar.MINUTE, 0);
        d2Cal.set(Calendar.SECOND, 0);
        d1Cal.setTime(d1);
        d2Cal.setTime(d2);
        int rstCal = 0 ;
        if(d2.before(d1)){
            rstCal = ((int) (d1Cal.getTime().getTime() / 1000) - (int) (d2Cal.getTime().getTime() / 1000)) / 3600 / 24;
        }else{
            rstCal = ((int) (d2Cal.getTime().getTime() / 1000) - (int) (d1Cal.getTime().getTime() / 1000)) / 3600 / 24;
        }
        return rstCal;
    }

    /**
     * 获取当天0点
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * 获取当天最后一毫秒
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.SECOND, -1);
        Date zero = calendar.getTime();
        return zero;
    }

    public static String getToday() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String date = "";
        try {
            date = formater.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String d = date + " 00:00:00";
        return d;
    }

    /**
     * 获取本周时间 本周周一
     * 2016-06-06 00:00:00
     *
     * @param
     */
    public static String getWeekTime() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String d = formater.format(cal.getTime()) + " 00:00:00";
        return d;
    }

    /**
     * 获取本月时间
     * return 2016-6-1 00:00:00
     *
     * @param
     */
    public static String getMonths() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String months = year + "-" + month + "-" + "01" + " 00:00:00";
        return months;
    }

    /**
     * 获取年的一月一日
     * <p>
     * 2016-01-01 00:00:00
     *
     * @param
     */
    public static String getYears() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        String years = year + "-" + "01" + "-" + "01" + " 00:00:00";
        return years;
    }

    /**
     * 获取季度
     * 三个月
     *
     * @param
     */
    public static String getThreeMonths() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        month = month - 3;
        String d = year + "-" + month + "-" + "01" + " 00:00:00";
        return d;
    }

    /**
     * 获取半年前日期
     *
     * @return
     */
    public static Date getHalfYear() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -6);
        return cal.getTime();
    }
    public static String format(Date date,String format){
        return DateFormatUtils.format(date,format);
    }
    /**
     * 获取半年前日期
     *
     * @return
     */
    public static String getHalfYearStr() {
        return format(getHalfYear(), "yyyyMMdd");
    }

    /**
     * 获取一年前日期
     *
     * @return
     */
    public static String getOneYear() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return format(cal.getTime(), "yyyyMMdd");
    }

    /**
     * 获取前n个月的月份字符串
     * @param n
     * @return
     */
    public static List<String> getMonths(int n) {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, -i);
            months.add(format(cal.getTime(),"yyyy-MM"));
        }
        return months;
    }

    /**
     * 获取前n个月的月份字符串
     * @param i
     * @return
     */
    public static String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }
    /**
     * 获取指定日期的前n个月的月份字符串
     * @param i
     * @return
     */
    public static String getDateLast12Months(Date date,int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }

    /**
     * 根据指定年月获取前n个月的开始时间
     * @param date yyyy-MM 格式
     * @param i
     * @return
     */
    public static String getLastMonthByYearMonth(String date, int i){

        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);

        return getLastMonthByYearMonth(year, month, i);

    }
    /**
     * 根据指定年月获取前n个月的月份字符串
     * @param year
     * @param month
     * @return
     */
    public static String getLastMonthByYearMonth(int year, int month, int i)
    {

        Calendar cal = Calendar.getInstance();
        int n = i+1;
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-n);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String lastMonth = sdf.format(cal.getTime());

        return lastMonth;
    }


    /**
     * 根据指定年月获取前n个月的开始时间
     * @param date yyyy-MM 格式
     * @param i
     * @return
     */
    public static String getPreviousMonthStartTimeByYearMonth(String date, int i){

        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);

        return getPreviousMonthStartTimeByYearMonth(year, month, i);

    }

    /**
     * 根据指定年月获取前n个月的开始时间
     * @param year
     * @param month
     * @param i
     * @return
     */
    public static String getPreviousMonthStartTimeByYearMonth(int year, int month, int i){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int n = i+1;
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.YEAR,year);
        cale.set(Calendar.MONTH, month-n);
        cale.set(Calendar.DAY_OF_MONTH,1);
        String previousMonthFirstDay = format.format(cale.getTime()) + " 00:00:00";

        return previousMonthFirstDay;

    }

    /**
     * 根据传入的时间和格式返回指定字符格式的时间
     * @param date
     * @param format
     * @return
     */
    public static  String getStringDate(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate=sdf.format(date);
        return strDate;
    }

    public static Date getNow() {
        return new Date();
    }

    public static String getNowStr() {
        return getNowStr(PATTERN);
    }

    public static String getNowStr(String pattern) {
        return format(new Date(),pattern);
    }

    /**
     *  计算当前系统时间和传入的时间相差的月份
     *  LocalDate 为jdk1.8新的时间工具类
     * @param date
     * @return
     */
    public static int betweenMonths(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate startDate = LocalDate.of(year,month,day);
        LocalDate endDate = LocalDate.now();//获取当前系统时间
        Period period = Period.between(startDate, endDate);//比较两个日期变量
        int betweenMonth=period.getMonths();
        return betweenMonth;
    }

    /**
     * 比较上个月的最后一天与传入时间相差的月份
     * @return
     */
    public static int betweenPreviousMonthLastDay(Date date){

        int result = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH) + 1  ;
        LocalDate startDate = LocalDate.of(year,month,day);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH,0);//设置为0号,当前日期既为上个月最后一天
        String previousMonthLastDay = format.format(cale.getTime());

        Period period = Period.between(startDate, LocalDate.parse(previousMonthLastDay));//比较两个日期变量
        if(period.getDays() > 15){
            result = period.getMonths() + 1;
        }
        return result;

    }


    /**
     * 本年在职位月份
     * @return
     */
    public static int caculateDuringCompanyMonthsOneYear(Date date){

        int result;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH) + 1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        LocalDate startDate = LocalDate.of(year,month,day);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
        String previousMonthLastDay = format.format(cale.getTime());
        Period period = Period.between(startDate, LocalDate.parse(previousMonthLastDay));//比较两个日期变量
        if(0 == period.getYears()){
            result =  period.getMonths();
        }else{
            result = LocalDate.parse(previousMonthLastDay).getMonthValue();
        }

        if(period.getDays() > 15){
            result++;
        }
        return result;
    }

    /**
     * 比较本月的第一天与传入时间相差的月份
     * @return
     */
    public static int betweenThisMonthFirstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate startDate = LocalDate.of(year,month,day);

        Period period = Period.between(startDate, LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));//比较两个日期变量
        return period.getMonths();

    }

    /**
     * 获取月份字符串
     * @return
     */
    public static String getMonth() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }

    /**
     *  获取指定的年份
     * @param a
     * @return
     */
    public static String getLastYear(int a) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR-a);
        return String.valueOf(year);
    }

    /**
     * java8(别的版本获取2月有bug) 获取某月最后一天的23:59:59
     * @return
     */
    public static String getLastDayOfMonth(String datestr) {
        if (StringUtils.isBlank(datestr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = strToDateNotDD(datestr);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(dates);
    }


    /**
     * 将短时间格式字符串转换为时间 yyyy-MM( 2017-02)
     * @param strDate
     * @return
     */
    public static Date strToDateNotDD(String strDate) {
        if (StringUtils.isBlank(strDate)) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取某年第一天的零点时间 2017-01-01 00:00:00
     * @param year
     */
    public static String getProFirstDay(int year){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,-year);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND,0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    /**
     * 获取某年的最后一天的最后一秒 2017-12-31 23:59:59
     * @param year
     * @return
     */
    public static String getProLastDay(int year,int month){
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.YEAR,-year);
        ca.set(Calendar.MONTH, 11);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至0
        ca.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至0
        ca.set(Calendar.MINUTE, 59);
        //将秒至0
        ca.set(Calendar.SECOND,59);
        //将毫秒至0
        ca.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(ca.getTime());
    }

    /**
     * 获取上一个月的开始时间
     * @return
     */
    public static String getPreviousMonthStartTime(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH)-1);
        cale.set(Calendar.DAY_OF_MONTH,1);
        String previousMonthLastDay = format.format(cale.getTime()) + " 00:00:00";

        return previousMonthLastDay;

    }

    /**
     * 获取上一个月的第15天
     * @return
     */
    public static String getPreviousMonthMiddleTime(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH)-1);
        cale.set(Calendar.DAY_OF_MONTH, 15);
        String previousMonthLastDay = format.format(cale.getTime()) + " 23:59:59";
        return previousMonthLastDay;

    }

    /**
     * 获取上一个月的最后时间
     * @return
     */
    public static String getPreviousMonthEndTime(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH,0);
        String previousMonthLastDay = format.format(cale.getTime()) + " 23:59:59";
        return previousMonthLastDay;

    }

    /**
     * date转string
     * @param date
     * @return
     */
    public static String date2String(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime stringDate = LocalDateTime.ofInstant(instant, zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(stringDate);
    }

    /**
     * date转string
     * @param date
     * @return
     */
    public static String date2YearMonthDayString(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime stringDate = LocalDateTime.ofInstant(instant, zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dtf.format(stringDate);
    }

    /**
     * String转date
     * @param stringDate
     * @return
     */
    public static Date string2Date(String stringDate){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate,dtf);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);

    }

    /**
     * String转date
     * @param stringDate
     * @return
     */
    public static Date yearMonthString2Date(String stringDate){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate,dtf);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);

    }

    /**
     * 判断是传入的时间和系统时间是否是同一年
     * @param date
     * @return
     */
    public static boolean isThisYear(Date date){
        boolean flag=true;
        Date sysDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(sysDate);
        int year = cal.get(Calendar.YEAR);
        String sysYear=String.valueOf(year);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate=sdf.format(date).substring(0,4);
        if(!sysYear.equals(strDate)){
            flag=false;
        }
        return flag;
    }

    /**
     * 当年在职月数需要根据入司时间和薪资月份计算
     * 如果薪资月份是上年的12月，那么需要计算上年在职月数
     * 入司时间必须早于薪资或者考核的月份
     * @return
     */
    public static int thisYearBeOnTheJobMos(String personEntryDate,String calculateMonth){
        //Date parmDate=null;
        int months=0;//最终返回的月数
        String[] personEntrys=personEntryDate.split("-");
        String[] calculateDates=calculateMonth.split("-");
        //如果入职时间和计算的时间是同一年那么取这两个时间的差
        if(personEntrys[0].equals(calculateDates[0])){//如果这两个时间是同一年
            months=getMonthSpace(personEntryDate,calculateMonth,false);//计算这两个时间相差的月数
        }else{
            //如果不是同一年那么开始时间为计算时间所在年份的1月1号
            String calculateDate=calculateDates[0]+"-01-01";
            months=getMonthSpace(calculateDate,calculateMonth,false);//计算这两个时间相差的月数
        }
        return months;
        //判断入司时间和薪资考核计算的时间是否当年
//        if(isThisYear(personEntryDate)){
//            months=getMonthSpace();
//        }else{//不是当年 从当年的1月1号开始计算
//            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                parmDate =dateFormat1.parse(getProFirstDay(0));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        months= betweenMonths(parmDate);
//        return months;
    }

    /**
     *  获取指定的前几个月的月份
     * @param date
     * @param i
     * @param isAddString
     * @return 例如 2018-08
     */
    public static String getAssignedMoth(String date,int i,boolean isAddString){
        String resultDate="";
        try {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        if(isAddString){
            date=date+"-01";
        }
        Date myDate1 = dateFormat1.parse(date);
        resultDate=getDateLast12Months(myDate1,1);
        //System.out.println(resultDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("时间转换异常");
        }
        return resultDate;
    }

    /**
     * 比较两个日期的相差月数
     * @param date1
     * @param date2
     * @param  isAddYear 是否需要把相差年份的月累加起来
     * @return
     */
    public static int getMonthSpace(String date1, String date2,boolean isAddYear) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate1=null;
        Date newDate2=null;
        try {
            newDate1=sdf.parse(date1);
            newDate2=sdf.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate1);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(newDate2);
        int year1=calendar1.get(Calendar.YEAR);
        int month1=calendar1.get(Calendar.MONTH)+1;
        int day1=calendar1.get(Calendar.DAY_OF_MONTH);

        LocalDate startDate = LocalDate.of(year,month,day);
        LocalDate endDate = LocalDate.of(year1,month1,day1);
        Period period = Period.between(startDate, endDate);//比较两个日期变量
        int betweenMonth=0;
        if(isAddYear){
            betweenMonth = period.getYears()*12+period.getMonths();
        }else{
            betweenMonth = period.getMonths();
        }
        return betweenMonth;

    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year,int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 获取指定年月的最后时间
     * @param date (yyyy-MM)
     * @return
     */
    public static String getLastTimeByYearMonth(String date){

        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);;

        return getLastDayOfMonth(year, month) + " 23:59:59";
    }

    /**
     * 根据年月获取当月的最后一天
     * @param date  xxxx-xx
     * @return
     */
    public static String getLastDayOfMonthByYearMonth(String date)
    {
        int year = Integer.valueOf(date.split("-")[0]);
        int month = Integer.valueOf(date.split("-")[1]);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 根据年月获取后n个月的最后一天
     * @param date  xxxx-xx
     * @return
     */
    public static String getAfterMonthLastDayOfMonthByYearMonth(String date, int n)
    {
        int year = Integer.valueOf(date.split("-")[0]);
        int month = Integer.valueOf(date.split("-")[1]);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1 + n);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String afterMonthLastDayOfMonth = sdf.format(cal.getTime());

        return afterMonthLastDayOfMonth+" 23:59:59";
    }


    /**
     * 比较2个日期相差的天数
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysDiffer(String startDate, String endDate){

        int days = (int)((DateUtils.string2Date(startDate + " 00:00:00").getTime() - DateUtils.string2Date(endDate + " 23:59:59").getTime()) / (1000 * 3600 * 24));

        return days;
    }

    /**
     * 获取后n个月的最后一天的时间
     * @param n
     * @return
     */
    public static String getAfter12MonthsLastTime(int n){
        return getAfter12MonthsLastTimeByDate(null, n);
    }

    public static void main(String[] args) {


    }
    /**
     * 根据指定日期获取n个月后的最后一天的时间
     * @param date
     * @param n
     * @return
     */
    public static String getAfter12MonthsLastTimeByDate(Date date,int n){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        if(null == date){
            c.setTime(new Date());
        }else{
            c.setTime(date);
        }
        //设置月份
        c.add(Calendar.MONTH, n);
        //获取某月最大天数
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        c.set(Calendar.DAY_OF_MONTH, lastDay);
        Date m = c.getTime();
        return sdf.format(m) + " 23:59:59";

    }



    public static int getTimestamp() {
        return (int) (getNow().getTime()/1000);
    }

    /**
     * 获取当前年字符串
     * @return
     */
    public static String getCurrentYear() {
        Calendar date = Calendar.getInstance();
        return String.valueOf(date.get(Calendar.YEAR));
    }

    /**
     * 兼容字符串日期格式
     * @param date
     * @return
     */
    public static Date transform(Object date){
        if(date instanceof Date){
            return (Date) date;
        }
        if(date instanceof String){
            try {
                return parseDate((String) date,PATTERN);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 获取日期范围的天数
     * @param begin
     * @param end
     * @return
     */
    public static long getDaysBetween(Date begin,Date end){
        long beginDays = DateUtils.getFragmentInDays(begin, Calendar.YEAR);
        long endDays = DateUtils.getFragmentInDays(end, Calendar.YEAR);
        return endDays-beginDays;
    }

    public static BigDecimal getDaysOffset(Date begin, Date end) {
        Duration between = Duration.between(begin.toInstant(), end.toInstant());
        return BigDecimal.valueOf(between.toMillis()).divide(BigDecimal.valueOf(86400000),2,BigDecimal.ROUND_CEILING);
    }

    public static Date parseDate(String str){
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str,PATTERN);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDate(String str,String parsePatterns){
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str,parsePatterns);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInitDateDtr() {
        return "0000-00-00 00:00:00";
    }

    /**
     *  java.sql.date 父类 Java.util.date
     *
     *  sql.date  格式 2018-01-01 00:00:00.0
     *  util.date 格式 Mon Jan 01 00:00:00 GMT+08:00 2018
     */
    public static Date sqlDate2UtilDate(Date date){
        return new Date(date.getTime());
    }


    /**
     * 获取当前月第一天：
     */
    public static Date getCurrentMonthFirst(Boolean includeSecond){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        if (includeSecond){
            first = first + " 00:00:00";
            return DateUtils.parseDate(first);
        }
        return DateUtils.parseDate(first,"yyyy-MM-dd");
    }
    /**
     * 获取当前月最后一天
     */
    public static Date getCurrentMonthLast(Boolean includeSecond){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        if (includeSecond){
            last = last + " 23:59:59";
            return DateUtils.parseDate(last);
        }
        return DateUtils.parseDate(last,"yyyy-MM-dd");
    }


    /**
     * 当前日期加上天数后的日期
     * @param num 为增加的天数
     * @return
     */
    public static Date plusDay(int num){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        return ca.getTime();
    }

    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @return
     */
    public static Date plusDay(Date date,int num){
        Date d = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);

        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        return ca.getTime();
    }

}
