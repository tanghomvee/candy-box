package com.slst.customer.dao.impl;

import com.google.common.collect.Maps;
import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.dao.JpaDaoSupport;
import com.slst.common.utils.DateUtils;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Pager;
import com.slst.customer.dao.CustomerDaoExt;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.web.vo.CustomerConditionForSmsVO;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDaoExtImpl extends JpaDaoSupport<Customer, Long> implements CustomerDaoExt {
    @Resource
    private RedisComponent redisComponent;

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public List getCustomerWithCondition(CustomerConditionForSmsVO customerCondition, Pager pager) {
        //t_store_customer
        Long storeId = customerCondition.getStoreId();
        Long venderId=customerCondition.getVenderId();
        if(null == storeId || null==venderId){    //  storeId是必须条件
            return  null;
        }

        //smsRecord
        String minSendTime = customerCondition.getMinSendTime();
        String maxSendTime = customerCondition.getMaxSendTime();
        //Customer
        List<String> mobileBrand = customerCondition.getMobileBrand();
        Integer sex = customerCondition.getSex();
        List<String> ageSlot = customerCondition.getAgeSlot();
        List<String> incomeSlot = customerCondition.getIncomeSlot();
        List<String> career = customerCondition.getCareer();
        String car = customerCondition.getCar();
        String house=customerCondition.getHouse();
        List<String> education = customerCondition.getEducation();
        List<String> children = customerCondition.getChildren();

//  SELECT * FROM t_customer c WHERE c.id in(1) AND c.mobileBrand='华为' AND c.sex=1 AND c.ageSlot='成年';
        Map<String, Object> params = Maps.newHashMap();
        StringBuffer sql = new StringBuffer("SELECT * FROM t_customer c WHERE thirdPartyId is not null ");//1=1：为了方便拼接时，不用考虑第一个“AND”的位置
        if (null != mobileBrand) {
            sql.append("AND c.mobileBrand IN(:mobileBrand");
            params.put("mobileBrand", mobileBrand);
            sql.append(") ");
        }
        if (null != sex) {
            sql.append("AND c.sex = :sex ");
            params.put("sex", sex);
        }
        if (null != ageSlot) {
            sql.append("AND c.ageSlot IN(:ageSlot");
            params.put("ageSlot", ageSlot);
            sql.append(") ");
        }
        if (null != incomeSlot) {
            sql.append("AND c.incomeSlot IN(:incomeSlot");
            params.put("incomeSlot", incomeSlot);
            sql.append(") ");
        }
        if (null != career) {
            sql.append("AND c.career IN(:career");
            params.put("career", career);
            sql.append(") ");
        }
        if (null != car) {
            sql.append("AND c.car = :car ");
            params.put("car", car);
        }
        if (null != education) {
            sql.append("AND c.education IN(:education");
            params.put("education", education);
            sql.append(") ");
        }
        if (null != children) {
            sql.append("AND c.children IN(:children");
            params.put("children", children);
            sql.append(") ");
        }

        if (null != house) {
            sql.append("AND c.house IN(:house");
            params.put("house", house);
            sql.append(") ");
        }

        Map<String, Object> rtnMap = null;

        //因为storeId必有，所以
        sql.append("AND c.id IN(");

        if (null == minSendTime && null == maxSendTime) { //不用查询smsRecord表
            rtnMap = findCustIdFromStoreCust(customerCondition, null);
        } else {
            Map<String, Object> cidFromRecord = findCustIdFromSmsRecord(customerCondition);
            rtnMap = findCustIdFromStoreCust(customerCondition, cidFromRecord);
        }
        params.putAll((Map<? extends String, ?>) rtnMap.get("paramMap"));
        sql.append(rtnMap.get("sql"));
        sql.append(")");


        try {
            List retList = super.doSQL(sql.toString(),params,Customer.class);
            return retList;
        }catch (Exception ex){
            LOGGER.error("发短信精选人群时，分页查询特定customer异常,sql={} ,params={}", sql, params, ex);
        }

        return null;
    }

    @Override
    public int listCustomerNum(CustomerConditionForSmsVO customerCondition) {
        //创建查询顾客参数
        Map<String, Object> getCustomerSqlparams = Maps.newHashMap();
        //组建sql
        StringBuffer getCustomerSql = new StringBuffer("SELECT * FROM t_customer c");
            getCustomerSql.append(" inner join (select DISTINCT customerId from t_store_customer");
            getCustomerSql.append(" where mobile is not null and mobile <> '' and yn = 1");
        //封装门店客户请求的参数
        this.storeCustomerParam(getCustomerSqlparams, getCustomerSql, customerCondition);
        //注: 此语句下的参数为t_customer的参数，语句上面的参数为t_store_customer的参数
        getCustomerSql.append(") sc on sc.customerId = c.id and c.yn = 1");
        //封装客户请求参数
        this.customerParam(getCustomerSqlparams, getCustomerSql, customerCondition);
        try {
            Integer resultNum = super.doSqlCount(getCustomerSql.toString(), getCustomerSqlparams);
            //把sql与sql参数放Redis中，方便后面的创建电话或发短信
            //1、设置key值
            String key = RedisKey.LIST_CUSTOMER_NUM+customerCondition.getActivityId();
            //2、把sql放到参数map中,方便一并存或取
            getCustomerSqlparams.put("sql", getCustomerSql.toString());

            //3、存入redis
            redisComponent.deleteByKey(key);
            redisComponent.redisSaveMap(key, getCustomerSqlparams);
            return resultNum;
        }catch (Exception ex){
            LOGGER.error("发短信精选人群时，分页查询特定customer异常,sql={} ,params={}", getCustomerSql.toString(), getCustomerSqlparams, ex);
        }
        return 0;
    }

    @Override
    public List<Customer> listCustomerBySql(String sql, Map<String, Object> mapParam) {
        try {
            return super.doSQL(sql, mapParam, Customer.class);
        }catch (Exception ex){
            LOGGER.error("根据条件查询客户异常，sql={}, params={}", sql, mapParam, ex);
        }
        return null;
    }

    @Override
    public Integer countCustomerBySql(String sql, Map<String, Object> mapParam) {
        try {
            return super.doSqlCount(sql, mapParam);
        }catch (Exception ex){
            LOGGER.error("根据条件查询客户数量异常，sql={}, params={}", sql, mapParam, ex);
        }
        return 0;
    }

    /**
     * 根据时间条件查询已发送过短信的客户Id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param venderId 商家Id
     * @param storeId 门店Id
     * @return 查询的Sql语句("querySql")及参数的参数
     */
    protected Map<String, Object> querySentSMSCustomer(String startDate, String endDate, Long venderId, Long storeId){
        Map<String, Object> querySqlParam = new HashMap<>();
        //组建查询发送过短信的客户id
        StringBuffer querySql = new StringBuffer("SELECT DISTINCT customerId FROM t_sms_record sr");
            querySql.append(" INNER JOIN t_sms_box sb ON sb.id = sr.smsBoxId WHERE sr.yn = 1 AND sb.yn = 1");
        //判断是商家或门店
        if(venderId != null){
            querySql.append(" AND sb.venderId = :venderId");
            querySqlParam.put("venderId", venderId);
        }else {
            querySql.append(" AND sb.storeId = :storeId");
            querySqlParam.put("storeId", storeId);
        }
        //时间筛选
        if(!StringUtils.isNullOrEmpty(startDate)){
            querySql.append(" AND sr.createTime >= str_to_date(:startDate, '%Y-%m-%d')");
            querySqlParam.put("startDate", startDate);
        }
        if(!StringUtils.isNullOrEmpty(endDate)){
            querySql.append(" AND sr.createTime <= str_to_date(:endDate, '%Y-%m-%d')");
            querySqlParam.put("endDate", endDate);
        }
        //组建的sql与参数一起返回
        querySqlParam.put("querySql", querySql);
        return querySqlParam;
    }

    /**
     * 门店客户参数封装
     * @param getCustomerSqlparams 要查询的客户参数
     * @param getCustomerSql 查询客户sql
     * @param customerCondition 查询条件请求参数
     */
    protected void storeCustomerParam(Map<String, Object> getCustomerSqlparams, StringBuffer getCustomerSql, CustomerConditionForSmsVO customerCondition){
        //店铺ID
        Long storeId = customerCondition.getStoreId();
        //商家ID
        Long venderId = customerCondition.getVenderId();
        //筛选到店的开始日期
        String startTime = customerCondition.getStartTime();
        //筛选到店的结束日期
        String endTime = customerCondition.getEndTime();
        //筛选到店的开始时间
        String startHour=customerCondition.getStartHour();
        //筛选到店的结束时间
        String endHour=customerCondition.getEndHour();
        //筛选驻留时间的最小值
        Integer minStayTime = customerCondition.getMinStayTime();
        //筛选驻留时间的最大值
        Integer maxStayTime = customerCondition.getMaxStayTime();
        //最小到店次数
        Integer minComeTimes = customerCondition.getMinComeTimes();
        //最大到店次数
        Integer maxComeTimes = customerCondition.getMaxComeTimes();
        //判断是店铺请求还是商家请求
        if(storeId!=null && storeId!=0L){
            getCustomerSql.append(" AND storeId = :storeId");
            getCustomerSqlparams.put("storeId", storeId);
        }else{
            getCustomerSql.append(" AND venderId = :venderId");
            getCustomerSqlparams.put("venderId", venderId);
        }
        //通过客户到店日期筛选到店客户
        if(!StringUtils.isNullOrEmpty(startTime)){
            getCustomerSql.append(" AND DATE_FORMAT(arriveTime,'%Y-%m-%d') >= :startTime");
            getCustomerSqlparams.put("startTime", startTime);
        }
        if(!StringUtils.isNullOrEmpty(endTime)){
            getCustomerSql.append(" AND DATE_FORMAT(arriveTime,'%Y-%m-%d') <= :endTime");
            getCustomerSqlparams.put("endTime", endTime);
        }else {
            //如果筛选没有传时间与日期，就填入当前时间与日期
            getCustomerSql.append(" AND arriveTime <= str_to_date(:endTime, '%Y-%m-%d %T')");
            getCustomerSqlparams.put("endTime", DateUtils.dateTime2Str(new Date()));
        }
        //通过客户到店时间筛选到店客户
        if(!StringUtils.isNullOrEmpty(startHour)){
            getCustomerSql.append(" AND extract(hour from arriveTime) >= :startHour");
            getCustomerSqlparams.put("startHour", startHour);
        }
        if(!StringUtils.isNullOrEmpty(endHour)){
            getCustomerSql.append(" AND extract(hour from arriveTime) <= :endHour");
            getCustomerSqlparams.put("endHour", endHour);
        }
        //通过停留时间:分钟 筛选到店客户
        if (minStayTime != null) {
            getCustomerSql.append(" AND s.stayTime >= :minStayTime");
            getCustomerSqlparams.put("minStayTime", minStayTime);
        }
        if (maxStayTime != null) {
            getCustomerSql.append(" AND s.stayTime <= :maxStayTime");
            getCustomerSqlparams.put("maxStayTime", maxStayTime);
        }
        //通过到店次数筛选到店客户
        if(minComeTimes != null){
            getCustomerSql.append(" group by customerId having(customerId) >= :minComeTimes");
            getCustomerSqlparams.put("minComeTimes", minComeTimes);
        }
        if(maxComeTimes != null){
            getCustomerSql.append(" group by customerId having(customerId) <= :maxComeTimes");
            getCustomerSqlparams.put("maxComeTimes", maxComeTimes);
        }
    }
    protected void customerParam(Map<String, Object> getCustomerSqlparams, StringBuffer getCustomerSql, CustomerConditionForSmsVO customerCondition){
        //已发送过短信的最小时间
        String minSendTime = customerCondition.getMinSendTime();
        //已发送过短信的最大时间
        String maxSendTime = customerCondition.getMaxSendTime();
        //手机品牌
        List<String> mobileBrand = customerCondition.getMobileBrand();
        //性别
        Integer sex = customerCondition.getSex();
        //年龄段
        List<String> ageSlot = customerCondition.getAgeSlot();
        //收入情况
        List<String> incomeSlot = customerCondition.getIncomeSlot();
        //职业
        List<String> career = customerCondition.getCareer();
        //车产情况
        String car = customerCondition.getCar();
        //有房or无房
        String house=customerCondition.getHouse();
        //教育程度
        List<String> education = customerCondition.getEducation();
        //小孩情况
        List<String> children = customerCondition.getChildren();
        //兴趣爱好
        List<String> interest = customerCondition.getInterest();

        //通过手机品牌筛选客户
        if(mobileBrand!=null && !mobileBrand.isEmpty()){
            getCustomerSql.append(" AND c.mobileBrand IN(:mobileBrand)");
            getCustomerSqlparams.put("mobileBrand", mobileBrand);
        }
        //通过性别筛选客户
        if (sex != null) {
            getCustomerSql.append(" AND c.sex = :sex ");
            getCustomerSqlparams.put("sex", sex);
        }
        //通过年龄段筛选客户
        if (ageSlot != null && !ageSlot.isEmpty()) {
            getCustomerSql.append(" AND c.ageSlot IN(:ageSlot)");
            getCustomerSqlparams.put("ageSlot", ageSlot);
        }
        //通过职业筛选客户
        if (career != null && !career.isEmpty()) {
            getCustomerSql.append(" AND c.career IN(:career)");
            getCustomerSqlparams.put("career", career);
        }
        //通过客户收入筛选客户
        if (incomeSlot != null && !incomeSlot.isEmpty()) {
            getCustomerSql.append(" AND c.incomeSlot IN(:incomeSlot)");
            getCustomerSqlparams.put("incomeSlot", incomeSlot);
        }
        //通过客户是否有车筛选客户
        if (!StringUtils.isNullOrEmpty(car)) {
            //由于参数转换
            car = "1".equals(car) ? "有" : "无";
            getCustomerSql.append(" AND c.car = :car ");
            getCustomerSqlparams.put("car", car);
        }
        //通过客户教育程度筛选客户
        if (null != education && !education.isEmpty()) {
            getCustomerSql.append(" AND c.education IN(:education)");
            getCustomerSqlparams.put("education", education);
        }
        //通过客户有无子女筛选客户
        if (null != children && !children.isEmpty()) {
            getCustomerSql.append(" AND c.children IN(:children)");
            getCustomerSqlparams.put("children", children);
        }
        //通过客户有无住房筛选客户
        if (!StringUtils.isNullOrEmpty(house)) {
            getCustomerSql.append(" AND c.house = :house");
            getCustomerSqlparams.put("house", house);
        }
        //通过客户兴趣爱好筛选客户,
        if(interest!=null && !interest.isEmpty()){
            getCustomerSql.append(" AND c.id in (SELECT customerId FROM `t_customer_interest` WHERE interest in (:interest))");
            getCustomerSqlparams.put("interest", interest);

        }
        //去除已发送过短信的客户
        if(!StringUtils.isNullOrEmpty(minSendTime) || !StringUtils.isNullOrEmpty(maxSendTime)){
            //已发过短信的客户(查询sql与参数)
            Map<String, Object> querySentSMSCustomerMap = this.querySentSMSCustomer(minSendTime, maxSendTime, customerCondition.getVenderId(), customerCondition.getStoreId());
            //获取已发过短信的客户sql
            String querySentSMSCustomerSql = querySentSMSCustomerMap.get("querySql").toString();
            querySentSMSCustomerMap.remove("querySql");
            getCustomerSql.append(" AND c.id not in (");
            getCustomerSql.append(querySentSMSCustomerSql);
            getCustomerSql.append(")");
            getCustomerSqlparams.putAll(querySentSMSCustomerMap);
        }
    }

    /**
     * 在customer_interest表 和 t_store_customer查找符合的customerId
     *
     * @return Map:Key - "sql",String；Key - "paramMap",Map<String,Object>;
     */
    private Map<String, Object> findCustIdFromStoreCust(CustomerConditionForSmsVO customerCondition, Map<String, Object> cidFromRecord) {
        StringBuffer resultSql = null;//最终返回sql
        Map<String, Object> resultMap = null;//最终返回参数map
        Map<String, Object> result = Maps.newHashMap();   //最终返回sql+参数map

        Long storeId = customerCondition.getStoreId();
        Long venderId=customerCondition.getVenderId();
        String startTime = customerCondition.getStartTime();
        String endTime = customerCondition.getEndTime();
        String startHour=customerCondition.getStartHour();
        String endHour=customerCondition.getEndHour();
        Integer minStayTime = customerCondition.getMinStayTime();
        Integer maxStayTime = customerCondition.getMaxStayTime();
        Integer minComeTimes = customerCondition.getMinComeTimes();
        Integer maxComeTimes = customerCondition.getMaxComeTimes();



        List<String> interest = customerCondition.getInterest();
        Map<String, Object> paramsMap1 = Maps.newHashMap();

        StringBuffer sql = new StringBuffer("SELECT customerId FROM t_store_customer s ");
        if (null!=storeId && 0!=storeId){
            sql.append("WHERE s.mobile is not null and s.mobile<>'' and s.storeId = ");
            sql.append(storeId);
            sql.append(" ");
        }else{
            sql.append("WHERE s.mobile is not null and s.mobile<>'' and s.venderId = ");
            sql.append(venderId);
            sql.append(" ");
        }

        if (null != startTime) {
            sql.append("AND s.arriveTime >= str_to_date(:startTime");
            paramsMap1.put("startTime", startTime);

            sql.append(",'%Y-%m-%d') ");
        }
        if (null != endTime) {
            sql.append("AND s.arriveTime <= str_to_date(:endTime");
            paramsMap1.put("endTime", endTime);

            sql.append(",'%Y-%m-%d') ");
        }

        if(null!=startHour){
            sql.append("AND extract(hour from s.arriveTime) >= :startHour ");
            paramsMap1.put("startHour", startHour);
        }

        if (null!=endHour){
            sql.append("AND extract(hour from s.arriveTime) <= :endHour ");
            paramsMap1.put("endHour", endHour);
        }

        if (null != minStayTime) {
            sql.append("AND s.stayTime >= :minStayTime");
            paramsMap1.put("minStayTime", minStayTime);

            sql.append(" ");
        }
        if (null != maxStayTime) {
            sql.append("AND s.stayTime <= :maxStayTime");
            paramsMap1.put("maxStayTime", maxStayTime);

            sql.append(" ");
        }

        if (null != cidFromRecord && !(cidFromRecord.keySet().isEmpty()) && 0 != cidFromRecord.size()) {
            String cidFromRecordSql = (String) cidFromRecord.get("sql");
            Map<String, Object> cidFromRecordParams = (Map<String, Object>) cidFromRecord.get("paramMap");
            sql.append("AND s.customerId NOT IN(");
            sql.append(cidFromRecordSql);
            paramsMap1.putAll(cidFromRecordParams);
            sql.append(") ");
        }


        sql.append("GROUP BY s.customerId ");
        if (null != minComeTimes || null != maxComeTimes) {
            sql.append("HAVING ");
            if (null != minComeTimes) {
                sql.append("COUNT(customerId) >= :minComeTimes");
                paramsMap1.put("minComeTimes", minComeTimes);
                if (null != maxComeTimes) {
                    sql.append(" AND ");
                    sql.append("COUNT(customerId) <= :maxComeTimes");
                    paramsMap1.put("maxComeTimes", maxComeTimes);
                }
            } else {
                sql.append("COUNT(customerId) <= :maxComeTimes");
                paramsMap1.put("maxComeTimes", maxComeTimes);
            }

        }
        //customer_interest表查询
        StringBuffer updateSql = new StringBuffer();
        Map<String, Object> updateParamsMap1 = Maps.newHashMap();
        if (null != interest) {
            // SELECT customerId FROM `t_customer_interest` WHERE interest in(?) AND customerId IN ();
            updateSql.append("SELECT customerId FROM `t_customer_interest` WHERE interest in(:interest");
            updateParamsMap1.put("interest", interest);
            updateSql.append(") ");

            updateSql.append("AND customerId IN (");
            updateSql.append(sql);
            updateParamsMap1.putAll(paramsMap1);
            updateSql.append(") ");

            resultSql = updateSql;
            resultMap = updateParamsMap1;
        } else {
            resultSql = sql;
            resultMap = paramsMap1;
        }

        result.put("sql", resultSql.toString());
        result.put("paramMap", resultMap);


        return result;

    }

    /**
     * 在t_sms_record查找符合的customerId和根据t_sms_box表查找smsBoxId
     *
     * @param customerCondition
     * @return Map:Key - "sql",String；Key - "paramMap",Map<String,Object>;
     */
    private Map<String, Object> findCustIdFromSmsRecord(CustomerConditionForSmsVO customerCondition) {
        StringBuffer resultSql = null;//最终返回sql
        Map<String, Object> resultMap = Maps.newHashMap();
        ;//最终返回参数map
        Map<String, Object> result = Maps.newHashMap();   //最终返回sql+参数map

        String minSendTime = customerCondition.getMinSendTime();
        String maxSendTime = customerCondition.getMaxSendTime();
        Long storeId = customerCondition.getStoreId();

        StringBuffer sql = new StringBuffer("SELECT DISTINCT customerId FROM t_sms_record r WHERE ");
        if (null != minSendTime) {
            sql.append("r.createTime >= str_to_date(:minSendTime");
            resultMap.put("minSendTime", minSendTime);

            sql.append(",'%Y-%m-%d') ");

            if (null != maxSendTime) {
                sql.append("AND r.createTime <= str_to_date(:maxSendTime");
                resultMap.put("maxSendTime", maxSendTime);

                sql.append(",'%Y-%m-%d') ");
            }

            sql.append("AND ");
        } else {
            if (null != maxSendTime) {
                sql.append("AND r.createTime <= str_to_date(:maxSendTime");
                resultMap.put("maxSendTime", maxSendTime);

                sql.append(",'%Y-%m-%d') ");
                sql.append("AND ");
            }

        }
        //AND r.smsBoxId IN(SELECT id FROM t_sms_box b WHERE storeId=1)
        sql.append("r.smsBoxId IN(SELECT id FROM t_sms_box b WHERE b.storeId = :storeId");
        resultMap.put("storeId", storeId);
        sql.append(")");

        resultSql = sql;

        result.put("sql", resultSql.toString());
        result.put("paramMap", resultMap);

        return result;

    }


//    /**
//     * 拼接 List<String> 中的数据，用于 WHERE...IN(...)的判断查询
//     * @param stringList
//     * @return
//     */
//    private StringBuffer splitJoint(List<String> stringList) {
//        StringBuffer joint = new StringBuffer();
//        for (int i = 0; i < stringList.size(); i++) {
//            if (i != 0) {
//                joint.append(",");
//            }
//            joint.append("'");
//            joint.append(stringList.get(i));
//            joint.append("'");
//        }
//        return joint;
//    }

}
