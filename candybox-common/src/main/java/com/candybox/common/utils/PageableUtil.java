package com.candybox.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    public static final String ASC_ORDER= "ASC";
    public static final String DESC_ORDER = "DESC";

    /**
     * 根据传入参数，创建排序分页器
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    public static Pageable getPageable(int pageNum, int pageSize, String sortKey, String order){
        String realSortKey = "createTime";   //默认排序关键字
        if(!(StringUtils.isNullOrEmpty(sortKey))){
            realSortKey = sortKey;
        }

        Sort sort = null;
        if("DESC".equalsIgnoreCase(order)||StringUtils.isNullOrEmpty(order)){
            sort = new Sort(Sort.Direction.DESC, realSortKey);  //降序
        }else if("ASC".equalsIgnoreCase(order)){
            sort = new Sort(Sort.Direction.ASC, realSortKey);   //升序
        }else {
            return null;
        }

        return PageRequest.of(pageNum,pageSize,sort);
    }


}
