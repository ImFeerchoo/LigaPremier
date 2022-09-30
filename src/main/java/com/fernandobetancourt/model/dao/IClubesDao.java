package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Group;

@Repository
public interface IClubesDao extends JpaRepository<Club, Long> {
	List<Club> findByGroup(Group group);
}
