package com.slst.vender.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.vender.dao.model.VenderEmp;
import org.springframework.data.domain.Page;

public interface VenderEmpService extends BaseService<VenderEmp, Long> {

    /**
     * 创建商家员工信息
     * @param venderEmp
     * @return
     */
    VenderEmp createVenderEmp(VenderEmp venderEmp);

    /**
     * 修改商家员工信息
     * @param venderEmp
     * @return
     */
    VenderEmp updateVenderEmp(VenderEmp venderEmp);

    /**
     * 根据id删除商家员工
     * @param id
     */
    Msg deleteVenderEmpById(Long id);

    /**
     * 根据id查找商家员工
     * @param id
     * @return
     */
    VenderEmp findVenderEmpById(Long id);

    /**
     * 根据userId查找商家员工
     * @param userId
     * @return
     */
    VenderEmp findVenderEmpByUserId(Long userId);

    /**
     * 查找指定storeId 下的商家员工
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<VenderEmp> findVenderEmpByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String order);

    /**
     * 查找指定venderId下的商家员工
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<VenderEmp> findVenderEmpByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String order);


    /**
     * 获取商家员工数量
     * @param venderId 商家ID
     * @return 数量
     */
    Long countByVenderId(Long venderId);

    /**
     * 获取门店员工数量
     * @param storeId 门店ID
     * @return 数量
     */
    Long countByStoreId(Long storeId);

    /**
     * 将员工分配给新的门店
     * @param newStoreId
     * @param storeId
     * @param changer
     * @return
     */
    Integer modifyStoreIdByStoreId(String changer,Long newStoreId,Long storeId);
}
