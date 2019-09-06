package info.lxlong.demo.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

public class Btest {

    private Logger logger = LoggerFactory.getLogger(Btest.class);

    @Test
    public void testDocument() {
        InputStream inStream = null;
        // String strUrl = "https://www.mm131.net/xinggan/";
        String strUrl = "https://www.mm131.net/xinggan/1530.html";
        // String strUrl = "https://www.mm131.net/xinggan/5122_4.html";
        // https://img1.mmmw.net/pic/5122/4.jpg
        // https://img1.mmmw.net/pic/1530/1.jpg
        try {

            // URL url = new URL(strUrl);
            // HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // // 设置Java服务器代理连接，要不然报错403
            // // 浏览器可以访问此url图片并显示，但用Java程序就不行报错Server returned HTTP response code:403 for
            // // 具体原因：服务器的安全设置不接受Java程序作为客户端访问(被屏蔽)，解决办法是设置客户端的User Agent
            // conn.setRequestProperty("User-Agent",
            // "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; AcooBrowser; .NET CLR
            // 1.1.4322; .NET CLR 2.0.50727)");
            // // 很多网站的防采集的办法,就是判断浏览器来源referer和cookie以及userAgent
            // // 参考：https://blog.csdn.net/chengyingzhilian/article/details/7835400
            // // https://blog.csdn.net/wl981292580/article/details/80351136
            // conn.setRequestProperty("Referer", strUrl);
            // conn.setRequestMethod("GET");
            // conn.setConnectTimeout(30 * 1000);

            // conn.connect();
            // // 得到响应码
            // int responseCode = conn.getResponseCode();
            // if (responseCode == HttpURLConnection.HTTP_OK) {
            // // 得到响应流
            // InputStream inputStream = conn.getInputStream();
            // // 将响应流转换成字符串
            // String result = inputStream2String(inputStream);// 将流转换为字符串。
            // logger.info("result=============" + result);
            // }
            Document doc = Jsoup.connect(strUrl).get();
            // Document imgDoc = Jsoup.connect(strUrl).get();
            // String imgUrl = imgDoc.select("div.main-image p a img").first().attr("src");
            // logger.info("图片url ---- {}", imgUrl);

            Element h5 = doc.select("div.content h5").first();
            Element maxIndex = doc.select("div.content-page span.page-ch").first();
            // logger.info("title == {}", h5.html() );
            // logger.info("maxIndex == {}", maxIndex.html().replaceAll("[^0-9]", "") );
            logger.info(doc.body().html());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {

        String imgPath = "D://test/mm131/";
        TestUtil.mkdir(imgPath);

        Document doc = null;

        Document imgDoc = null;

        Element img = null;
        int maxPage = 0;

        String baseUrl = "https://www.mm131.net/xinggan/";

        String baseImgUrl = "https://img1.mmmw.net/pic/";

        for (int i = 1530; i < 5122; i++) {
        // for (int i = 5121; i < 5123; i++) {

            String url = baseUrl + i + ".html";
            try {

                doc = Jsoup.connect(url).userAgent(TestUtil.randomStr(Constan.USER_AGENT_LIST)).get();
                String title = doc.select("div.content h5").first().html();
                String page = doc.select("div.content-page span.page-ch").first().html().replaceAll("[^0-9]", "");
                maxPage = page.length() == 0 ? 0 : Integer.parseInt(page);

                logger.info("-- i = {} --- title = {} --- maxPage = {} --", i, title, maxPage);
                TestUtil.mkdir(imgPath + "/" + title);

                for (int j = 1; j <= maxPage; j++) {
                        String imgUrl = baseImgUrl + i + "/" + j + ".jpg";
                        String imgName = j + "..jpg";
                        logger.info("图片url ---- {}", imgUrl);
                        String path = imgPath + "/" + title + "/" + imgName;
                        String referer = j == 1 ? url : baseUrl + i + "_" + j + ".html";
                        TestUtil.downImage(imgUrl, referer, path);
                }
            } catch (IOException e) {
                System.out.println("读取失败或写入失败");
                // e.printStackTrace();
            } catch (Exception e) {
            }
        }
    }

}