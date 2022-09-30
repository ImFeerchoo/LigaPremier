package com.fernandobetancourt.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lineups")
public class Lineup implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_id")
	private Long lineupId;
	
	
	
	
	public Long getLineupId() {
		return lineupId;
	}




	public void setLineupId(Long lineupId) {
		this.lineupId = lineupId;
	}




	private static final long serialVersionUID = 1L;

}
