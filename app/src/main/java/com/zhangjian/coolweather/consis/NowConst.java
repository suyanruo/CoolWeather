package com.zhangjian.coolweather.consis;

import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;

public class NowConst {
    private Basic basic;
    private String status;
    private Update update; // 接口更新时间
    private NowBase now; // now 实况天气


//    class Basic {
//        private String location;	// 地区／城市名称
//        private String cid;	 // 地区／城市ID	CN101080402
//        private String lon;	 // 地区／城市经度	112.577702
//        private String lat;	 // 地区／城市纬度	40.89576
//        private String parent_city;	 // 该地区／城市的上级城市	乌兰察布
//        private String admin_area;	 // 该地区／城市所属行政区域	内蒙古
//        private String cnty;	 // 该地区／城市所属国家名称	中国
//        private String tz;	 // 该地区／城市所在时区	+8.00
//    }
//    class Update {
//        private String loc;	 // 当地时间，24小时制，格式yyyy-MM-dd HH:mm	2017-10-25 12:34
//        private String utc;	 // UTC时间，24小时制，格式yyyy-MM-dd HH:mm	2017-10-25 04:34
//    }
//    class Now {
//        private String fl;	 // 体感温度，默认单位：摄氏度	23
//        private String tmp;	// 温度，默认单位：摄氏度	21
//        private String cond_code;	// 实况天气状况代码	100
//        private String cond_txt; 	// 实况天气状况代码	晴
//        private String wind_deg; //	风向360角度	305
//        private String wind_dir;	// 风向	西北
//        private String wind_sc; // 风力	3-4
//        private String wind_spd;	 // 风速，公里/小时	15
//        private String hum;	 // 相对湿度	40
//        private String pcpn;	// 降水量	0
//        private String pres;	// 大气压强	1020
//        private String vis;	 // 能见度，默认单位：公里	10
//        private String cloud;	// 云量	23
//    }
}
