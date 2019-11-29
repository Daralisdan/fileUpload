package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片上传
 */
@PropertySource("classpath:application.properties")
@Controller
public class FileUpload {
    @Autowired
    private Environment environment;
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping("/fileupload")
    public String fileUpload(MultipartFile filecontent) throws IOException {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssFFF");
        String imageName=df.format(new Date())+"qwe"+filecontent.getOriginalFilename();
        boolean c = cacheFile(filecontent, imageName, environment.getProperty("imageUrl.urls"));
        if (c) {
            return "成功";
        }
        return "失败";
    }

    public static boolean cacheFile(MultipartFile filecontent, String names, String filePath) throws IOException {
        OutputStream os = null;
        InputStream inputStream = null;
        try {
            inputStream = filecontent.getInputStream();

            String path = filePath;
            byte[] bs = new byte[1024];
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + names);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (Exception e) {
            return false;
        } finally {
            os.close();
            inputStream.close();
        }
        return true;
    }
}
