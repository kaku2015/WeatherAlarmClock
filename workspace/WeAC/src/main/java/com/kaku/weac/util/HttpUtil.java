/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.util;

import com.kaku.weac.Listener.HttpCallbackListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * http工具类
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public class HttpUtil {

    /**
     * 发送http请求
     *
     * @param address  访问地址
     * @param cityName 城市名
     * @param listener 响应监听
     */
    public static void sendHttpRequest(final String address, final String cityName, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // FIXME: okHttp
                HttpURLConnection connection = null;
                try {
                    String address1;
                    if (address == null ) {
                        address1 = "http://wthrcdn.etouch.cn/WeatherApi?city=" + URLEncoder.encode(cityName, "UTF-8");
                    } else {
                        address1 = address;
                    }
                    URL url = new URL(address1);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();


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

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("无法访问")) {
                            listener.onError(new Exception());
                            return;
                        }
                        response.append(line);
                    }

/*
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/WeaAlarmClock/test/" +
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())
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
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 加载失败
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }
}
