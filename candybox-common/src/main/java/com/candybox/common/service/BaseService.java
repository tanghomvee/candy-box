package com.candybox.common.service;

import com.candybox.common.web.vo.Pager;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface BaseService<T  , PK extends Serializable>  {

    /**
     * @Description：将org.springframework.data.domain.Page转为Pager
     * @param page
     * @return
     */
     Pager convertPage2Pager(Page page);
}
