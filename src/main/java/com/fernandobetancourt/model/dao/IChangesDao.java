package com.fernandobetancourt.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.model.entity.Match;

@Repository
public interface IChangesDao extends JpaRepository<Change, Long> {
	
	public abstract List<Change> findByMatch(Match match);
	
}
