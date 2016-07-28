package com.example.okhttp.Manager;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * okHttp请求管理者
 * Created by Administrator on 2016/7/26.
 */
public class OkHttpManager {
    private static OkHttpManager manager;
    private OkHttpClient mHttpClient;
    private Handler mHandler;
    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    public OkHttpManager() {
        mHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpManager getInstance() {
        OkHttpManager instance = null;
        if (manager == null) {
            synchronized (OkHttpManager.class) {
                if (instance == null) {
                    instance = new OkHttpManager();
                    manager = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 异步请求
     *
     * @param url
     * @param callBack
     * @return
     */
    public void asyncGetJsonString(final String url, final Func callBack) {
        final Request request = new Request.Builder().url(url).build();
        //enqueue为异步方式
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonString(response.body().string(), callBack);
                }
            }
        });
    }

    public void asyncGetJsonObject(final String url, final Func1 callBack) {
        final Request request = new Request.Builder().url(url).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObject(response.toString(), callBack);
                }
            }
        });
    }

    /**
     * 同步方法
     *
     * @param url
     * @return
     */
    private String syncGetByUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response;
        try {
            response = mHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交表单数据
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void sendComplexFrom(final String url, HashMap<String, String> map, final Func1 callBack) {
        //新建表单
        FormEncodingBuilder body = new FormEncodingBuilder();
        for (Map.Entry<String, String> map1 : map.entrySet()) {
            body.add(map1.getKey(), map1.getValue());
        }
        //请求body
        RequestBody requestBody = body.build();
        //建立请求
        Request request = new Request.Builder().url(url).post(requestBody).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObject(response.toString(), callBack);
                }
            }
        });
    }

    /**
     * 向服务器提交字符串(文件类似)
     *
     * @param url
     * @param content
     * @param callBack
     */
    public void sendString(final String url, final String content, final Func1 callBack) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObject(response.toString(), callBack);
                }
            }
        });
    }

    /**
     * 请求成功之后给callback
     *
     * @param jsonValue
     * @param callBack
     */
    public void onSuccessJsonString(final String jsonValue, final Func callBack) {
        if (jsonValue != null && !jsonValue.isEmpty()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 返回成功的json对象
     *
     * @param jsonValue
     * @param callBack
     */
    public void onSuccessJsonObject(final String jsonValue, final Func1 callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callBack.onResponse(new JSONObject(jsonValue));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface Func {
        void onResponse(String result);
    }

    public interface Func1 {
        void onResponse(JSONObject jsonObject);
    }

}
