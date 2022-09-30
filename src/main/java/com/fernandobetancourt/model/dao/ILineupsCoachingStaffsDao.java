package com.fernandobetancourt.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;

@Repository
public interface ILineupsCoachingStaffsDao extends JpaRepository<LineupCoachingStaff, Long> {
	
	public abstract Optional<List<LineupCoachingStaff>> findByLineup(Lineup lineup);
	
}
