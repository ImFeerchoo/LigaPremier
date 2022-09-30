package com.fernandobetancourt.model.entity;

import java.io.Serializable;

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
	
	public ClubScoreboard() {}
	
	public ClubScoreboard(String clubStatus, Scoreboard scoreboard, Club club) {
		super();
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




	private static final long serialVersionUID = 1L;

}
