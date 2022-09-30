package com.fernandobetancourt.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;

@Repository
public interface IClubesMatchesDao extends JpaRepository<ClubMatch, Long> {

	public abstract Optional<ClubMatch> findByMatch(Match match);
	
}
