package com.xiongchao.blog.util;

import com.xiongchao.blog.bean.BaseResult;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Component
public class FileUtil {

    @Value("${server.file-path}")
    private String filePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public BaseResult uploadFile(MultipartFile file, String folder){
        try {
            if (file == null) return BaseResult.failure("文件为空");
            // 文件名
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) return BaseResult.failure("文件名为空");
            String key = upload(folder, fileName, file);
            return BaseResult.success(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseResult.success();
    }

    /**
     * 网络文件上传
     *
     * @param url
     * @param folder
     * @return
     */
    public BaseResult uploadUrl(String url, String folder) {
        try {
            // 打开资源链接
            URLConnection connection = new URL(url).openConnection();
            // 设置超时间为6秒
            connection.setConnectTimeout(6*1000);
            // 防止屏蔽程序抓取而返回403错误
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = connection.getInputStream();

            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(filePath + folder);
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            // 文件名重新生成
            String fileName = getName(url);
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
            return BaseResult.success(folder + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseResult.failure("上传失败");
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private String upload(String folder, String fileName, MultipartFile file) throws IOException {
        // 文件名重新生成
        String key = folder + "/" + getName(fileName);
        File dest = new File(filePath + folder);
        if(!dest.exists()){
            dest.mkdirs();
        }
        dest = new File(filePath + key);
        file.transferTo(dest);
        return key;
    }

    /**
     * 根据文件名生成新的文件名
     *
     * @param fileName
     * @return
     */
    private String getName(String fileName) {
        // 文件后缀
        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 8位随机字符(a-zA-Z0-9)
        return RandomString.make() + fileExt;
    }
}
