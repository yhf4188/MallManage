package com.yhf.pointsmanage.tools;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static boolean exist(String url)
    {
        boolean success = true;
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            success = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
        }catch (Exception e)
        {
            e.printStackTrace();
            success = false;
        }finally {
            return success;
        }
    }
}
