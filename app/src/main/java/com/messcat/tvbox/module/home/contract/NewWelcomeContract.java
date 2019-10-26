package com.messcat.tvbox.module.home.contract;


import com.messcat.mclibrary.base.BasePresenter;
import com.messcat.mclibrary.base.BaseView;
import com.messcat.tvbox.module.home.bean.WeatherBean;
import com.messcat.tvbox.module.home.bean.WelcomeBean;
import com.messcat.tvbox.module.home.bean.WifiBean;

/**
 * author:Bandele
 * date:2018/9/12 0012$
 * describe:
 */
public class NewWelcomeContract {

    public interface View extends BaseView {
        void getWelcomeData(WelcomeBean welcomeBean);

        void getWeather(WeatherBean weatherBean);

        void weatherError(String error);

        void wifiState(WifiBean wifiBean);
    }

    public interface Presenter extends BasePresenter<NewWelcomeContract.View> {
        void getWelcomeInfo(String diCode, String roomNum,String type);

        void getWifiState(String diCode, String roomNum);
    }
}
