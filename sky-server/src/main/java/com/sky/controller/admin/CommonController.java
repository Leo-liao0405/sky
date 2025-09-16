package com.sky.controller.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    
    @Autowired
    private AliOssUtil aliOssUtil;
    /*
     * 文件上传
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        //上传到阿里云服务器
        try {
            //获取原始文件名
            String originalFilenam = file.getOriginalFilename();

            //获取文件后缀名
            String extenSion = originalFilenam.substring(originalFilenam.lastIndexOf("."));
            //使用UUID重新生成文件名，防止文件名重复造成文件覆盖
            String ObjectName = UUID.randomUUID().toString() + extenSion;
            //调用工具类的方法实现上传（返回为文件路径网址）
            String filepath = aliOssUtil.upload(file.getBytes(),ObjectName);

            return Result.success(filepath);
        } catch (Exception e) {
            // 说明上传失败
            log.error("文件上传失败：{}", e);
        }
        
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }













}
