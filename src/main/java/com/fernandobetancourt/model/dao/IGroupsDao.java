package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Serie;

@Repository
public interface IGroupsDao extends JpaRepository<Group, Long> {
	List<Group> findBySerie(Serie serie);
}
