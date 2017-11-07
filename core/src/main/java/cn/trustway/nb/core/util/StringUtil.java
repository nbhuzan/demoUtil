package cn.trustway.nb.core.util;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by huzan  2017/3/27 16:11.
 * 描述：字符串操作
 */

public class StringUtil {


    /**
     *创建时间：2017/10/31
     *创建者：huzan
     *描述：格式化显示头像的文字
     */
    public static String formatHeaderName(String name){
        if(name==null){
            return "";
        }
        if (name.length() == 3) {
            name = name.substring(1, 3);
        } else if (name.length() > 3) {
            name = name.substring(name.length() - 3, name.length());
        }
        return name;
    }

    /**
     * Create by Zheming.xin on 2017/6/24 12:02
     * param:
     * description: 判断String是否为空或者为null
     */
    public static boolean isStringEmpty(String str) {
        if (str == null || str.isEmpty() || "".equals(str.trim()) || "null".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 截取年月日，不是时分秒
     *
     * @param timeStr
     * @return
     */
    public static String getDateStr(String timeStr) {
        String dataStr = "--";
        if (timeStr != null && timeStr.length() > 0) {
            dataStr = timeStr.split(" ")[0];
        }
        return dataStr;
    }

    /**
     * 处理查询显示信息，有数据则显示数据，无数据则显示'--'
     *
     * @param str
     * @return
     */
    public static String getSearchInfoStr(String str) {
        String infoStr = "--";
        if (str != null && str.length() > 0) {
            infoStr = str;
        }
        return infoStr;
    }

    /**
     * string解成char[]
     *
     * @param str
     * @return
     */
    public static char[] getStrings(@NonNull String str) {
        return str.toCharArray();
    }

    /**
     * 纯数字正则
     *
     * @return
     */
    public static String regularNumber() {
        return "^\\d+$";
    }

    public static String regularLetter() {
        return "[a-zA-Z]+";
    }

    public static String regularChinese() {
        return "[\u4e00-\u9fa5]";
    }

    public static String regularIdentification() {
        return "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
    }

    /**
     * 解析准驾车型
     *
     * @param zjcx
     * @return
     */
    public static List<String> getZjcx(String zjcx) {
        if (zjcx == null || zjcx.length() <= 0) {
            return new ArrayList<>();
        }
        char[] zjcxs = zjcx.toCharArray();
        int index = 0;
        List<String> zjcxList = new ArrayList<>();
        for (int i = 0; i < zjcxs.length; i++) {
            if (String.valueOf(zjcxs[i]).matches(StringUtil.regularNumber())) {
                zjcxList.add(zjcx.substring(index != 0 ? index + 1 : index, i + 1));
                index = i;
            }
            if (i == zjcxs.length - 1 && index != i) {
                zjcxList.add(zjcx.substring(index + 1, zjcxs.length));
            }
        }
        return zjcxList;
    }








    public static SpannableStringBuilder str2ssb(String strBefore, String str, String strAfter) {
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(strBefore + str + strAfter);
        int length = strBefore.length();
        strBuilder.setSpan(redSpan, length, length + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * @return timtPoint距离现在经过的时间，分为
     * 刚刚，1-29分钟前，半小时前，1-23小时前，1-14天前，半个月前，1-5个月前，半年前，1-xxx年前
     */
    public static String getTimeElapse(Date date) {

        long nowTime = System.currentTimeMillis() / 1000;

        //createTime是发表文章的时间

        long oldTime = date.getTime() / 1000;
        if (oldTime > nowTime) {
            return getTimeElapseAfter(date);
        }

        //elapsedTime是发表和现在的间隔时间

        long elapsedTime = nowTime - oldTime;

        if (elapsedTime < seconds_of_1minute) {

            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {

            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {

            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {

            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {

            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {

            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {

            return "半年前";
        }

        return elapsedTime / seconds_of_1year + "年前";
    }

 private static String getTimeElapseAfter( Date date) {

        long nowTime = System.currentTimeMillis() / 1000;

        //createTime是发表文章的时间

        long oldTime = date.getTime() / 1000;

        //elapsedTime是发表和现在的间隔时间

        long elapsedTime = oldTime - nowTime;

        if (elapsedTime < seconds_of_1minute) {

            return "1分钟内";
        }
        if (elapsedTime < seconds_of_30minutes) {

            return elapsedTime / seconds_of_1minute + "分钟后";
        }
        if (elapsedTime < seconds_of_1hour) {

            return "半小时后";
        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时后";
        }
        if (elapsedTime < seconds_of_15days) {

            return elapsedTime / seconds_of_1day + "天后";
        }
        if (elapsedTime < seconds_of_30days) {

            return "半个月后";
        }
        if (elapsedTime < seconds_of_6months) {

            return elapsedTime / seconds_of_30days + "月后";
        }
        if (elapsedTime < seconds_of_1year) {

            return "半年后";
        }

        return elapsedTime / seconds_of_1year + "年后";
    }


    /**
     * 设置占位符
     *
     * @param key
     * @return 样式：？，？，？
     */
    public static String sqlPlaceholders(String key){
        StringBuilder sb = new StringBuilder(key.length() * 2 - 1);
        sb.append("?");
        for (int i = 1; i < key.length(); i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

    //身份证第一位到第十七位的系数: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
    private static int[] validateIdentiList = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    //取余11以后的对照表
    private static String[] validateIdentiRemainderList = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * Create by Zheming.xin on 2017/7/12 10:47
     * param:
     * description: 正则粗略校验身份证是否符合规范，然后对身份证进行正确性校验
     */
    public static boolean validateIdentification(String identification) {
        if (StringUtil.isStringEmpty(identification)) {
            return false;
        }

        if (!Pattern.matches(regularIdentification(), identification)) {
            return false;
        }

        String[] identis = identification.trim().split("");

        List<String> identilist = new ArrayList<>();
        for (int i = 0; i < identis.length; i++) {
            if (!isStringEmpty(identis[i])) {
                identilist.add(identis[i]);
            }
        }

        if (identilist.size() != 18) {
            return false;
        }

        try {
            int validateCount = 0;
            for (int i = 0; i < validateIdentiList.length; i++) {
                validateCount += Integer.parseInt(identilist.get(i)) * validateIdentiList[i];
            }
            validateCount = validateCount % 11;
            String remainder = validateIdentiRemainderList[validateCount];
            if (!identilist.get(17).equals(remainder)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     *创建时间：2017/8/2
     *创建者：huzan
     *描述：
     */
    public static List<String> str2List(@NonNull String str){
        char[] chars = str.toCharArray();
        List<String> list = new ArrayList<>();
        for (char c:chars) {
            list.add(String.valueOf(c));
        }
        return list;
    }

    private static final String sensitiveStringFormat = "(\\d{%d})\\d{%d}(\\d{%d})";

    public static String hideSensitiveInfo(String info, String sensitiveStr) {
        if(isStringEmpty(info)) {
            return null;
        }

        int total = info.length();
        int hideLength = 0;
        int showLeftLength = 0;
        int showRightLength = 0;
        String sStr = "";
        String strFormat = "";

        boolean isChinese = false;
        for (char curchar : info.trim().toCharArray()) {
            if (Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                isChinese = true;
                break;
            }
        }

        if (!isChinese) {
            if (total == 2) {
                hideLength = 1;
                showLeftLength = 1;
                showRightLength = 0;
            } else if (total == 3) {
                hideLength = 1;
                showLeftLength = 1;
                showRightLength = 1;
            } else if (total == 4) {
                hideLength = 2;
                showLeftLength = 1;
                showRightLength = 1;
            } else if (total == 18) {
                hideLength = 10;
                showLeftLength = 4;
                showRightLength = 4;
            } else if (total > 18) {
                hideLength = 10;
                showLeftLength = 4;
                showRightLength = 4;
            } else {
                hideLength = total / 3 + 2;

                if ((total - hideLength) % 2 != 0) {
                    hideLength += 1;
                }

                showLeftLength = (total - hideLength) / 2;
                showRightLength = (total - hideLength) / 2;
            }
            for (int i = 0; i < hideLength; i++) {
                sStr += sensitiveStr;
            }

            strFormat = String.format(sensitiveStringFormat, showLeftLength, hideLength, showRightLength);

            return info.replaceAll(strFormat, "$1" + sStr + "$2");
        } else {
            if (total == 2) {
                hideLength = 1;
                showLeftLength = 1;
            } else if (total == 3) {
                hideLength = 2;
                showLeftLength = 1;
            } else if (total == 4) {
                hideLength = 3;
                showLeftLength = 1;
            } else if (total > 12) {
                hideLength = 8;
                showLeftLength = 4;
            } else {
                hideLength = total / 2 + 2;
                showLeftLength = total - hideLength;
            }
            for (int i = 0; i < hideLength; i++) {
                sStr += sensitiveStr;
            }

            return info.substring(0, showLeftLength) + sStr;
        }
    }
}
