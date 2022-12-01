package com.fernandobetancourt.model.entity;

import java.io.Serializable;
import java.util.Objects;

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

	public Scoreboard() {
	}

	public Scoreboard(Long scoreboardId) {
		this.scoreboardId = scoreboardId;
	}

	public Long getScoreboardId() {
		return scoreboardId;
	}

	public void setScoreboardId(Long scoreboardId) {
		this.scoreboardId = scoreboardId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(scoreboardId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scoreboard other = (Scoreboard) obj;
		return Objects.equals(scoreboardId, other.scoreboardId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Scoreboard [scoreboardId=").append(scoreboardId).append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
