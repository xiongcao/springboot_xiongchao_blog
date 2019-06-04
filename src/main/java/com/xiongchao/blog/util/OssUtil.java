package com.xiongchao.blog.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xiongchao.blog.bean.BaseResult;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Component
public class OssUtil {

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    /**
     * 初始化OssClient
     *
     * @return
     */
    private OSS initOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public BaseResult uploadFile(MultipartFile file, String folder) {
        try {
            if (file == null) return BaseResult.failure("文件为空");

            // 文件名
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) return BaseResult.failure("文件名为空");

            String key = upload(folder, fileName, file.getInputStream());

            return BaseResult.success(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseResult.failure("上传失败");
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public BaseResult uploadFile(File file, String folder) {
        try {
            if (file == null) return BaseResult.failure("文件为空");

            // 文件名
            String fileName = file.getName();
            if (StringUtils.isEmpty(fileName)) return BaseResult.failure("文件名为空");

            String key = upload(folder, fileName, new FileInputStream(file));

            return BaseResult.success(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseResult.failure("上传失败");
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

            InputStream inputStream = connection.getInputStream();
            String key = upload(folder, ".png", inputStream);
            return BaseResult.success(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseResult.failure("上传失败");
    }

    private String upload(String folder, String fileName, InputStream inputStream) throws IOException {
        // 文件名重新生成
        String key = folder + "/" + getName(fileName);
        OSS ossClient = initOssClient();
        ossClient.putObject(bucketName, key, inputStream);
        ossClient.shutdown();
        inputStream.close();
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
