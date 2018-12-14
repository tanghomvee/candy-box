package com.slst.market.dao;

import com.slst.market.dao.model.ActivityVenderEmpRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityVenderEmpRelationDao extends JpaRepository<ActivityVenderEmpRelation, Long>, ActivityVenderEmpRelationDaoExt{
}
