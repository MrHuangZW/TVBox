package com.messcat.mclibrary

/**
 * http://v.juhe.cn/weather/index?format=2&cityname=广州&key=404df18bfe29fe7b93e520d998489b61
 * 15902090933  ，密码slj820910110
百度地图开发者账号
 * 服务器地址
 */
var Any.BASE_URL: String
    set(value) {
        BASE_URL = value
    }
    get() {
//        return "http://211.149.230.232/"
//        return "http://www.9tsmart.com:8081/mccms-web/"
//        return "http://192.168.0.129:8081/mccms-web/"
//        return "http://test2.messcat.com/tvbox-web/"
        return "http://cms.ekesh.cn:8081/mccms-web/"
    }