package com.fernandobetancourt.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.Serie;

@Repository
public interface ISeriesDao extends JpaRepository<Serie, Long> {

}
