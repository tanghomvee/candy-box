package com.slst.market.service.impl;

import com.google.common.collect.Maps;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SoundToothService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.SmsTplDao;
import com.slst.market.dao.model.SmsTpl;
import com.slst.market.service.SmsTplService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-23 14:29
 */
@Service("smsTplService")
public class SmsTplServiceImpl extends BaseServiceImpl<SmsTpl, Long> implements SmsTplService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private
    SoundToothService soundToothService;

    @Resource
    private SmsTplDao smsTplDao;



    /**
     * 新建短信模板
     *
     * @param curUser
     * @param smsTpl
     * @return
     */
    @Override
    public SmsTpl createSmsTpl(UserVO curUser, SmsTpl smsTpl) {
        String creator = curUser.getUserName();
        Date curDate = new Date();//当前时间
        Integer yes = YNEnum.YES.getVal();

        String content=smsTpl.getContent();

        if(StringUtils.isNullOrEmpty(content)){
            LOGGER.error("前端提交短信模板时，内容为空，Title={},Sign={},venderId={}",smsTpl.getTitle(),smsTpl.getSignname(),smsTpl.getVenderId());
            return null;
        }

//        content=content.substring(content.lastIndexOf("】")+1);
        content=content.substring(content.lastIndexOf("】")+1,content.lastIndexOf("退订回T"));


        Map<String, Object> template = Maps.newHashMap();
        template.put("content", content);
        template.put("title", smsTpl.getTitle());
        template.put("sign", smsTpl.getSignname());
        Map<String, Object> rtnMapByThird = submitSmsTplAndfindStateFromThirdParty(smsTpl.getTitle(),content,smsTpl.getSignname());//向第三方提交，并返回thirdPartyId，state，errmsg
        String thirdPartyId = (String) rtnMapByThird.get("thirdPartyId");
        Integer state = (Integer) rtnMapByThird.get("state");
        String errmsg = (String) rtnMapByThird.get("errmsg");

        smsTpl.setThirdPartyId(thirdPartyId);//第三方短信模板ID
        smsTpl.setState(state); //审核状态
        smsTpl.setErrmsg(errmsg);   //审核状态返回信息
        smsTpl.setCreator(creator);
        smsTpl.setCreateTime(curDate);
        smsTpl.setYn(yes);
        smsTpl.setUserId(curUser.getId());
        if (null!=curUser.getStoreId() && curUser.getStoreId()>0L){
            smsTpl.setStoreName(curUser.getStoreName());
            smsTpl.setVenderId(curUser.getVenderId());
            smsTpl.setVenderName(curUser.getVenderName());
            smsTpl.setStoreId(curUser.getStoreId());
        }else{
            smsTpl.setStoreName("");
            smsTpl.setVenderId(curUser.getVenderId());
            smsTpl.setVenderName(curUser.getDisplayName());
            smsTpl.setStoreId(0L);
        }

//        else{
//            smsTpl.setStoreName("");
//            smsTpl.setVenderId(0L);
//            smsTpl.setVenderName("");
//            smsTpl.setStoreId(0L);
//
//        }

        //存入数据库
        SmsTpl rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.save(smsTpl);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，创建短信模板失败！" + e);
            return null;
        }

        return rtnSmsTpl;
    }

    /**
     * 修改短信模板
     *
     * @param curUser
     * @param smsTpl
     * @return
     */
    @Override
    public SmsTpl modifySmsTpl(UserVO curUser, SmsTpl smsTpl) {
        String changer = curUser.getUserName();
        Date curDate = new Date();//当前时间
        Long smsTplId = smsTpl.getId();//需要修改的模板Id
        String content = smsTpl.getContent();
        String title = smsTpl.getTitle();
        String sign = smsTpl.getSignname();

        SmsTpl findSmsTpl = null;
        try {
            findSmsTpl = smsTplDao.findById(smsTplId).get();//取得原本的短信模板
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，查找短信模板失败！" + e);
            return null;
        }

        //如果没有做任何修改
        if(null != findSmsTpl && findSmsTpl.getContent().equals(content) && findSmsTpl.getTitle().equals(title) && findSmsTpl.getSignname().equals(sign)){
            return findSmsTpl;
        }

        Map<String, Object> rtnMapByThird = submitSmsTplAndfindStateFromThirdParty(title,content,sign);//向第三方提交，并返回thirdPartyId，state，errmsg
//        thirdPartyId,state,errmsg
        String thirdPartyId = (String) rtnMapByThird.get("thirdPartyId");
        Integer state = (Integer) rtnMapByThird.get("state");
        String errmsg = (String) rtnMapByThird.get("errmsg");

        //更新短信模板信息
        findSmsTpl.setThirdPartyId(thirdPartyId);
        findSmsTpl.setState(state);
        findSmsTpl.setErrmsg(errmsg);
        //可修改的内容：title;signname;content;
        findSmsTpl.setTitle(title);
        findSmsTpl.setSignname(sign);
        findSmsTpl.setContent(content);

        findSmsTpl.setChanger(changer);
        findSmsTpl.setChangeTime(curDate);

        SmsTpl rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.saveAndFlush(findSmsTpl);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("数据库操作异常，更新短信模板失败！" + ex);
            return null;
        }

        return rtnSmsTpl;
    }

    /**
     * 根据Id，批量删除短信模板
     *
     * @param ids
     * @return
     */
    @Override
    public Msg deleteSmsTpl(String ids) {
        if (StringUtils.isNullOrEmpty(ids)) {
            return Msg.error("未选择要删除的模板。");
        }

        if (ids.contains(",")) {
            String[] idsArr = ids.split(",");

            for (int i = 0; i < idsArr.length; i++) {
                try {
                    smsTplDao.deleteById(Long.parseLong(idsArr[i].trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("删除短信模板，ID："+idsArr[i]+"，数据库操作异常" + e);
                    return Msg.error("短信模板删除出错，请重试。");
                }
            }
        } else {

            try {
                smsTplDao.deleteById(Long.parseLong(ids.trim()));
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("删除短信模板，数据库操作异常" + ex);
                return Msg.error("短信模板删除出错，请重试。");
            }
        }

        return Msg.success();
    }


    /**
     * 查看短信模板审核状态，返回 Map<String,Object>:"state"-审核状态（Integer） & "errmsg"-返回信息（String）
     *
     * @param thirdPartyId
     * @return Map<String   ,   Object>:"state"-审核状态（Integer） & "errmsg"-返回信息（String）
     */
    @Override
    public Map<String, Object> findSmsTemplateStatus(String thirdPartyId) {
        Map<String, Object> stateMap = Maps.newHashMap();
        Integer state = null;
        String errmsg = null;
        Msg rtnMsg = soundToothService.findSMSTemplateAuthStatus(thirdPartyId);//请求审核状态

        if (null == rtnMsg || Msg.error().getMsg().equals(rtnMsg.getMsg())) { //请求审核状态失败
            state = -1;
            errmsg = "查看短信模板审核状态请求失败。";

        } else if (rtnMsg.getMsg().contains("审核通过")) {
            state = 1;//审核通过：1
            errmsg = rtnMsg.getMsg();

        } else if (rtnMsg.getMsg().contains("等待审核")) { //需保证rtnMsg.getMsg()编码格式为“utf-8”
            state = 0;//审核中：0
            errmsg = rtnMsg.getMsg();

        } else {//审核失败
            state = -1;
            errmsg = rtnMsg.getMsg();
//                errmsg = "短信模板审核未通过。";
        }

        stateMap.put("state", state);
        stateMap.put("errmsg", errmsg);

        return stateMap;
    }

    /**
     * 根据ID查找短信模板
     * @param id
     * @return
     */
    public SmsTpl findSmsTplById(Long id){
        Optional<SmsTpl> resultSmsTpl = smsTplDao.findById(id);
        return resultSmsTpl.isPresent() ? resultSmsTpl.get() : null;
    }

    /**
     * 根据userId分页查找短信模板
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsTpl> findSmsTplByUserId(Long userId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        Page<SmsTpl> rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.findByUserId(userId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找userId：" + userId + " 下的短信模板，数据库操作异常。" + e);
            return null;
        }

        return updateStateWhenListShow(rtnSmsTpl);
    }

    /**
     * 根据venderId分页查找短信模板
     *
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsTpl> findSmsTplByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        Page<SmsTpl> rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.findByVenderId(venderId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找venderId：" + venderId + " 下短信模板，数据库操作异常。" + e);
            return null;
        }
        return updateStateWhenListShow(rtnSmsTpl);
    }

    /**
     * 根据storeId分页查找短信模板
     *
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsTpl> findSmsTplByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        Page<SmsTpl> rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.findByStoreId(storeId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找storeId：" + storeId + " 下的短信模板，数据库操作异常。" + e);
            return null;
        }
        return updateStateWhenListShow(rtnSmsTpl);
    }

    /**
     * 根据venderId和titleLike模糊分页查询
     *
     * @param venderId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey   排序关键字。默认createTime
     * @param orderKey  升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsTpl> findSmsTplByVenderIdAndTitle(Long venderId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        Page<SmsTpl> rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.findByVenderIdAndTitleContaining(venderId, titleLike, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找venderId：" + venderId + " 下，Title包含的:" + titleLike + " 的短信模板，数据库操作异常。" + e);
            return null;
        }
        return updateStateWhenListShow(rtnSmsTpl);
    }

    /**
     * 根据storeId和titleLike模糊分页查询
     *
     * @param storeId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey   排序关键字。默认createTime
     * @param orderKey  升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsTpl> findSmsTplByStoreIdAndTitle(Long storeId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        Page<SmsTpl> rtnSmsTpl = null;
        try {
            rtnSmsTpl = smsTplDao.findByStoreIdAndTitleContaining(storeId, titleLike, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找storeId：" + storeId + " 下，Title包含的:" + titleLike + " 的短信模板，数据库操作异常。" + e);
            return null;
        }
        return updateStateWhenListShow(rtnSmsTpl);
    }

    /**
     * 根据标题和状态查看短信模板
     * @param venderId
     * @param storeId
     * @param state
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsTpl> findSmsTplByStateAndTitle(Long venderId, Long storeId, Integer state, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey) {

        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);
        if (null!=storeId && 0!=storeId){
            return findSmsTplByStateAndTitleToStore(storeId,state,titleLike,pageable);
        }

        return findSmsTplByStateAndTitleToVender(venderId,state,titleLike,pageable);
    }

    private Page<SmsTpl> findSmsTplByStateAndTitleToVender(Long venderId, Integer state, String titleLike, Pageable pageable){

        if (StringUtils.isNullOrEmpty(titleLike)){
            return smsTplDao.findByVenderIdAndState(venderId,state,pageable);
        }

        return smsTplDao.findByVenderIdAndStateAndTitleContaining(venderId,state,titleLike,pageable);
    }

    private Page<SmsTpl> findSmsTplByStateAndTitleToStore(Long storeId, Integer state, String titleLike, Pageable pageable){

        if (StringUtils.isNullOrEmpty(titleLike)){
            return smsTplDao.findByStoreIdAndState(storeId,state,pageable);
        }

        return smsTplDao.findByStoreIdAndStateAndTitleContaining(storeId,state,titleLike,pageable);
    }


    /**
     * 提交短信模板到第三方，返回thirdPartyId，state,errmsg
     *
     * @param title
     * @param content
     * @param sign
     * @return
     */
    private Map<String, Object> submitSmsTplAndfindStateFromThirdParty(String title,String content,String sign) {
        Map<String, Object> map = Maps.newHashMap();
        String thirdPartyId = null;
        Integer state = null;
        String errmsg = null;

        String rtnThirdPartyId = soundToothService.createSMSTemplate(title,content,sign).getData().toString();//获取第三方短信模板ID



        if (StringUtils.isNullOrEmpty(rtnThirdPartyId) ) { //第三方创建模板请求失败
            thirdPartyId = "-1";
            state = -1;
            errmsg = "创建短信模板请求失败。";

        } else {
            thirdPartyId = rtnThirdPartyId;
            Map<String, Object> rtnMap = findSmsTemplateStatus(rtnThirdPartyId);//请求审核状态
            state = (Integer) rtnMap.get("state");
            errmsg = (String) rtnMap.get("errmsg");
        }

        map.put("thirdPartyId", thirdPartyId);
        map.put("state", state);
        map.put("errmsg", errmsg);

        return map;
    }

    /**
     * 页面展示时，更新短信模板审核状态
     * @param rtnSmsTpl
     * @return
     */
    private Page<SmsTpl> updateStateWhenListShow(Page<SmsTpl> rtnSmsTpl) {

        if(null == rtnSmsTpl || rtnSmsTpl.getContent().size() == 0){  //如果没有匹配的内容
            return null;
        }

        Integer count = 0;  //状态更新条数
        List<SmsTpl> smsTplList = rtnSmsTpl.getContent();
        for(int i = 0;i<smsTplList.size();i++) {

            SmsTpl oldSmsTpl = smsTplList.get(i);
            Map<String, Object> stateMap = findSmsTemplateStatus(oldSmsTpl.getThirdPartyId());//查找最新的审核状态
            Integer oldState = oldSmsTpl.getState();
            String oldErrMsg = oldSmsTpl.getErrmsg();
            Integer newState = (Integer) stateMap.get("state");
            String newErrMsg = (String) stateMap.get("errmsg");

            if (oldState == 0 && !(oldState.equals(newState) && oldErrMsg.equals(newErrMsg))) { //只有待审核的，以及当最新审核状态与数据库不同时才需要更新
                oldSmsTpl.setState(newState);    //更新返回状态数据
                oldSmsTpl.setErrmsg(newErrMsg);

                try {
                    SmsTpl resultSmsTpl = smsTplDao.saveAndFlush(oldSmsTpl);  //保存到数据库
                    count++;
                }catch (Exception e){
                    oldSmsTpl.setState(oldState);    //数据库保存失败，将状态修改回状态
                    oldSmsTpl.setErrmsg(oldErrMsg);
                    e.printStackTrace();
                    LOGGER.error("更新短信模板审核状态，数据库操作异常。"+e);
                }
            }

        }

        LOGGER.info("展示短信模板页面时，更新的审核状态条数："+count);
        return rtnSmsTpl;
    }

}
