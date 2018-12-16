package com.candybox.common.service.impl;

import com.candybox.common.service.ImageService;
import com.candybox.common.utils.ImageUtils;
import org.springframework.stereotype.Service;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/5/22 17:06
 */
@Service("imageService")
public class ImageServiceImpl implements ImageService {

    @Override
    public String saveBizLicense(String imgStr, String realPath, String basePath) {

        String savePath = "/resource/bizlicense/";

        return ImageUtils.saveImge(imgStr,realPath,savePath,basePath);
    }

    @Override
    public String saveLogo(String imgStr, String realPath, String basePath) {
        String savePath = "/resource/logo/";

        return ImageUtils.saveImge(imgStr,realPath,savePath,basePath);
    }
}
