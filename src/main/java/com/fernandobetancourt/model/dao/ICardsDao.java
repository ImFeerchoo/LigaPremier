package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.model.entity.Match;

@Repository
public interface ICardsDao extends JpaRepository<Card, Long> {
	
	public abstract List<Card> findByMatch(Match match);
	
}
