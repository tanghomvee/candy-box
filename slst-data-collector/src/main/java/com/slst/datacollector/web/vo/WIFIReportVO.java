package com.slst.datacollector.web.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slst.common.enums.ReportTypeEnum;
import com.slst.common.enums.SeparatorEnum;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.slst.common.enums.ReportTypeEnum.DATA_FLASH;
import static com.slst.common.enums.ReportTypeEnum.getByVal;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-09 16:46
 */
public class WIFIReportVO implements Serializable {

    protected static Logger LOGGER = LoggerFactory.getLogger(WIFIReportVO.class);

    /**
     * 探针上报的 id 序列
     */
    private String id;

    /**
     * 为上报信息的探针 mac 地址，12 个字母或数字组合
     */
    private String sta;

    /**
     * 是该探针上设置的 shop id 数值, 比如安装在不同店的探针 shop 可以配置不同值，这样可以根据 shop 进行服务器端负载分发
     */
    private String shop;

    /**
     * 是探针上设置的秘钥，可以通过 app 进行配置修改
     */
    private String token;

    /**
     * 该上报的类型,probea 表示 wifi a 芯片探测到的 mac 信息
     */
    private String type;

    /**
     * 表示上报的具体数据
     */
    private String data;

    private String r;

    /**
     * 每个手机的MAC地址对应的rssi值
     */
    private Map<String , ArrayList<Integer>> macRssi = Maps.newHashMap();

    public  static  WIFIReportVO buildWIFIReportVO(String data){
        WIFIReportVO wifiReportVO = new WIFIReportVO();
        String regex = "sta=|&shop=|&token=|&r=|&id=|&type=|&data=";

        try {
            data = URLDecoder.decode(data , Consts.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("解析上报的数据异常:{}" , data , e);
            return wifiReportVO;
        }

        String[] vals = data.split(regex);
        if (ObjectUtils.isEmpty(vals) || vals.length !=8 ){
            LOGGER.warn("上报的数据格式错误:{}" , data);
        }

        int len = vals.length;
        wifiReportVO.setSta(len > 1 ? vals[1] : null);
        wifiReportVO.setShop(len > 2 ? vals[2] : null);
        wifiReportVO.setToken(len > 3 ? vals[3] : null);
        wifiReportVO.setR(len > 4 ? vals[4] : null);
        wifiReportVO.setId(len > 5 ? vals[5] : null);
        wifiReportVO.setType(len > 6 ? vals[6] : null);
        wifiReportVO.setData(len > 7 ? vals[7] : null);


        if(!StringUtils.isEmpty(wifiReportVO.getData())){
            ReportTypeEnum typeEnum = getByVal(wifiReportVO.getType());
            if(typeEnum != null){

                switch (typeEnum){
                    case DATA_FLASH:
                    case DATA_WIFI_B:
                    case DATA_WIFI_A:
                        wifiReportVO.setMacRssi(transferData(wifiReportVO.getData() , typeEnum));
                }
            }
        }

        //
        if(!StringUtils.isEmpty(wifiReportVO.getSta()) && wifiReportVO.getSta().length()==12){
            StringBuffer sb = new StringBuffer(wifiReportVO.getSta());
            sb
            .insert(2 , SeparatorEnum.COLON.getVal())
            .insert(5 ,SeparatorEnum.COLON.getVal())
            .insert(8 ,SeparatorEnum.COLON.getVal())
            .insert(11 ,SeparatorEnum.COLON.getVal())
            .insert(14 ,SeparatorEnum.COLON.getVal());
            wifiReportVO.setSta(sb.toString().toUpperCase());
        }

        return wifiReportVO;
    }


    private static Map<String, ArrayList<Integer>> transferData(String data , ReportTypeEnum typeEnum){
        String delimiter = typeEnum.getSeparator();

        //正确的数据以分隔符开始,如果第一个字母不是分隔符,就去除掉,从找到 的第一个分隔符开始
        int start = data.indexOf(delimiter);

        if(start == -1){
            return null;
        }

        //正确的数据以分隔符结束,如果最后一个字母不是分隔符,就去除掉,从最后一个分割符结束
        int end = data.lastIndexOf(delimiter);

        //因为 mac 地址为 12 字母,加上至少一个 rssi,因此长度要大于 12
        int minLen = 13;
        if((end-start) < minLen){
            return null;
        }
        Map<String, ArrayList<Integer>> maps = Maps.newHashMap();
        //去除掉两边可能的脏数据
        String dataTrim = data.substring(start + 1, end);
        String[] datas = dataTrim.split(delimiter);

        Arrays.stream(datas).forEach((String item) ->{
            if(item.length() < minLen){
                return;
            }
            //每一段的前 12 个字符为 mac 地址
            String mac = item.substring(0, 12);
            StringBuffer sb = new StringBuffer(mac);
            sb.
            insert(2 , SeparatorEnum.COLON.getVal()).
            insert(5 ,SeparatorEnum.COLON.getVal()).
            insert(8 ,SeparatorEnum.COLON.getVal()).
            insert(11 ,SeparatorEnum.COLON.getVal()).
            insert(14 ,SeparatorEnum.COLON.getVal());

            mac = sb.toString().toUpperCase();

            ArrayList<Integer> rssiList = Lists.newArrayList();
            maps.put(mac , rssiList);
            //将rssis 转为 byte 处理,不同语言方法不同
            byte[] macRssis = StringUtils.replace(item , mac , "").getBytes();

            int len = macRssis.length;

            if (typeEnum == DATA_FLASH){
                len = 2;
            }
            for(int j = 0 ; j < len; j++) {
                int rssi=macRssis[j];
                //正确的rssi, 进行处理即可,rssi应该是负数,但我们为了减少传输字节,用的是绝对值,rssi是一个9到99的数 值
                if(rssi <= 9 || rssi >= 100){
                    continue;
                }
                rssiList.add(rssi);
            }
        });

        return maps;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {

        this.sta = sta;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, ArrayList<Integer>> getMacRssi() {
        return macRssi;
    }

    public void setMacRssi(Map<String, ArrayList<Integer>> macRssi) {
        this.macRssi = macRssi;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "WIFIReportVO{" +
                "id='" + id + '\'' +
                ", sta='" + sta + '\'' +
                ", shop='" + shop + '\'' +
                ", token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", r='" + r + '\'' +
                ", macRssi=" + macRssi +
                '}';
    }
}
