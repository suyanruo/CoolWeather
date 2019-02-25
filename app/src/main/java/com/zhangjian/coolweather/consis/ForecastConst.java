package com.zhangjian.coolweather.consis;

import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;

public class ForecastConst {
    private Basic basic;	// 基础信息	Basic
    private String status;	// 接口状态	ok
    private Update update;	// 接口更新时间	Update
    private ForecastBase daily_forecast;	// 天气预报	List<ForecastBase>

//    public class Basic {
//        private String location;	// 地区／城市名称	卓资
//        private String cid;	     // 地区／城市ID	CN101080402
//        private String lon; 	// 地区／城市经度	112.577702
//        private String lat;	    // 地区／城市纬度	40.89576
//        private String parent_city; 	// 该地区／城市的上级城市	乌兰察布
//        private String admin_area; 	// 该地区／城市所属行政区域	内蒙古
//        private String cnty; 	// 该地区／城市所属国家名称	中国
//        private String tz; 	// 该地区／城市所在时区	+8.00
//    }
//
//    public class Update {
//        private String loc;	 // 当地时间，24小时制，格式yyyy-MM-dd HH:mm	2017-10-25 12:34
//        private String utc;	 // UTC时间，24小时制，格式yyyy-MM-dd HH:mm	2017-10-25 04:34}
//    }
//
//
//    public class ForecastBase {
//        private String tmp_max; 	// 最高温度	4
//        private String tmp_min;	    // 最低温度	-5
//    }
}
