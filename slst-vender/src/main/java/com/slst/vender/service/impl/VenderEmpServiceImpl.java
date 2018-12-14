package com.slst.vender.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.web.vo.Msg;
import com.slst.vender.dao.VenderEmpDao;
import com.slst.vender.dao.model.VenderEmp;
import com.slst.vender.service.VenderEmpService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@Service("venderEmpService")
public class VenderEmpServiceImpl extends BaseServiceImpl<VenderEmp, Long> implements VenderEmpService {

    @Resource
    private VenderEmpDao venderEmpDao;

    /**
     * 创建商家员工信息
     * @param venderEmp
     * @return
     */
    @Override
    public VenderEmp createVenderEmp(VenderEmp venderEmp) {
        return venderEmpDao.save(venderEmp);
    }

    /**
     * 修改商家员工信息
     * @param venderEmp
     * @return
     */
    @Override
    public VenderEmp updateVenderEmp(VenderEmp venderEmp) {
        return venderEmpDao.saveAndFlush(venderEmp);
    }

    /**
     * 根据id删除商家员工
     * @param id
     */
    @Override
    public Msg deleteVenderEmpById(Long id) {
        try {
            venderEmpDao.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("删除venderEmp表数据出错！");
        }
        return Msg.success();
    }

    /**
     * 根据id查找商家员工
     * @param id
     * @return
     */
    @Override
    public VenderEmp findVenderEmpById(Long id) {
        Optional<VenderEmp> optional=venderEmpDao.findById(id);
//        return venderEmpDao.findById(id).get();

        return null!=optional && optional.isPresent() ? optional.get():null;
    }

    /**
     * 根据userId查找商家员工
     * @param userId
     * @return
     */
    @Override
    public VenderEmp findVenderEmpByUserId(Long userId) {
        return venderEmpDao.findByUserId(userId);
    }

    /**
     * 查找指定storeId 下的商家员工
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<VenderEmp> findVenderEmpByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String order) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);
        return venderEmpDao.findByStoreId(storeId,pageable);
    }

    /**
     * 查找指定venderId下的商家员工
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<VenderEmp> findVenderEmpByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String order) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);
        return venderEmpDao.findByVenderId(venderId,pageable);
    }

    @Override
    public Long countByVenderId(Long venderId) {
        return venderEmpDao.countByVenderId(venderId);
    }

    @Override
    public Long countByStoreId(Long storeId) {
        return venderEmpDao.countByStoreId(storeId);
    }

    @Transactional
    @Override
    public Integer modifyStoreIdByStoreId(String changer, Long newStoreId, Long storeId) {

        return venderEmpDao.modifyStoreIdByStoreId(newStoreId,storeId,changer,new Date());
    }
}
