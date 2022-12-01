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
@Table(name = "clubes_scoreboards")
public class ClubScoreboard implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_scoreboard_id")
	private Long clubScoreboardId;

	@Column(name = "club_status")
	private String clubStatus;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "scoreboard_id", referencedColumnName = "scoreboard_id")
	private Scoreboard scoreboard;

	@ManyToOne
	@JoinColumn(name = "club_id", referencedColumnName = "club_id")
	private Club club;

	public ClubScoreboard() {
	}

	public ClubScoreboard(String clubStatus, Scoreboard scoreboard, Club club) {
		super();
		this.clubStatus = clubStatus;
		this.scoreboard = scoreboard;
		this.club = club;
	}

	public ClubScoreboard(Long clubScoreboardId, String clubStatus, Scoreboard scoreboard, Club club) {
		super();
		this.clubScoreboardId = clubScoreboardId;
		this.clubStatus = clubStatus;
		this.scoreboard = scoreboard;
		this.club = club;
	}

	public Long getClubScoreboardId() {
		return clubScoreboardId;
	}

	public void setClubScoreboardId(Long clubScoreboardId) {
		this.clubScoreboardId = clubScoreboardId;
	}

	public String getClubStatus() {
		return clubStatus;
	}

	public void setClubStatus(String clubStatus) {
		this.clubStatus = clubStatus;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	@Override
	public int hashCode() {
		return Objects.hash(club, clubScoreboardId, clubStatus, scoreboard);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClubScoreboard other = (ClubScoreboard) obj;
		return Objects.equals(club, other.club) && Objects.equals(clubScoreboardId, other.clubScoreboardId)
				&& Objects.equals(clubStatus, other.clubStatus) && Objects.equals(scoreboard, other.scoreboard);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClubScoreboard [clubScoreboardId=").append(clubScoreboardId).append(", clubStatus=")
				.append(clubStatus).append(", scoreboard=").append(scoreboard).append(", club=").append(club)
				.append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
