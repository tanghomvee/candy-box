package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.Contacts;
import com.slst.market.web.vo.ContactsSaveByActivityIdVO;
import org.springframework.data.domain.Page;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 14:11
 */
public interface ContactsService extends BaseService<Contacts,Long> {

    /**
     * 保存联系人列表
     * @param contacts 联系人实体
     * @return
     */
    Contacts save(Contacts contacts);




    /**
     * 创建联系人列表
     * @param curUser 当前用户
     * @param activityId 活动ID
     * @param customerIds 客户ID集合"1,2,3",逗号隔开
     * @return
     */
    Msg createContacts(UserVO curUser, Long activityId, String customerIds);

    /**
     * 修改联系人信息
     * @param curUser 当前登录用户
     * @param contacts 联系人实体
     * @return
     */
    Contacts modifyContact(UserVO curUser,Contacts contacts);

    /**
     * 根据ID获取联系人信息
     * @param id
     * @return
     */
    Contacts findById(Long id);

    /**
     * 根据活动ID分页查询
     * @param activityId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Contacts> findByActivityId(Long activityId,Integer intention,String toNum, int pageNum, int pageSize);

    /**
     * 根据活动ID和商家员工ID分页查询
     * @param activityId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Contacts> findByActivityIdAndVenderEmpId(Long activityId,Long venderEmpId,Integer intention,String toNum, int pageNum, int pageSize);

    /**
     * 拨打后备注
     * @param curUser
     * @param intention
     * @param remark
     * @param id
     * @return
     */
    Msg callRemark(UserVO curUser, Integer intention, String remark, Long id);

    /**
     * 创建联系人列表
     * @param curUser 当前用户
     * @param contactsSaveByActivityIdVO 客户分配请求对象
     * @return
     */
    Msg addContacts(UserVO curUser, ContactsSaveByActivityIdVO contactsSaveByActivityIdVO);

    /**
     * 通过活动id查询添加联系人状态
     * @param userVO 登陆信息
     * @param activityId 活动id
     * @return
     */
    Msg getSaveContactsStatus(UserVO userVO, Long activityId);

}
