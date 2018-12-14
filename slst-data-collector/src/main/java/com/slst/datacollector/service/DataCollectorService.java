package com.slst.datacollector.service;

import com.slst.common.web.vo.Msg;
import com.slst.datacollector.web.vo.WIFIReportVO;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface DataCollectorService {
    /**
     * 处理设备上报的心跳数据
     * @param wifiReportVO
     * @return
     */
    Msg doCheck(WIFIReportVO wifiReportVO);

    /**
     * 处理设备上报的WIFI配置信息
     * @param wifiReportVO
     * @return
     */
    Msg doCfgWIFI(WIFIReportVO wifiReportVO);

    /**
     *处理设备上报的AP信息
     * @param wifiReportVO
     * @return
     */
    Msg doAP(WIFIReportVO wifiReportVO);

    /**
     * 处理WIFI上报的Mac信息
     * 包括 WiFi-A & wifi-B & flash
     * @param wifiReportVO
     * @return
     */
    Msg doWifiReportedData(WIFIReportVO wifiReportVO);
}
