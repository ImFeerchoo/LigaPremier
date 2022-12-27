package com.fernandobetancourt.model.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lineups_matches")
public class LineupMatch implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_match_id")
	private Long LineupMatchId;

	@Column(name = "club_status")
	private String clubStatus;

	@ManyToOne
	@JoinColumn(name = "match_id", referencedColumnName = "match_id")
	private Match match;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "lineup_id", referencedColumnName = "lineup_id")
	private Lineup lineup;

	public LineupMatch() {

	}

	public LineupMatch(Match match, Lineup lineup) {
		this.match = match;
		this.lineup = lineup;
	}

	public LineupMatch(String clubStatus, Match match, Lineup lineup) {
		super();
		this.clubStatus = clubStatus;
		this.match = match;
		this.lineup = lineup;
	}

	public LineupMatch(Long lineupMatchId, String clubStatus, Match match, Lineup lineup) {
		super();
		LineupMatchId = lineupMatchId;
		this.clubStatus = clubStatus;
		this.match = match;
		this.lineup = lineup;
	}

	public Long getLineupMatchId() {
		return LineupMatchId;
	}

	public void setLineupMatchId(Long lineupMatchId) {
		LineupMatchId = lineupMatchId;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public Lineup getLineup() {
		return lineup;
	}

	public void setLineup(Lineup lineup) {
		this.lineup = lineup;
	}

	public String getClubStatus() {
		return clubStatus;
	}

	public void setClubStatus(String clubStatus) {
		this.clubStatus = clubStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(LineupMatchId, clubStatus, lineup, match);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineupMatch other = (LineupMatch) obj;
		return Objects.equals(LineupMatchId, other.LineupMatchId) && Objects.equals(clubStatus, other.clubStatus)
				&& Objects.equals(lineup, other.lineup) && Objects.equals(match, other.match);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LineupMatch [LineupMatchId=").append(LineupMatchId).append(", clubStatus=").append(clubStatus)
				.append(", match=").append(match).append(", lineup=").append(lineup).append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
