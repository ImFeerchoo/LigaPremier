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
@Table(name = "lineups")
public class Lineup implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_id")
	private Long lineupId;

	public Lineup() {
	}

	public Lineup(Long lineupId) {
		this.lineupId = lineupId;
	}

	public Long getLineupId() {
		return lineupId;
	}

	public void setLineupId(Long lineupId) {
		this.lineupId = lineupId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lineupId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lineup other = (Lineup) obj;
		return Objects.equals(lineupId, other.lineupId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Lineup [lineupId=").append(lineupId).append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
