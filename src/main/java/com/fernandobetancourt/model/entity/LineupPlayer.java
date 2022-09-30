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
@Table(name = "lineups_players")
public class LineupPlayer implements Serializable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_player_id")
	private Long LineupPlayerId;
	
	@Column(name = "player_status")
	private String playerStatus;
	
//	@Column(name = "club_status")
//	private String clubStatus;
	
	@ManyToOne
	@JoinColumn(name = "player_id", referencedColumnName = "player_id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "lineup_id", referencedColumnName = "lineup_id")
	private Lineup lineup;
	
	
	
	
	
	public Long getLineupPlayerId() {
		return LineupPlayerId;
	}





	public void setLineupPlayerId(Long lineupPlayerId) {
		LineupPlayerId = lineupPlayerId;
	}





	public String getPlayerStatus() {
		return playerStatus;
	}





	public void setPlayerStatus(String playerStatus) {
		this.playerStatus = playerStatus;
	}





//	public String getClubStatus() {
//		return clubStatus;
//	}
//
//
//
//
//
//	public void setClubStatus(String clubStatus) {
//		this.clubStatus = clubStatus;
//	}





	public Player getPlayer() {
		return player;
	}





	public void setPlayer(Player player) {
		this.player = player;
	}





	public Lineup getLineup() {
		return lineup;
	}





	public void setLineup(Lineup lineup) {
		this.lineup = lineup;
	}





	private static final long serialVersionUID = 1L;
	
}
