package com.messcat.mclibrary.mchttp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

/**
 * 保存Cookie的实体类
 * Created by Administrator on 2017/7/28 0028.
 */

public class MCOkHttpCookies implements Serializable {
    private transient Cookie clientCookies;

    public MCOkHttpCookies(Cookie cookies) {
        this.clientCookies = cookies;
    }

    public Cookie getCookies() {
        Cookie bestCookies = clientCookies;
        if (clientCookies != null) {
            bestCookies = clientCookies;
        }
        return bestCookies;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(clientCookies.name());
        out.writeObject(clientCookies.value());
        out.writeLong(clientCookies.expiresAt());
        out.writeObject(clientCookies.domain());
        out.writeObject(clientCookies.path());
        out.writeBoolean(clientCookies.secure());
        out.writeBoolean(clientCookies.httpOnly());
        out.writeBoolean(clientCookies.hostOnly());
        out.writeBoolean(clientCookies.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder();
        builder = builder.name(name);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = builder.path(path);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        clientCookies = builder.build();
    }
}
