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

	public LineupPlayer() {
	}

	public LineupPlayer(Long lineupPlayerId, String playerStatus, Player player, Lineup lineup) {
		super();
		LineupPlayerId = lineupPlayerId;
		this.playerStatus = playerStatus;
		this.player = player;
		this.lineup = lineup;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(LineupPlayerId, lineup, player, playerStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineupPlayer other = (LineupPlayer) obj;
		return Objects.equals(LineupPlayerId, other.LineupPlayerId) && Objects.equals(lineup, other.lineup)
				&& Objects.equals(player, other.player) && Objects.equals(playerStatus, other.playerStatus);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LineupPlayer [LineupPlayerId=").append(LineupPlayerId).append(", playerStatus=")
				.append(playerStatus).append(", player=").append(player).append(", lineup=").append(lineup).append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
