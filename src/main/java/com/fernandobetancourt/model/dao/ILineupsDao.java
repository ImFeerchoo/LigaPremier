package com.fernandobetancourt.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Lineup;

@Repository
public interface ILineupsDao extends JpaRepository<Lineup, Long> {

}
