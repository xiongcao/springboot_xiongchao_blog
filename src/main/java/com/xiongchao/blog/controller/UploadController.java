package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 熊超
 */
@Api(description = "文件", tags = "uploadFile")
@RestController
@RequestMapping("file")
public class UploadController {

    @Autowired
    private FileUtil fileUtil;


    @PostMapping("{folder}")
    @ApiOperation("文件上传")
    public BaseResult upload(@RequestParam("file") MultipartFile file,
                             @ApiParam("文件类型") @PathVariable("folder") String folder) {
        return fileUtil.uploadFile(file, folder);
    }

    @PostMapping("url/{folder}")
    @ApiOperation("上传网络文件")
    public BaseResult uploadUrl(@RequestParam("url") String url,
                             @ApiParam("文件类型") @PathVariable("folder") String folder) {
        return fileUtil.uploadUrl(url, folder);
    }

}
