package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;

@Repository
public interface ILineupsCoachingStaffsDao extends JpaRepository<LineupCoachingStaff, Long> {
	
	public abstract List<LineupCoachingStaff> findByLineup(Lineup lineup);
	
}
