/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.util;

import com.kaku.weac.Listener.HttpCallbackListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * http工具类
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public class HttpUtil {
    private static OkHttpClient sOkHttpClient;

    /**
     * 发送http请求
     *
     * @param address  访问地址
     * @param cityName 城市名
     * @param listener 响应监听
     */
    public static void sendHttpRequest(final String address, final String cityName, final
    HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                HttpURLConnection connection = null;
                try {
                    String address1;
                    if (address == null) {
                        address1 = "http://wthrcdn.etouch.cn/WeatherApi?city=" + URLEncoder
                                .encode(cityName, "UTF-8");
                    } else {
                        address1 = address;
                    }
                    if (sOkHttpClient == null) {
                        sOkHttpClient = new OkHttpClient();
                    }
                    sOkHttpClient.setReadTimeout(6000, TimeUnit.MILLISECONDS);
                    sOkHttpClient.setConnectTimeout(6000, TimeUnit.MILLISECONDS);
                    sOkHttpClient.setWriteTimeout(6000, TimeUnit.MILLISECONDS);
                    Request request = new Request.Builder().url(address1).build();
                    Response response = sOkHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    // 访问：【http://www.weather.com.cn/data/list3/cityXXX.xml】的时候，
                    // 如果城市代码错误会继续访问一些无关的东西
//                    if (result.contains("无法访问")) {
//                        listener.onError(new Exception());
//                        return;
//                    }



/*                    URL url = new URL(address1);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();*/


/*                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();

                    InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
                    InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());*/


                    // 天气信息
//                    WeatherInfo weatherInfo = handleWeatherResponse(in);

/*                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("无法访问")) {
                            listener.onError(new Exception());
                            return;
                        }
                        response.append(line);
                    }*/

/*
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/WeaAlarmClock/test/" +
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .format(new Date())
                            + ".txt");
                    if (!file.exists()){
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file);
                    fw.write(response.toString());
                    fw.flush();
                    fw.close();*/


                    if (listener != null) {
                        // 加载完成返回
                        listener.onFinish(result);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 加载失败
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }
}
