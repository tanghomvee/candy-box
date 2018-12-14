package com.slst.common.service;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/5/22 17:01
 */
public interface ImageService {

    /**
     * 保存营业执照
     *
     * @param imgStr   图片二进制
     * @param realPath 物理根目录
     * @param basePath 网站访问路径
     * @return 返回图片完整访问路径
     */
    String saveBizLicense(String imgStr, String realPath, String basePath);

    /**
     * 保存LOGO
     *
     * @param imgStr   图片二进制
     * @param realPath 物理根目录
     * @param basePath 网站访问路径
     * @return 返回图片完整访问路径
     */
    String saveLogo(String imgStr, String realPath, String basePath);

}
