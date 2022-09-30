package com.fernandobetancourt.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Scoreboard;

@Repository
public interface IClubesScoreboardsDao extends JpaRepository<ClubScoreboard, Long> {

	public abstract Optional<ClubScoreboard> findByScoreboardAndClubStatus(Scoreboard scoreboard, String clubStatus);
	
}
