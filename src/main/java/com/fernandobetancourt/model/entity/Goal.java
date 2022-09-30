package com.fernandobetancourt.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "goals")
public class Goal implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goal_id")
	private Long goalId;
	
	private Integer minute;
	
	@ManyToOne
	@JoinColumn(name = "player_id", referencedColumnName = "player_id")
	private Player player;
	
//	@ManyToOne
//	@JoinColumn(name = "match_id", referencedColumnName = "match_id")
//	private Match match;

	@ManyToOne
	@JoinColumn(name = "scoreboard_id", referencedColumnName = "scoreboard_id")
	private Scoreboard scoreboard;
	
	
	
	public Long getGoalId() {
		return goalId;
	}



	public void setGoalId(Long goalId) {
		this.goalId = goalId;
	}



	public Integer getMinute() {
		return minute;
	}



	public void setMinute(Integer minute) {
		this.minute = minute;
	}



	public Player getPlayer() {
		return player;
	}



	public void setPlayer(Player player) {
		this.player = player;
	}



//	public Match getMatch() {
//		return match;
//	}
//
//
//
//	public void setMatch(Match match) {
//		this.match = match;
//	}



	public Scoreboard getScoreboard() {
		return scoreboard;
	}



	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}



	private static final long serialVersionUID = 1L;

}
