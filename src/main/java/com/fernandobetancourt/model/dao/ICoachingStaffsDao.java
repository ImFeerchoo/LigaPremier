package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.CoachingStaff;

@Repository
public interface ICoachingStaffsDao extends JpaRepository<CoachingStaff, Long> {
	List<CoachingStaff> findByClub(Club club);
}
