package com.fernandobetancourt.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scoreboards")
public class Scoreboard implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scoreboard_id")
	private Long scoreboardId;
	
	
	
	public Long getScoreboardId() {
		return scoreboardId;
	}



	public void setScoreboardId(Long scoreboardId) {
		this.scoreboardId = scoreboardId;
	}



	private static final long serialVersionUID = 1L;

}
