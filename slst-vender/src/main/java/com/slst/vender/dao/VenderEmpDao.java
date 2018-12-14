package com.slst.vender.dao;

import com.slst.vender.dao.model.VenderEmp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface VenderEmpDao extends JpaRepository<VenderEmp,Long> ,VenderDaoExt {

    /**
     * 保存商家员工信息
     * @param venderEmp
     * @return
     */
    VenderEmp save(VenderEmp venderEmp);

    /**
     * 跟新商家员工信息
     * @param venderEmp
     * @return
     */
    VenderEmp saveAndFlush(VenderEmp venderEmp);

    /**
     * 根据Id删除商家员工
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据userId查找商家员工
     * @param userId
     * @return
     */
    VenderEmp findByUserId(Long userId);

    /**
     * 查找指定storeId下的商家员工
     * @param storeId
     * @param pageable
     * @return
     */
    Page<VenderEmp> findByStoreId(Long storeId, Pageable pageable);

    /**
     * 查找指定venderId下的商家员工
     * @param venderId
     * @param pageable
     * @return
     */
    Page<VenderEmp> findByVenderId(Long venderId, Pageable pageable);

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
     * @param changeTime
     * @return
     */
    @Modifying
    @Query("update VenderEmp ve set ve.storeId=:newStoreId,ve.changer=:changer,ve.changeTime=:changeTime where ve.storeId=:storeId")
    Integer modifyStoreIdByStoreId(@Param("newStoreId") Long newStoreId,@Param("storeId") Long storeId,@Param("changer") String changer,@Param("changeTime") Date changeTime);

}
