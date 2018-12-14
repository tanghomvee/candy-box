package com.slst.datacollector.web.ctrls;

import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.enums.*;
import com.slst.common.utils.DateUtils;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.datacollector.service.DataCollectorService;
import com.slst.datacollector.web.vo.WIFIReportVO;
import com.slst.device.dao.model.Device;
import com.slst.device.service.DeviceService;
import com.slst.vender.service.StoreRuleService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = {"/collector", "/slstweb/data"})
//@RequestMapping(path = "/collector")
public class DataCollectorCtrl extends BaseCtrl {

    private static String RET_VAL_YES = "ok";
    private static String RET_VAL_NO = "error";

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private DataCollectorService dataCollectorService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private StoreRuleService storeRuleService;

    @RequestMapping(path = {"/collect", "/upload3"}, method = {RequestMethod.GET, RequestMethod.POST})
//    @RequestMapping(path = {"/collect"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public String collect(@RequestBody String data) {
        if (StringUtils.isEmpty(data)) {
            LOGGER.error("设备上报数据为空");
            return RET_VAL_NO;
        }
        LOGGER.info("设备上报数据内容:{}", data);
        WIFIReportVO wifiReportVO = WIFIReportVO.buildWIFIReportVO(data);
        if (wifiReportVO == null) {
            LOGGER.error("解析上报数据为WIFIReportVO对象失败:{}", data);
            return RET_VAL_NO;
        }
        if (StringUtils.isEmpty(wifiReportVO.getSta()) || StringUtils.isEmpty(wifiReportVO.getType()) || StringUtils.isEmpty(wifiReportVO.getData())) {
            return RET_VAL_YES;
        }

        ReportTypeEnum typeEnum = ReportTypeEnum.getByVal(wifiReportVO.getType());
        if (typeEnum == null) {
            LOGGER.error("解析上报数据类型失败:{}", data);
            return RET_VAL_NO;
        }

        if (!checkData(wifiReportVO).isSuccess()) {
            return RET_VAL_NO;
        }
        Msg msg = Msg.error();
        switch (typeEnum) {
            case DATA_FLASH:
            case DATA_WIFI_B:
            case DATA_WIFI_A:
                if (CollectionUtils.isEmpty(wifiReportVO.getMacRssi())) {
                    LOGGER.error("上报数据Mac为空:{}", Base64Utils.encodeToString(data.getBytes()));
                }
                msg = dataCollectorService.doWifiReportedData(wifiReportVO);
                break;
            case CFG_WIFI_A:
            case CFG_WIFI_B:
                msg = dataCollectorService.doCfgWIFI(wifiReportVO);
                break;
            case AP_WIFI:
                msg = dataCollectorService.doAP(wifiReportVO);
                break;
            case CHECK_WIFI:
                msg = dataCollectorService.doCheck(wifiReportVO);
                break;
            case TIME_WIFI:
                msg = doServerTime();
                return msg.isSuccess() ? msg.getData().toString() : RET_VAL_NO;
        }

        return msg.isSuccess() ? RET_VAL_YES : RET_VAL_NO;
    }


    private Msg checkData(WIFIReportVO wifiReportVO) {

        String deviceMac = wifiReportVO.getSta();
        Device device = deviceService.findByMac(deviceMac);

        if (null == device || YNEnum.NO.getVal().equals(device.getYn()) || DeviceStateEnum.UN_ACTIVE.getVal().equals(device.getState())) {
            LOGGER.warn("设备mac={},不可使用或者未激活:{}", deviceMac, device);
            return Msg.error("未找到设备");
        }
        Long storeId = device.getStoreId();
        if (storeId == null || storeId < 1) {
            LOGGER.warn("设备mac={},storeId={}门店不存在:{}", deviceMac, storeId, device);
            return Msg.error();
        }


        //2.时间点限制
        boolean rs = storeRuleService.execute(Long.valueOf(storeId), RuleTypeEnum.RULE_TYPE_ST_COLLECT_TIME_LIMIT.getVal(), DateTime.now().getHourOfDay(), (String param) -> Integer.valueOf(param));
        if (!rs) {
            return Msg.error();
        }
        //3.日期限制
        rs = storeRuleService.execute(Long.valueOf(storeId), RuleTypeEnum.RULE_TYPE_ST_COLLECT_DATE_LIMIT.getVal(), DateTime.now().toDate(),
                (String param) -> DateUtils.str2DateTime(param, DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS));
        if (!rs) {
            return Msg.error();
        }

        return Msg.success();
    }

    private Msg doServerTime() {
        String pattern = "{0}-timestart-{1}-timeend";
        return Msg.success((Object) MessageFormat.format(pattern, RET_VAL_YES, System.currentTimeMillis() / 1000));
    }

}
