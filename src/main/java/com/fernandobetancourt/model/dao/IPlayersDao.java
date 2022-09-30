package com.fernandobetancourt.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Player;

@Repository
public interface IPlayersDao extends JpaRepository<Player, Long> {

	public abstract Optional<Player> findByNames(String names);
	public abstract Optional<List<Player>> findByClub(Club club);
	
}
