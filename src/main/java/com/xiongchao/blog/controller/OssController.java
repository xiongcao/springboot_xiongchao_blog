package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.util.OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xiong Chao
 */
@Api(description = "对象管理", tags = "oss")
@RestController
@RequestMapping("oss")
public class OssController {

    @Autowired
    private OssUtil ossUtil;

//    @Autowired
//    private WxMpService wxMpService;

    @PostMapping("{folder}")
    @ApiOperation("文件上传")
    public BaseResult upload(@RequestParam("file") MultipartFile file,
                             @ApiParam("文件类型") @PathVariable("folder") String folder) {
        return ossUtil.uploadFile(file, folder);
    }

//    @PostMapping("{folder}/{mediaId}")
//    @ApiOperation("微信媒体文件上传")
//    public BaseResult upload(@ApiParam("文件类型") @PathVariable("folder") String folder,
//                             @ApiParam("微信临时文件ID") @PathVariable("mediaId") String mediaId) throws WxErrorException {
//
//        File file = wxMpService.getMaterialService().mediaDownload(mediaId);
//        return ossUtil.uploadFile(file, folder);
//    }
}
