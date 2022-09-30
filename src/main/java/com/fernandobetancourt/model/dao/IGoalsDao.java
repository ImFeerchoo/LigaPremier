package com.fernandobetancourt.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.model.entity.Scoreboard;

@Repository
public interface IGoalsDao extends JpaRepository<Goal, Long> {
	
	public abstract Optional<List<Goal>> findByScoreboard(Scoreboard scoreboard);

}
