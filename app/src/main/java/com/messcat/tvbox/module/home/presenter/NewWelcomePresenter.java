package com.messcat.tvbox.module.home.presenter;


import com.messcat.kotlin.mchttp.ObjectLoader;
import com.messcat.mclibrary.base.RxPresenter;
import com.messcat.tvbox.module.Constants;
import com.messcat.tvbox.module.home.bean.WeatherBean;
import com.messcat.tvbox.module.home.bean.WelcomeBean;
import com.messcat.tvbox.module.home.bean.WifiBean;
import com.messcat.tvbox.module.home.contract.NewWelcomeContract;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * author:Bandele
 * date:2018/9/12 0012$
 * describe:
 */
public class NewWelcomePresenter extends RxPresenter<NewWelcomeContract.View> implements NewWelcomeContract.Presenter {

    private NewWelcomeContract.View mView;
    private NewWelcomeLoader newWelcomeLoader;

    public NewWelcomePresenter(NewWelcomeContract.View mView, NewWelcomeLoader newWelcomeLoader) {
        this.mView = mView;
        this.newWelcomeLoader = newWelcomeLoader;
    }

    @Override
    public void getWelcomeInfo(String diCode, String roomNum,String type) {
        mView.showLoadingDialog();
        newWelcomeLoader.getWelcomeInfo(diCode, roomNum,type).doOnNext(new Action1<WelcomeBean>() {
            @Override
            public void call(WelcomeBean welcomeBean) {
                if ("200".equals(welcomeBean.getStatus())) {
                    mView.getWelcomeData(welcomeBean);
                } else {
                    mView.showError(welcomeBean.getMessage());
                    mView.dismissLoadingDialog();
                }
            }
        }).flatMap(new Func1<WelcomeBean, Observable<WeatherBean>>() {
            @Override
            public Observable<WeatherBean> call(WelcomeBean welcomeBean) {
                return newWelcomeLoader.getWeather
                        (welcomeBean.getResult().getWelcomeInfo().getHotelInfo().getCity(),
                                Constants.Companion.getKey());
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WeatherBean>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {
                mView.weatherError(e.toString());
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(WeatherBean weatherBean) {
                if ("200".equals(weatherBean.getResultcode())) {
                    mView.getWeather(weatherBean);
                } else {
                    mView.weatherError("获取天气信息失败");
                }
            }
        });
    }

    @Override
    public void getWifiState(String diCode, String roomNum) {
        newWelcomeLoader.getWifiStatus(diCode, roomNum).subscribe(new Observer<WifiBean>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.showError(e.toString());
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(WifiBean wifiBean) {
                if ("200".equals(wifiBean.getStatus())) {
                    mView.wifiState(wifiBean);
                } else {
                    mView.showError(wifiBean.getMessage());
                }
            }
        });

    }

    public class NewWelcomeLoader extends ObjectLoader {
        private NewWelcomeHttp newWelcomeHttp;

        public NewWelcomeLoader(NewWelcomeHttp newWelcomeHttp) {
            this.newWelcomeHttp = newWelcomeHttp;
        }

        Observable<WelcomeBean> getWelcomeInfo(String diCode, String roomNum,String type) {
            return observe(newWelcomeHttp.welcomeInfo(diCode, roomNum,type));
        }

        Observable<WeatherBean> getWeather(String cityname, String key) {
            return observe(newWelcomeHttp.weather(cityname, key));
        }

        Observable<WifiBean> getWifiStatus(String diCode, String roomNum) {
            return observe(newWelcomeHttp.wifiStatus(diCode, roomNum));
        }
    }

    interface NewWelcomeHttp {
        @FormUrlEncoded
        @POST("epIndex/getWelcomeInfo")
        Observable<WelcomeBean> welcomeInfo(@Field("diCode") String diCode, @Field("roomNum") String roomNum,@Field("type") String type);

        @GET("http://v.juhe.cn/weather/index")
        Observable<WeatherBean> weather(@Query("cityname") String cityname, @Query("key") String key);

        @FormUrlEncoded
        @POST("epIndex/getWifiStatus")
        Observable<WifiBean> wifiStatus(@Field("diCode") String diCode, @Field("roomNum") String roomNum);

    }
}
