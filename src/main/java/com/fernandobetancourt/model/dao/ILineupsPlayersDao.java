package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupPlayer;

@Repository
public interface ILineupsPlayersDao extends JpaRepository<LineupPlayer, Long> {

	public abstract List<LineupPlayer> findByLineup(Lineup lineup);
	
}
