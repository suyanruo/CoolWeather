package com.zhangjian.coolweather.consis;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowStation;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;

public class AirNowConst {

    private Basic basic;	 // 基础信息	Basic
    private String status;	// 接口状态	ok
    private Update update; 	// 接口更新时间	Update
    private List<AirNowStation> air_now_station; 	// AQI站点实况	List<AirNowStation>
    private AirNowCity air_now_city; // AQI城市实况


}
