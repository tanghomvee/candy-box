package com.candybox.common.utils;

import com.mysql.cj.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/5/22 15:09
 */
public class ImageUtils {

    /**
     * 二进制转图片
     *
     * @param imgStr 二进制
     * @param path   保存路劲
     * @return true转换成功，反之失败
     */
    private static boolean generateImage(String imgStr, String path) {
        if (null != imgStr) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] b = decoder.decodeBuffer(imgStr);
                for (int i = 0; i < b.length; i++) {
                    if (b[i] < 0) {

                        b[i] += 256;
                    }
                }
                OutputStream stream = new FileOutputStream(path);
                stream.write(b);
                stream.flush();
                stream.close();
                return true;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获得随机文件名
     *
     * @return
     */
    private static String getFileName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static String[] getImgStrAndImgType(String imgStr) {
        String[] imgProperty = new String[2];
        if (StringUtils.isNullOrEmpty(imgStr)){
            return new String[0];
        }
        imgProperty[0] = imgStr.substring(imgStr.indexOf(",") + 1);
        imgProperty[1] = "." + imgStr.substring(imgStr.indexOf("/") + 1, imgStr.indexOf(";"));
        return imgProperty;
    }

    /**
     * 保存图片,并返回访问路径
     *
     * @param imgStr   图片二进制编码
     * @param realPath 项目物理根目录
     * @param savePath 图片保存路径
     * @param basePath 网站访问路径
     * @return 返回图片完整访问地址 accsPath
     */
    public static String saveImge(String imgStr, String realPath, String savePath, String basePath) {

        if (getImgStrAndImgType(imgStr).length<=0){
            return "";
        }

        String imgCode = getImgStrAndImgType(imgStr)[0];
        String fileName = getFileName();
        String fileType = getImgStrAndImgType(imgStr)[1];

        //拼接图片访问地址
        StringBuffer accsPath = new StringBuffer(basePath);
        accsPath.append(savePath);
        accsPath.append(fileName);
        accsPath.append(fileType);

        //拼接图片保存地址
        StringBuffer filePath = new StringBuffer(realPath);
        savePath = savePath.replace("/", File.separator);
        filePath.append(savePath);
        filePath.append(fileName);
        filePath.append(fileType);

        boolean isSuc = generateImage(imgCode, filePath.toString());

        return isSuc ? accsPath.toString() : "";
    }

}
