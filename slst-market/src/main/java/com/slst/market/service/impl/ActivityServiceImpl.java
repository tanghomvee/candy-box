package com.slst.market.service.impl;

import com.google.common.collect.Maps;
import com.slst.common.enums.ActivityTypeEnum;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.DateUtils;
import com.slst.common.utils.PageableUtil;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.ActivityDao;
import com.slst.market.dao.model.Activity;
import com.slst.market.dao.model.SmsBox;
import com.slst.market.dao.model.SmsFee;
import com.slst.market.service.ActivityService;
import com.slst.market.service.SmsBoxService;
import com.slst.market.service.SmsFeeService;
import com.slst.market.service.SmsRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service("ActivityService")
public class ActivityServiceImpl
        extends BaseServiceImpl<Activity, Long> implements ActivityService {

//    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ActivityDao activityDao;


    @Resource
    private SmsBoxService smsBoxService;

    @Resource
    private SmsRecordService smsRecordService;

    @Resource
    private SmsFeeService smsFeeService;

    /**
     * 新建活动
     *
     * @param curUser
     * @param activity
     * @return
     */
    @Override
    public Activity createActivity(UserVO curUser, Activity activity) {
        //activName，startTime，endTime，userId，venderId，venderName，storeId，storeName，yn，creator，createTime
        String creator = curUser.getUserName();
        Date curDate = new Date();  //当前时间
        Integer yes = YNEnum.YES.getVal();

        activity.setCreator(creator);
        activity.setCreateTime(curDate);
        activity.setYn(yes);
        activity.setState(1);

        activity.setUserId(curUser.getId());
//        if (curUser.getUserType() == UserTypeEnum.VENDER_EMP.getVal()) {
//
//            activity.setVenderId(curUser.getVenderId());
//            activity.setVenderName(curUser.getVenderName());
//            activity.setStoreId(curUser.getStoreId());
//            activity.setStoreName(curUser.getStoreName());
//        } else if (curUser.getUserType() == UserTypeEnum.VENDER.getVal()) {
//            activity.setVenderId(curUser.getVenderId());
//            activity.setVenderName(curUser.getDisplayName());
//            activity.setStoreId(0L);
//            activity.setStoreName("");
//
//        } else {
//            activity.setVenderId(0L);
//            activity.setVenderName("");
//            activity.setStoreId(0L);
//            activity.setStoreName("");
//        }

        if (null!=curUser.getStoreId() && curUser.getStoreId()>0L){
            activity.setVenderId(curUser.getVenderId());
            activity.setVenderName(curUser.getVenderName());
            activity.setStoreId(curUser.getStoreId());
            activity.setStoreName(curUser.getStoreName());
        }else{
            activity.setVenderId(curUser.getVenderId());
            activity.setVenderName(curUser.getDisplayName());
            activity.setStoreId(0L);
            activity.setStoreName("");
        }

        //用户类型为商家员工,设置员工Id
        if(curUser.getUserType() == UserTypeEnum.VENDER_EMP.getVal()){
            activity.setEmpId(curUser.getVenderEmpId());
        }

        Activity rtnActivity = null;

        try {
            rtnActivity = activityDao.save(activity);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("新建活动，数据库操作异常。" + e);
            return null;
        }

        return rtnActivity;
    }

    /**
     * 更新活动
     *
     * @param userVO
     * @param activity
     * @return
     */
    @Override
    public Activity modifyActivity(UserVO userVO, Activity activity) {

        String changer = userVO.getUserName();
        Date curDate = new Date();  //当前时间
        Long activId = activity.getId();
        String activName = activity.getActivName();
        Date startTime = activity.getStartTime();
        Date endTime = activity.getEndTime();

        Activity findActiv = null;
        try {
            findActiv = activityDao.findById(activId).get();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据ID：" + activId + "查找活动，数据库操作异常。" + e);
            return null;
        }
        //如果没有更改任何内容
        if (null != findActiv && findActiv.getActivName().equals(activName) && findActiv.getStartTime().getTime() == startTime.getTime() && findActiv.getEndTime().getTime() == endTime.getTime()) {
            return findActiv;
        }

        findActiv.setChanger(changer);
        findActiv.setChangeTime(curDate);
        //可更改的内容：activName，startTime，endTime
        findActiv.setActivName(activName);
        findActiv.setStartTime(startTime);
        findActiv.setEndTime(endTime);
        Activity rtnActiv = null;
        try {
            rtnActiv = activityDao.saveAndFlush(findActiv);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("更新活动，，ID：" + activId + "，数据库操作异常。" + ex);
            return null;
        }

        return rtnActiv;
    }

    @Override
    public Activity findActivityById(Long id) {
        Optional<Activity> resultActivity = activityDao.findById(id);
        return resultActivity.isPresent() ? resultActivity.get() : null;
    }

    /**
     * 根据ID批量删除活动
     *
     * @param ids
     * @return
     */
    @Override
    public Msg deleteActivityByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return Msg.error("未选择要删除的活动。");
        }

        if (ids.contains(",")) {

            String[] idsArr = ids.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                try {
                    activityDao.deleteById(Long.parseLong(idsArr[i].trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("删除活动，ID：" + idsArr[i] + "，数据库操作异常。" + e);
                    return Msg.error("删除活动失败，请重试。");
                }
            }
        } else {

            try {
                activityDao.deleteById(Long.parseLong(ids.trim()));
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("删除活动，ID：" + ids + "，数据库操作异常。" + ex);
                return Msg.error("删除活动失败，请重试。");
            }
        }

        return Msg.success();
    }

    @Override
    public Msg deleteActivity(Long id, Integer type) {
        ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.getByVal(type);

        if (null == activityTypeEnum) {
            return Msg.error("未知活动类型");
        }

        switch (activityTypeEnum) {
            case SMS:
                break;
            case PHONE_CALL:
                break;
        }

        return null;
    }

    /**
     * 根据userId分页查找活动
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<Activity> findActivByUserId(Long userId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<Activity> activityPage = null;
        try {
            activityPage = activityDao.findByUserId(userId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据userId：" + userId + "查找活动，数据库操作异常。" + e);
            return null;
        }
        if (null == activityPage || activityPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return activityPage;
    }

    /**
     * 根据venderId分页查找活动
     *
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<Activity> findActivByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<Activity> activityPage = null;
        try {
            activityPage = activityDao.findByVenderId(venderId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据venderId：" + venderId + "查找活动，数据库操作异常。" + e);
            return null;
        }
        if (null == activityPage || activityPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return activityPage;
    }

    @Override
    public Page<Activity> findActivByEmpId(Long empId, Integer type, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Page<Activity> result = null;
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        try{
            if (type != null){
                if(StringUtils.isEmpty(activNameLike)){
                    result = activityDao.findByEmpIdAndType(empId, type, pageable);
                }else {
                    result = activityDao.findByEmpIdAndTypeAndActivNameContaining(empId, type, activNameLike, pageable);
                }
            }else {
                if(StringUtils.isEmpty(activNameLike)){
                    result = activityDao.findByEmpId(empId, pageable);
                }else {
                    result = activityDao.findByEmpIdAndActivNameContaining(empId, activNameLike, pageable);                    
                }                
            }      
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据empId={}查找活动，数据库操作异常。",empId, e);
            return null;
        }
        return result;
    }

    /**
     * 根据storeId分页查找活动
     *
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<Activity> findActivByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<Activity> activityPage = null;
        try {
            activityPage = activityDao.findByStoreId(storeId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据storeId：" + storeId + "查找活动，数据库操作异常。" + e);
            return null;
        }
        if (null == activityPage || activityPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return activityPage;
    }

    /**
     * 根据venderId和activNameLike模糊分页查询
     *
     * @param venderId
     * @param activNameLike
     * @param pageNum
     * @param pageSize
     * @param sortKey       排序关键字。默认createTime
     * @param orderKey      升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<Activity> findActivByVenderIdAndActivName(Long venderId, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<Activity> activityPage = null;
        try {
            activityPage = activityDao.findByVenderIdAndActivNameContaining(venderId, activNameLike, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据venderId：" + venderId + "查找名称包含：" + activNameLike + "的活动，数据库操作异常。" + e);
            return null;
        }
        if (null == activityPage || activityPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return activityPage;
    }

    /**
     * 根据storeId和activNameLike模糊分页查询
     *
     * @param storeId
     * @param activNameLike
     * @param pageNum
     * @param pageSize
     * @param sortKey       排序关键字。默认createTime
     * @param orderKey      升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<Activity> findActivByStoreIdAndActivName(Long storeId, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<Activity> activityPage = null;
        try {
            activityPage = activityDao.findByStoreIdAndActivNameContaining(storeId, activNameLike, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据storeId：" + storeId + "查找名称包含：" + activNameLike + "的活动，数据库操作异常。" + e);
            return null;
        }
        if (null == activityPage || activityPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return activityPage;
    }

    @Override
    public Page<Activity> findByVenderIdAndTypeAndActivName(Long venderId, Integer type, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        if (StringUtils.isEmpty(activNameLike)) {
            return activityDao.findByVenderIdAndType(venderId, type, pageable);
        } else {
            return activityDao.findByVenderIdAndTypeAndActivNameContaining(venderId, type, activNameLike, pageable);
        }
    }

    @Override
    public Page<Activity> findByStoreIdAndTypeAndActivName(Long storeId, Integer type, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        if (StringUtils.isEmpty(activNameLike)) {
            return activityDao.findByStoreIdAndType(storeId, type, pageable);
        } else {
            return activityDao.findByStoreIdAndTypeAndActivNameContaining(storeId, type, activNameLike, pageable);
        }
    }

    /**
     * 活动smsBox详情页面，头部展示内容
     *
     * @param venderId
     * @param smsBoxId
     * @return Map<String       ,       Object>.
     * key : Title,短信名称;sendTime,短信发送时间;sender,发件人;smsFee,短信单价;totalAmount,短信总价;smsContent,短信内容
     */
    @Override
    public Map<String, Object> showSummaryOfSmsBox(Long venderId, Long smsBoxId) {
        Map<String, Object> resultMap = Maps.newHashMap();

        SmsFee rtnSmsFee = smsFeeService.findSmsFeeByVenderId(venderId);
        if (null == rtnSmsFee) return null;
        Long longSmsFee = rtnSmsFee.getFee();
        Double smsFee = new BigDecimal(String.valueOf(longSmsFee)).multiply(new BigDecimal("0.001")).doubleValue();

        SmsBox rtnSmsBox = smsBoxService.findSmsBoxById(smsBoxId);
        if (null == rtnSmsBox) return null;
        String sender = rtnSmsBox.getCreator();
        Long longTotalAmount = rtnSmsBox.getPrice();
        Double totalAmount = new BigDecimal(String.valueOf(longTotalAmount)).multiply(new BigDecimal("0.001")).doubleValue();

        String smsContent = rtnSmsBox.getContent();
        Long storeId = rtnSmsBox.getStoreId();
        String Title = rtnSmsBox.getTitle();
        Date sendTime = rtnSmsBox.getSendTime();


        resultMap.put("Title", Title);
        resultMap.put("storeId", storeId);
        resultMap.put("sender", sender);
        resultMap.put("sendTime", sendTime);
        resultMap.put("smsFee", smsFee);
        resultMap.put("totalAmount", totalAmount);
        resultMap.put("smsContent", smsContent);

        return resultMap;

    }

    /**
     * 短信发送结果统计：成功，失败，正在发送（用于统计图）
     *
     * @param smsBoxId
     * @return Map<String       ,       Object>. key: successCount,成功条数；failedCount,失败条数；pendingCount,正在发送条数
     */
    @Override
    public Map<String, Object> resultOfSendingSms(Long smsBoxId) {
        Map<String, Object> resultMap = Maps.newHashMap();

        Long successCount = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, 1);  //成功
        Long failedCount = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, -1);   //失败
        Long pendingCount = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, 0);  //正在发送

        resultMap.put("successCount", successCount);
        resultMap.put("failedCount", failedCount);
        resultMap.put("pendingCount", pendingCount);

        return resultMap;
    }


    /**
     * 短信人群活动到店人数统计：到店，未到店（用于统计图）
     *
     * @param venderId
     * @param smsBoxId
     * @return Map<String       ,       Object>. key: arriveAmount,到店；notArriveAmount,未到店；totalAmount,此smsBox短信发送总人数
     */
    @Override
    public Map<String, Object> resultOfArrive(Long venderId, Long smsBoxId) {
        Map<String, Object> resultMap = Maps.newHashMap();

        SmsBox rtnSmsBox = smsBoxService.findSmsBoxById(smsBoxId);
        if (null == rtnSmsBox) return null;
        Long activityId = rtnSmsBox.getActivityId();
        Long storeId = rtnSmsBox.getStoreId();

        Activity rtnActiv = null;
        try {
            rtnActiv = activityDao.findById(activityId).get();

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找活动出错。");
            return null;
        }
        Date startTime = rtnSmsBox.getCreateTime();   //短信发送时间
        Date endTime = rtnActiv.getEndTime();   //活动结束时间
        endTime = DateUtils.addOrSubDate(endTime, 1);

        Long arriveAmount = null;    //短信到店人数
        if (null == storeId || 0 == storeId) {    //商家活动
            arriveAmount = activityDao.countSmsArriveCustomerOfVender(smsBoxId, startTime, endTime, venderId);

        } else { //店铺活动
            arriveAmount = activityDao.countSmsArriveCustomerOfStore(smsBoxId, startTime, endTime, storeId);

        }

        Long totalAmount = rtnSmsBox.getCount();//短信发送总人数
        Long notArriveAmount = totalAmount - arriveAmount;  //短信未到店人数

        resultMap.put("arriveAmount", arriveAmount);
        resultMap.put("notArriveAmount", notArriveAmount);
        resultMap.put("totalAmount", totalAmount);

        return resultMap;
    }


    /**
     * 活动smsBox详情页面，底部展示内容
     *
     * @param venderId
     * @param smsBoxId
     * @return Map<String       ,       Object>. key: successCount,成功条数；failedCount,失败条数；pendingCount,正在发送条数；
     * arriveAmount,到店；notArriveAmount,未到店；totalAmount,此smsBox短信发送总人数；arriveRate,到店率
     */
    @Override
    public Map<String, Object> summaryOfSmsAndArrive(Long venderId, Long smsBoxId) {
        Map<String, Object> resultMap = Maps.newHashMap();

        //短信发送统计：
        Map<String, Object> smsResultMap = resultOfSendingSms(smsBoxId);
        resultMap.putAll(smsResultMap);

        //到店人数统计
        Map<String, Object> arrivMap = resultOfArrive(venderId, smsBoxId);
        resultMap.putAll(arrivMap);

        //计算营销率
        Long arriveAmount = (Long) arrivMap.get("arriveAmount");    //短信到店人数
        Long totalAmount = (Long) arrivMap.get("totalAmount");  //短信发送总人数
        BigDecimal b1 = new BigDecimal(String.valueOf(arriveAmount));
        BigDecimal b2 = new BigDecimal(String.valueOf(totalAmount));

        Double arriveRate = 0.00;

        if (b2.compareTo(new BigDecimal(0)) == 1) {
            arriveRate = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();//到店率，四舍五入到小数点后4位
        }

        resultMap.put("arriveRate", arriveRate);

        return resultMap;

    }


    /**
     * (用于短信发送详情展示)。指定smsBoxId下，短信详情（电话，发送状态）展示,按照state降序（成功-正在发送-失败）
     *
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @return Page<Object       [       ]>:[0]:mobile;[1]:errmsg
     */
    @Override
    public Page<Object[]> showDetailOfSms(Long smsBoxId, int pageNum, int pageSize) {

        Page<Object[]> rtnPage = smsRecordService.findMobileAndErrmsgByBoxId(smsBoxId, pageNum, pageSize);
        return rtnPage;
    }


    /**
     * (用于客户到店详情展示)。指定smsBoxId下，到店顾客详情（电话，到店时间，离店时间）展示。按照到店时间降序
     *
     * @param venderId
     * @param smsBoxId
     * @param mobileLike
     * @param pageNum
     * @param pageSize
     * @return Page<Object       [       ]>:[0]:mobile；[1]:arriveTime；[2]leaveTime
     */
    @Override
    public Page<Object[]> showDetailOfArriveCust(Long venderId, Long smsBoxId, String mobileLike, int pageNum, int pageSize) {


        SmsBox rtnSmsBox = smsBoxService.findSmsBoxById(smsBoxId);
        if (null == rtnSmsBox) return null;
        Long activityId = rtnSmsBox.getActivityId();
        Long storeId = rtnSmsBox.getStoreId();

        Activity rtnActiv = null;
        try {
            rtnActiv = activityDao.findById(activityId).get();

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找活动出错。" + e);
            return null;
        }
        Date startTime = rtnSmsBox.getCreateTime();   //短信发送时间
        Date endTime = rtnActiv.getEndTime();   //活动结束时间

        endTime = DateUtils.addOrSubDate(endTime, 1);

        Page<Object[]> rtnPage = null;    //短信到店人数
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, "arriveTime", null);
        if (null == storeId || 0 == storeId) {    //商家活动
            if (StringUtils.isEmpty(mobileLike)) {  //没有搜索mobile
                rtnPage = activityDao.findSmsArriveMobileAndTimeOfVender(smsBoxId, startTime, endTime, venderId, pageable);

            } else { //按mobile搜索
                rtnPage = activityDao.findSmsArriveMobileAndTimeOfvenderByMobile(smsBoxId, mobileLike, startTime, endTime, venderId, pageable);
            }

        } else { //店铺活动

            if (StringUtils.isEmpty(mobileLike)) {  //没有搜索mobile
                rtnPage = activityDao.findSmsArriveMobileAndTimeOfStoreId(smsBoxId, startTime, endTime, storeId, pageable);

            } else { //按mobile搜索
                rtnPage = activityDao.findSmsArriveMobileAndTimeOfStoreByMobile(smsBoxId, mobileLike, startTime, endTime, storeId, pageable);
            }

        }

        return rtnPage;
    }

    /**
     * 短信发送详情展示页面，根据发送状态（state：1=成功；0=正在发送；-1=失败）筛选记录。按照创建时间降序
     *
     * @param smsBoxId
     * @param state
     * @param pageNum
     * @param pageSize
     * @return Page<Object       [       ]>:[0]:mobile；[1]:arriveTime；[2]leaveTime
     */
    @Override
    public Page<Object[]> filterSmsRecordByState(Long smsBoxId, Integer state, int pageNum, int pageSize) {
        Page<Object[]> rtnPage = smsRecordService.findMobileAndErrmsgByBoxIdAndState(smsBoxId, state, pageNum, pageSize);
        return rtnPage;
    }


    /**
     * 短信发送详情展示页面，根据部分手机号码（eg：后4位）筛选记录。按照创建时间降序
     *
     * @param smsBoxId
     * @param mobile
     * @param pageNum
     * @param pageSize
     * @return Page<Object       [       ]>:[0]:mobile；[1]:arriveTime；[2]leaveTime
     */
    @Override
    public Page<Object[]> filterSmsRecordByMobile(Long smsBoxId, String mobile, int pageNum, int pageSize) {
        Page<Object[]> rtnPage = smsRecordService.findMobileAndErrmsgByBoxIdAndMobile(smsBoxId, mobile, pageNum, pageSize);
        return rtnPage;
    }

    /**
     * 短信发送详情展示页面，根据发送状态（0，1，-1）和部分手机号码（eg：后4位）筛选记录。按照创建时间降序
     *
     * @param smsBoxId
     * @param state
     * @param mobile
     * @param pageNum
     * @param pageSize
     * @return Page<Object       [       ]>:[0]:mobile;[1]:errmsg
     */
    @Override
    public Page<Object[]> filterSmsRecordByMobileAndState(Long smsBoxId, Integer state, String mobile, int pageNum, int pageSize) {
        Page<Object[]> rtnPage = smsRecordService.findMobileAndErrmsgByBoxIdAndMobileAndState(smsBoxId, state, mobile.trim(), pageNum, pageSize);
        return rtnPage;
    }

}
