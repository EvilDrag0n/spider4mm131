package info.lxlong.demo.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);

    public static String inputStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    // 带编码的
    public static String inputStream2String(InputStream in, String encoding) throws Exception {
        StringBuffer out = new StringBuffer();
        InputStreamReader inread = new InputStreamReader(in, encoding);

        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * 随机获取集合元素
     */
    public static String randomStr(List<String> list) {
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }

    /**
     * 下载图片
     *
     * @param imageUrl 图片url
     * @param pathName 文件保存时的位置
     */
    public static Boolean downImage(String imageUrl, String pathName) throws Exception {
        return downImage(imageUrl, imageUrl, pathName);
    }

    /**
     * 下载图片
     *
     * @param imageUrl 图片url
     * @param pathName 文件保存时的位置
     * @param referer 浏览器来源referer
     */
    public static Boolean downImage(String imageUrl, String referer, String pathName) throws Exception {
        byte[] btImg = getImageFromNetByUrl(imageUrl, referer);
        if (null != btImg && btImg.length > 0) {
            try {
                File file = new File(pathName);
                FileOutputStream fops = new FileOutputStream(file);
                fops.write(btImg);
                fops.flush();
                fops.close();
                logger.info("图片已经写入到：{}", pathName);
                return true;
            } catch (Exception e) {
                logger.error("下载图片异常,图片url为:{}, pathname为{}", imageUrl, pathName);
                // Logger.error("下载图片异常,图片url为:{}, pathname为{},异常信息{}", imageUrl, pathName,
                // e.getMessage());
                throw e;
            }
        } else {
            logger.error("无数据的字节流.图片url为:{}, pathname为{}", imageUrl, pathName);
        }
        return false;
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) throws IOException {
        return getImageFromNetByUrl(strUrl, strUrl);
    }

    /**
     * 根据地址获得数据的字节流
     * 很多网站的防采集的办法,就是判断浏览器来源referer和cookie以及userAgent
     *
     * @param strUrl 网络连接地址
     * @param referer 浏览器来源referer
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl, String referer) throws IOException {
        InputStream inStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置Java服务器代理连接，要不然报错403
            // 浏览器可以访问此url图片并显示，但用Java程序就不行报错Server returned HTTP response code:403 for URL
            // 具体原因：服务器的安全设置不接受Java程序作为客户端访问(被屏蔽)，解决办法是设置客户端的User Agent
            conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            // 很多网站的防采集的办法,就是判断浏览器来源referer和cookie以及userAgent
            // 参考：https://blog.csdn.net/chengyingzhilian/article/details/7835400
            // https://blog.csdn.net/wl981292580/article/details/80351136
            conn.setRequestProperty("Referer", referer);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30 * 1000);
            // 通过输入流获取图片数据
            inStream = conn.getInputStream();
            // 得到图片的二进制数据
            byte[] btImg = readInputStream(inStream);
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 创建文件夹
     */
    public static void mkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {// 如果文件夹不存在
            file.mkdir();// 创建文件夹
        }
    }
}