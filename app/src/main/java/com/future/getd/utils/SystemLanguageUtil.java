package com.future.getd.utils;

import android.content.Context;

import com.future.getd.log.LogUtils;

import java.util.Locale;


public class SystemLanguageUtil {
    public static final String LANGUAGE_CN = "zh";//中文
    public static final String LANGUAGE_EN = "en";//英文

    public static String currentLanguage = LANGUAGE_EN;


    public static boolean isZh(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" language = " + language);
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    public static boolean isJA(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" language = " + language);
        if (language.contains("ja"))
            return true;
        else
            return false;
    }

    public static boolean isTR(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" language = " + language);
        if (language.contains("tr"))
            return true;
        else
            return false;
    }
    public static boolean isPT(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" language = " + language);
        if (language.contains("pt"))
            return true;
        else
            return false;
    }

    public static boolean isPL(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" language = " + language);
        if (language.contains("pl"))
            return true;
        else
            return false;
    }
    public static boolean isRu(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" language = " + language);
        if (language.contains("ru"))
            return true;
        else
            return false;
    }


    public static boolean isCn(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        LogUtils.e(" language = " + language);
        if (language.endsWith("zh")&&country.equals("CN"))
            return true;
        else
            return false;
    }


    public static String getCurrentLanguage(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        LogUtils.e(" language = " + language);
        if (language.endsWith("zh")&&country.equals("CN"))
            return LANGUAGE_CN;
        else
            return LANGUAGE_EN;
    }

    public static String getLanguage(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        LogUtils.e("mytest"," language-country = " + language  + "-" + country);
        if (language != null) {
            if (language.contains("zh")) {
                return "zh";
            } else if (language.contains("ja")) {
                return "jp";
            } else if (language.contains("ko")) {
                return "ko";
            } else if (language.contains("pl")) {
                return "pl";
            } else if (language.contains("ru")) {
                return "ru";
            } else if (language.contains("th")) {
                return "th";
            } else if (language.contains("tr")) {
                return "tr";
            } else if (language.contains("pt")) {
                return "pt";
            }else {
                return "en";
            }
        }
        return "en";

    }
    public static String getOriginalLanguage(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;
    }
    public static String getOriginalCountry(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String country = locale.getCountry();
        return country;
    }
}
