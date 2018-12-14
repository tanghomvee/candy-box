package com.slst.user.dao;

import com.slst.user.dao.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/4 12:22
 */
public interface IndustryDao extends JpaRepository<Industry,Long> {

    List<Industry> findByParentId(Long parentId);

}
