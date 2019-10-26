package com.messcat.tvbox.module.home.bean

import java.io.Serializable

/**
 * Created by Administrator on 2017/9/4 0004.
 */

data class WeatherBean(var resultcode: String?, var reason: String?, var result: ResultBean?, var error_code: Int) : Serializable {

    data class ResultBean(var sk: SkBean?, var today: TodayBean?, var future: FutureBean?) {

        data class SkBean(var temp: String?, var wind_direction: String?, var wind_strength: String?, var humidity: String?, var time: String?)
    }

    data class TodayBean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBean?, var wind: String?, var week: String?,
                         var city: String?, var date_y: String?, var dressing_index: String?, var dressing_advice: String?, var uv_index: String?,
                         var comfort_index: String?, var wash_index: String?, var travel_index: String?, var exercise_index: String?, var drying_index: String?) {

        data class WeatherIdBean(var fa: String?, var fb: String?)
    }

    data class FutureBean(var day_20170904: Day20170904Bean?, var day_20170905: Day20170905Bean?, var day_20170906: Day20170906Bean?,
                          var day_20170907: Day20170907Bean?, var day_20170908: Day20170908Bean?, var day_20170909: Day20170909Bean?,
                          var day_20170910: Day20170910Bean?) {

        data class Day20170904Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanX?,
                                   var wind: String?, var week: String?, var date: String?) {

            data class WeatherIdBeanX(var fa: String?, var fb: String?)
        }

        data class Day20170905Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanXX?,
                                   var wind: String?, var week: String?, var date: String?) {

            data class WeatherIdBeanXX(var fa: String?, var fb: String?)
        }

        data class Day20170906Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanXXX?, var wind: String?,
                                   var week: String?, var date: String?) {

            data class WeatherIdBeanXXX(var fa: String?, var fb: String?)
        }

        data class Day20170907Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanXXXX?, var wind: String?,
                                   var week: String?, var date: String?) {

            data class WeatherIdBeanXXXX(var fa: String?, var fb: String?)
        }

        data class Day20170908Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanXXXXX?, var wind: String?,
                                   var week: String?, var date: String?) {

            data class WeatherIdBeanXXXXX(var fa: String?, var fb: String?)
        }

        data class Day20170909Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanXXXXXX?, var wind: String?,
                                   var week: String?, var date: String?) {
            /**
             * temperature : 26℃~33℃
             * weather : 多云转雷阵雨
             * weather_id : {"fa":"01","fb":"04"}
             * wind : 微风
             * week : 星期六
             * date : 20170909
             */


            data class WeatherIdBeanXXXXXX(var fa: String?, var fb: String?)
        }

        data class Day20170910Bean(var temperature: String?, var weather: String?, var weather_id: WeatherIdBeanXXXXXXX?,
                                   var wind: String?, var week: String?, var date: String?) {
            /**
             * temperature : 26℃~33℃
             * weather : 雷阵雨转多云
             * weather_id : {"fa":"04","fb":"01"}
             * wind : 微风
             * week : 星期日
             * date : 20170910
             */


            data class WeatherIdBeanXXXXXXX(var fa: String?, var fb: String?)
        }
    }
}
