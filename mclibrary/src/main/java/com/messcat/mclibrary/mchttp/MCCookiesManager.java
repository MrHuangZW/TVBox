package com.messcat.mclibrary.mchttp;

import com.messcat.mclibrary.base.BaseApplication;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookie管理类
 * Created by Administrator on 2017/7/28 0028.
 */

public class MCCookiesManager implements CookieJar {

    /**
     * 保存Cookie
     */
    private final MCPersistentCookieStore cookieStore = new MCPersistentCookieStore(BaseApplication.Companion.getInstance());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {//接收时获取cookie,并且保存Cookie
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {//发送时加入head
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}
