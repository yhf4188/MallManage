package com.yhf.pointsmanage.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class Base64Util {

    public static void main(String[] args) throws Exception {

        //本地图片地址
        String url = "C:/Users/Administrator/Desktop/628947887489084892.jpg";
        //在线图片地址

        String str = Base64Util.ImageToBase64ByLocal(url);


        System.out.println(str);


    }

    /**
     * 本地图片转换成base64字符串
     * 将图片文件转化为字节数组字符串，
     * 并对其进行Base64编码处理
     *
     * @param imgFile 图片本地路径
     * @return
     */
    public static String ImageToBase64ByLocal(String imgFile) {//


        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

/**
      * 在线图片转换成base64字符串
      * 
      * @param imgURL    图片线上路径
      * @return
      */
    public static String ImageToBase64ByOnline(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }


    /**
     * base64字符串转换成图片  对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      base64字符串
     * @param imgFilePath 图片存放路径
     * @return
     */
    public static String Base64ToImage(String imgStr, String imgFilePath, String urlPath) throws IOException{

        // 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            return null;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return urlPath;
        } catch (Exception e) {
            throw e;
        }
    }

}