package com.fernandobetancourt.model.entity;

import java.io.Serializable;
import java.util.Objects;

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

	public Goal() {
	}

	public Goal(Long goalId, Integer minute, Player player, Scoreboard scoreboard) {
		super();
		this.goalId = goalId;
		this.minute = minute;
		this.player = player;
		this.scoreboard = scoreboard;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(goalId, minute, player, scoreboard);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goal other = (Goal) obj;
		return Objects.equals(goalId, other.goalId) && Objects.equals(minute, other.minute)
				&& Objects.equals(player, other.player) && Objects.equals(scoreboard, other.scoreboard);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Goal [goalId=").append(goalId).append(", minute=").append(minute).append(", player=")
				.append(player).append(", scoreboard=").append(scoreboard).append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
