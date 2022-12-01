package com.fernandobetancourt.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "matches")
public class Match implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "match_id")
	private Long matchId;

	private String stadium;

	private String referee;

	private LocalDateTime date;

	@ManyToOne()
	@JoinColumn(name = "journey_id", referencedColumnName = "journey_id")
	private Journey journey;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "scoreboard_id", referencedColumnName = "scoreboard_id")
	private Scoreboard scoreboard;

	public Match() {
	}

	public Match(String stadium, String referee, LocalDateTime date, Journey journey, Scoreboard scoreboard) {
		super();
		this.stadium = stadium;
		this.referee = referee;
		this.date = date;
		this.journey = journey;
		this.scoreboard = scoreboard;
	}

	public Match(Long matchId, String stadium, String referee, LocalDateTime date, Journey journey,
			Scoreboard scoreboard) {
		super();
		this.matchId = matchId;
		this.stadium = stadium;
		this.referee = referee;
		this.date = date;
		this.journey = journey;
		this.scoreboard = scoreboard;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public String getStadium() {
		return stadium;
	}

	public void setStadium(String stadium) {
		this.stadium = stadium;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		this.journey = journey;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, journey, matchId, referee, scoreboard, stadium);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		return Objects.equals(date, other.date) && Objects.equals(journey, other.journey)
				&& Objects.equals(matchId, other.matchId) && Objects.equals(referee, other.referee)
				&& Objects.equals(scoreboard, other.scoreboard) && Objects.equals(stadium, other.stadium);
	}

	@Override
	public String toString() {
		return "Match [matchId=" + matchId + ", stadium=" + stadium + ", referee=" + referee + ", date=" + date
				+ ", journey=" + journey + ", scoreboard=" + scoreboard + "]";
	}

	private static final long serialVersionUID = 1L;

}
