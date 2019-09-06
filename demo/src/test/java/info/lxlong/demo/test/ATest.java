package info.lxlong.demo.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

public class ATest {

    private final Logger logger = LoggerFactory.getLogger(ATest.class);

    
    @Test
    public void test() {
        System.out.println("Hello World!!!");
    }

    @Test
    public void test1() {
        mkdir("D://test/img");
    }

    @Test
    public void test2() {

        String imgPath = "D://test/img/";
        mkdir(imgPath);

        Document doc = null;

        Document imgDoc = null;

        Element img = null;
        int maxPage = 0;

        try {
            doc = Jsoup.connect("https://mzitu.com").get();
            Elements as = doc.select("a.page-numbers");
            for (Element a : as) {
                String page = a.html().replaceAll("[^0-9]", "");
                int pageInt = page.length() == 0 ? 0 : Integer.parseInt(page);
                maxPage = maxPage < pageInt ?pageInt  : maxPage ;
            }
            logger.info("page---" + maxPage);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        for (int i = 1; i < maxPage; i++) {

            System.out.println("准备抓取第" + i + "页");
            String url = "https://www.mzitu.com/page/" + i + "/";
            try {

                doc = Jsoup.connect(url).userAgent(randomStr(Constan.USER_AGENT_LIST)).get();
                Elements lis = doc.select("#pins").select("li");
                int liIndex = 0;
                for (Element li : lis) {
                    logger.info("----------总数: {} , 当前数: {} .", lis.size(), liIndex++);
                    String docUrl = li.select("a").get(0).attr("href");
                    try {
                    imgDoc = Jsoup.connect(docUrl).userAgent(randomStr(Constan.USER_AGENT_LIST)).get();
                    img = imgDoc.select("div.main-image p a img").first();
                    String imgUrl = img.attr("src");
                    String imgName = img.attr("alt");
                    logger.info("图片url ---- {}", imgUrl);
                    String path = imgPath + imgName + imgUrl.substring(imgUrl.lastIndexOf("."));
                    downImage(imgUrl, path);
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e) {
                System.out.println("读取失败或写入失败");
                e.printStackTrace();
            } catch (Exception e) {
            }
        }
    }

    @Test
    public void testDocument() {
        InputStream inStream = null;
        String strUrl = "https://www.mzitu.com/131458";
        try {

            // URL url = new URL(strUrl);
            // HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            // // 设置Java服务器代理连接，要不然报错403
            // // 浏览器可以访问此url图片并显示，但用Java程序就不行报错Server returned HTTP response code:403 for
            // URL
            // // 具体原因：服务器的安全设置不接受Java程序作为客户端访问(被屏蔽)，解决办法是设置客户端的User Agent
            // conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;
            // Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            // // 很多网站的防采集的办法,就是判断浏览器来源referer和cookie以及userAgent
            // // 参考：https://blog.csdn.net/chengyingzhilian/article/details/7835400
            // // https://blog.csdn.net/wl981292580/article/details/80351136
            // conn.setRequestProperty("Referer", strUrl);
            // conn.setRequestMethod("GET");
            // conn.setConnectTimeout(30 * 1000);

            // conn.connect();
            // //得到响应码
            // int responseCode = conn.getResponseCode();
            // if(responseCode == HttpURLConnection.HTTP_OK){
            // //得到响应流
            // InputStream inputStream = conn.getInputStream();
            // //将响应流转换成字符串
            // String result = inputStream2String(inputStream);//将流转换为字符串。
            // logger.info("result============="+result);
            // }
            Document doc = Jsoup.connect(strUrl).get();
            // Document imgDoc = Jsoup.connect(strUrl).get();
            // String imgUrl = imgDoc.select("div.main-image p a img").first().attr("src");
            // logger.info("图片url ---- {}", imgUrl);
            logger.info(doc.body().html());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testDownImg() {
        String imgPath = "D://test/img/";
        String imgUrl = "https://i5.meizitu.net/2018/04/25c01.jpg";
        String path = imgUrl.replace("https://i5.meizitu.net", imgPath);
        try {
            downImage(imgUrl, imgPath + UUID.randomUUID() + imgUrl.substring(imgUrl.lastIndexOf(".")));
            logger.info("图片path ---- {}", path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testSub() {
        String imgUrl = "https://i5.meizitu.net/2018/04/25c01.jpg";
        logger.info( imgUrl.substring(imgUrl.lastIndexOf(".")));
    }

    public   String   inputStream2String(InputStream   is)   throws   Exception{ 
        ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
        int   i=-1; 
        while((i=is.read())!=-1){ 
        baos.write(i); 
        } 
        return   baos.toString(); 
    }
    //带编码的
    public String  inputStream2String  (InputStream  in , String encoding)  throws  Exception  {   
        StringBuffer  out  =  new  StringBuffer();   
        InputStreamReader inread = new InputStreamReader(in,encoding);   
           
        char[]  b  =  new  char[4096];   
        for  (int  n;  (n  =  inread.read(b))  !=  -1;)  {   
                out.append(new  String(b,  0,  n));   
        }   
          return out.toString();   
    } 

    /**
     * 随机获取集合元素
     */
    public String randomStr(List<String> list) {
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }

   /**
     * 下载图片
     *
     * @param imageUrl 图片url
     * @param pathName 文件保存时的位置
     */
    public Boolean downImage(String imageUrl, String pathName) throws Exception {
        byte[] btImg = getImageFromNetByUrl(imageUrl);
        if(null != btImg && btImg.length > 0){
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
                //  Logger.error("下载图片异常,图片url为:{}, pathname为{},异常信息{}", imageUrl, pathName, e.getMessage());
                throw e;
            }
        }else{
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
    public byte[] getImageFromNetByUrl(String strUrl) throws IOException {
        InputStream inStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            // 设置Java服务器代理连接，要不然报错403
            // 浏览器可以访问此url图片并显示，但用Java程序就不行报错Server returned HTTP response code:403 for URL
            // 具体原因：服务器的安全设置不接受Java程序作为客户端访问(被屏蔽)，解决办法是设置客户端的User Agent
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            // 很多网站的防采集的办法,就是判断浏览器来源referer和cookie以及userAgent
            // 参考：https://blog.csdn.net/chengyingzhilian/article/details/7835400
            //       https://blog.csdn.net/wl981292580/article/details/80351136
            conn.setRequestProperty("Referer", strUrl);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30 * 1000);
            //通过输入流获取图片数据
            inStream = conn.getInputStream();
            //得到图片的二进制数据
            byte[] btImg = readInputStream(inStream);
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null){
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
    public byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
    /**
     * 创建文件夹
     */
    public void mkdir(String path) {
        File file=new File(path);
		if(!file.exists()){//如果文件夹不存在
			file.mkdir();//创建文件夹
		}
    }
}