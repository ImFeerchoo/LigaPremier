package com.fernandobetancourt.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Journey;

@Repository
public interface IJourneysDao extends JpaRepository<Journey, Long> {

}
