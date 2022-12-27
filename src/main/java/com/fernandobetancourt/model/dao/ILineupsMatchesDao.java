package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;

@Repository
public interface ILineupsMatchesDao extends JpaRepository<LineupMatch, Long> {
	
	public abstract List<LineupMatch> findByMatch(Match match);
	
}
