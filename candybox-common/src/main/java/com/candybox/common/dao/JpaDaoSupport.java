package com.candybox.common.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.candybox.common.web.vo.Pager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 提供基础扩展功能
 * @author ddyunf
 */
public class JpaDaoSupport<T extends Serializable, ID extends Serializable>{

    protected static Logger LOGGER = null;


    public JpaDaoSupport() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }

    @PersistenceContext
    protected EntityManager entityManager;

    public Pager doSQLPage(String sql, Map<String, Object> params, Class<?> domainClazz, Integer pageNum, Integer pageSize) throws Exception {
        if(StringUtils.isEmpty(sql) || pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1){

            throw new Exception("错误的SQL语句或者错误的分页参数");
        }

        String cntSQL = "select count(1) from ("+ sql +") tmp";
        Query countQuery = entityManager.createNativeQuery(cntSQL);
        Set<String> paramNames = Sets.newHashSet();

        if(params != null && params.size() > 0){
            paramNames = params.keySet();
        }
        for (String paramName : paramNames){
            countQuery.setParameter(paramName , params.get(paramName));
        }

        BigInteger records = (BigInteger) countQuery.getSingleResult();
        if(records == null || records.intValue() < 1){
            return new Pager(0 , null);
        }

        //2.分页查询数据
        int startIndex = (pageNum - 1) * pageSize;
        String pageSQL = sql + " limit " + startIndex + ", " + pageSize ;

        Query query = createLocalQuery(domainClazz , pageSQL);

        for (String paramName : paramNames){
            query.setParameter(paramName , params.get(paramName));
        }
        List data = query.getResultList();
        Pager retPager = new Pager(records.intValue() , data , pageSize);
        retPager.setPageNum(pageNum);
        return retPager;
    }

    /**
     * 非分页查询
     *
     * @param sql
     * @param params
     * @param domainClazz
     * @return
     * @throws Exception
     */
    public List<T> doSQL(String sql, Map<String, Object> params, Class<?> domainClazz) throws Exception {
        if(StringUtils.isEmpty(sql)){
            throw new Exception("错误的SQL语句");
        }

        Set<String> paramNames = Sets.newHashSet();

        if(params != null && params.size() > 0){
            paramNames = params.keySet();
        }

        Query query = createLocalQuery(domainClazz , sql);

        for (String paramName : paramNames){
            query.setParameter(paramName , params.get(paramName));
        }
        List data = query.getResultList();
        return data;
    }

    /**
     * 通过sql与参数查询数据条数
     * @param sql sql 语句
     * @param params 查询参数
     * @return 返回查询数据的条数
     * @throws Exception
     */
    public Integer doSqlCount(String sql, Map<String, Object> params) throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("错误的SQL语句");
        }
        String countSql = "select count(1) from ("+ sql +") tmp";
        Query countQuery = entityManager.createNativeQuery(countSql);
        for(Map.Entry<String, Object> paramMap : params.entrySet()){
            countQuery.setParameter(paramMap.getKey(), paramMap.getValue());
        }
        BigInteger resultNum = (BigInteger) countQuery.getSingleResult();
        return resultNum == null ? 0 : resultNum.intValue();
    }

    public Pager doSQLPage(String sql, Map<String, Object> params, Integer pageNum, Integer pageSize) throws Exception {
        return this.doSQLPage(sql ,params , null ,pageNum ,pageSize);
    }


    public Pager doSQLPage(String sql, Class<?> domainClazz, Integer pageNum, Integer pageSize) throws Exception {
        return this.doSQLPage(sql , (List<Object>) null ,domainClazz ,pageNum ,pageSize);
    }

    public Pager doSQLPage(String sql, Integer pageNum, Integer pageSize) throws Exception {
        return this.doSQLPage(sql ,(Map<String, Object>) null , pageNum ,pageSize);
    }


    public Pager doSQLPage(String sql, List<Object> params, Class<?> domainClazz, Integer pageNum, Integer pageSize) throws Exception {
        if(StringUtils.isEmpty(sql) || pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1){

            throw new Exception("错误的SQL语句或者错误的分页参数");
        }

        //1.统计总记录数
        //        int startIndex = sql.toLowerCase().indexOf(" from ");
//        if(startIndex == -1){
//            throw new Exception("错误的SQL语句缺少from关键字");
//        }
//
//        String fromSQL = sql.substring( startIndex);
//        String cntSQL = "select count(1) " + fromSQL;

        String cntSQL = "select count(1) from ("+ sql +") tmp";
        Query countQuery = entityManager.createNativeQuery(cntSQL);

        if(CollectionUtils.isEmpty( params)){
            params = Lists.newArrayList();
        }
        for (int i = 0 , len = params.size(); i< len ; i++){
            countQuery.setParameter(i + 1 , params.get(i));
        }

        BigInteger records = (BigInteger) countQuery.getSingleResult();
        if(records == null || records.intValue() < 1){
            return new Pager(0 , null);
        }

        //2.分页查询数据
        int startIndex = (pageNum - 1) * pageSize;
        String pageSQL = sql + " limit " + startIndex + ", " + pageSize ;

        Query query = createLocalQuery(domainClazz , pageSQL);

        for (int i = 0 , len = params.size(); i< len ; i++){
            query.setParameter(i + 1 , params.get(i));
        }

        List data = query.getResultList();
        Pager retPager = new Pager(records.intValue() , data , pageSize);
        retPager.setPageNum(pageNum);
        return retPager;
    }

    public Pager doSQLPage(String sql, List<Object> params, Integer pageNum, Integer pageSize) throws Exception {
        return this.doSQLPage(sql ,params ,null ,pageNum ,pageSize);
    }

    protected Query createLocalQuery(Class<?> domainClazz , String pageSQL){
        Query query = null;

        if(domainClazz != null){
            if(domainClazz.getAnnotation(Entity.class) != null){
                query = entityManager.createNativeQuery( pageSQL , domainClazz);
            }else{
                query = entityManager.createNativeQuery( pageSQL);
                if(HashMap.class.equals(domainClazz)){
                    //考虑到对性能的影响,建议别将结果集转为Map
                    /**
                     * vlad(Hibernate Team member)
                     * setResultTransformer
                     * For the moment, there is no replacement.
                     * In 6.0, the ResultTransformer will be replaced by a @FunctionalInterface, but the underlying mechanism of transforming the Object[] property values will probably be the same.
                     * Therefore, you can use it as-is and, only if you upgrade to 6.0, you will have to think about replacing it with the new variant.
                     */
                    query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                }else{
                    query.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(domainClazz));
                }
            }
        }else {
            query = entityManager.createNativeQuery( pageSQL);
        }
        return  query;
    }

}
