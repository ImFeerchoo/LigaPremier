package com.fernandobetancourt.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Scoreboard;

@Repository
public interface IScoreboardsDao extends JpaRepository<Scoreboard, Long> {

}
