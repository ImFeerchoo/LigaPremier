package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.model.entity.Match;

@Repository
public interface IMatchesDao extends JpaRepository<Match, Long> {
	List<Match> findByJourney(Journey journey);
}
