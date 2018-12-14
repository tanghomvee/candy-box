package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.Activity;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ActivityService extends BaseService<Activity,Long> {

    /**
     * 新建活动
     * @param userVO
     * @param activity
     * @return
     */
    Activity createActivity(UserVO userVO, Activity activity);

    /**
     * 修改活动信息
     * @param userVO
     * @param activity
     * @return
     */
    Activity modifyActivity(UserVO userVO, Activity activity);


    Activity findActivityById(Long id);



    /**
     * 根据活动Id批量删除活动
     * @param ids
     * @return
     */
    Msg deleteActivityByIds(String ids);



    /**
     * 删除活动
     * @param id 活动ID
     * @param type 活动类型
     * @return
     */
    Msg deleteActivity(Long id,Integer type);

    /**
     * 根据userId分页查找活动
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<Activity> findActivByUserId(Long userId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据venderId分页查找活动
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<Activity> findActivByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据empId分页查找活动
     * @param empId 员工id
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<Activity> findActivByEmpId(Long empId, Integer type, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据storeId分页查找活动
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<Activity> findActivByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据venderId和activNameLike模糊分页查询
     * @param venderId
     * @param activNameLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<Activity> findActivByVenderIdAndActivName(Long venderId, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据storeId和activNameLike模糊分页查询
     * @param storeId
     * @param activNameLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<Activity> findActivByStoreIdAndActivName(Long storeId, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey);

    Page<Activity>  findByVenderIdAndTypeAndActivName(Long venderId,Integer type, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey);

    Page<Activity> findByStoreIdAndTypeAndActivName(Long storeId,Integer type, String activNameLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 活动smsBox详情页面，头部展示内容
     * @param venderId
     * @param smsBoxId
     * @return Map<String,Object>.
     * key : Title,短信名称(String);sendTime,短信发送时间(Date);sender,发件人(String);smsFee,短信单价(Double);totalAmount,短信总价(Double);smsContent,短信内容(String)
     */
    Map<String,Object> showSummaryOfSmsBox(Long venderId, Long smsBoxId);

    /**
     * 短信发送结果统计：成功，失败，正在发送（用于统计图）
     * @param smsBoxId
     * @return Map<String,Object>. key: successCount,成功条数(Long)；failedCount,失败条数(Long)；pendingCount,正在发送条数(Long)
     */
    Map<String,Object> resultOfSendingSms(Long smsBoxId);

    /**
     *  短信人群活动到店人数统计：到店，未到店（用于统计图）
     * @param venderId
     * @param smsBoxId
     * @return Map<String,Object>. key: arriveAmount,到店人数(Long)；notArriveAmount,未到店人数(Long)；totalAmount,此smsBox短信发送总人数(Long)
     */
    Map<String,Object> resultOfArrive(Long venderId,Long smsBoxId);

    /**
     * 活动smsBox详情页面，底部展示内容
     * @param venderId
     * @param smsBoxId
     * @return Map<String,Object>. key: successCount,成功条数(Long)；failedCount,失败条数(Long)；pendingCount,正在发送条数(Long)；
     * arriveAmount,到店(Long)；notArriveAmount,未到店(Long)；totalAmount,此smsBox短信发送总人数(Long)；arriveRate,到店率(Double)
     */
    Map<String,Object> summaryOfSmsAndArrive(Long venderId,Long smsBoxId);

    /**
     * (用于短信发送详情展示)。指定smsBoxId下，短信详情（电话，发送状态）展示,按照state降序（成功-正在发送-失败）
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    Page<Object[]> showDetailOfSms(Long smsBoxId,int pageNum,int pageSize);

    /**
     * (用于客户到店详情展示)。指定smsBoxId下，到店顾客详情（电话，到店时间，离店时间）展示。按照到店时间降序
     * @param venderId
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile；[1]:arriveTime；[2]leaveTime
     */
    Page<Object[]> showDetailOfArriveCust(Long venderId,Long smsBoxId,String mobileLike,int pageNum,int pageSize);

    /**
     * 短信发送详情展示页面，根据发送状态（state：1=成功；0=正在发送；-1=失败）筛选记录。按照创建时间降序
     * @param smsBoxId
     * @param state
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile；[1]:arriveTime；[2]leaveTime
     */
    Page<Object[]> filterSmsRecordByState(Long smsBoxId,Integer state,int pageNum,int pageSize);

    /**
     * 短信发送详情展示页面，根据部分手机号码（eg：后4位）筛选记录。按照创建时间降序
     * @param smsBoxId
     * @param mobile
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile；[1]:arriveTime；[2]leaveTime
     */
    Page<Object[]> filterSmsRecordByMobile(Long smsBoxId,String mobile,int pageNum,int pageSize);

    /**
     * 短信发送详情展示页面，根据发送状态（0，1，-1）和部分手机号码（eg：后4位）筛选记录。按照创建时间降序
     * @param smsBoxId
     * @param state
     * @param mobile
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    Page<Object[]> filterSmsRecordByMobileAndState(Long smsBoxId,Integer state,String mobile,int pageNum,int pageSize);

}
