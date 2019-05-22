package com.xyoye.dandanplay2.utils.net.okhttp;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 *
 * Created by xyy on 2017/6/23.
 */
public class CookiesManager implements CookieJar {

    private final PersistentCookieStore cookieStore;

    public CookiesManager(Context context) {
        cookieStore = new PersistentCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        LogUtils.d("cookie", "saveFromResponse");
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        LogUtils.d("cookie", "loadForRequest");
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            LogUtils.d("cookies_token_load",getToken(cookie));
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        LogUtils.d("cookies_load", cookieHeader.toString());
        return cookies;
    }

    public boolean remoteCookie(HttpUrl url, Cookie cookie) {
        return cookieStore.remove(url, cookie);
    }

    public PersistentCookieStore getStore() {
        return cookieStore;
    }

    public List<Cookie> getAllCookie() {
        return cookieStore.getCookies();
    }

    public String getToken(Cookie cookie) {
        return cookieStore.getCookieToken(cookie);
    }
}
