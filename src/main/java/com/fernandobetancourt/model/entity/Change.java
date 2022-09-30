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
@Table(name = "changes")
public class Change implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "change_id")
	private Long changeId;
	
	private Integer minute;
	
	@ManyToOne
	@JoinColumn(name = "player_in_id", referencedColumnName = "player_id")
	private Player playerIn;
	
	@ManyToOne
	@JoinColumn(name = "player_out_id", referencedColumnName = "player_id")
	private Player playerOut;
	
	@ManyToOne
	@JoinColumn(name = "lineup_id", referencedColumnName = "lineup_id")
	private Lineup lineup;
	
	@ManyToOne
	@JoinColumn(name = "match_id", referencedColumnName = "match_id")
	private Match match;
	
	
	
	public Long getChangeId() {
		return changeId;
	}



	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}



	public Integer getMinute() {
		return minute;
	}



	public void setMinute(Integer minute) {
		this.minute = minute;
	}



	public Player getPlayerIn() {
		return playerIn;
	}



	public void setPlayerIn(Player playerIn) {
		this.playerIn = playerIn;
	}



	public Player getPlayerOut() {
		return playerOut;
	}



	public void setPlayerOut(Player playerOut) {
		this.playerOut = playerOut;
	}



	public Lineup getLineup() {
		return lineup;
	}



	public void setLineup(Lineup lineup) {
		this.lineup = lineup;
	}



	public Match getMatch() {
		return match;
	}



	public void setMatch(Match match) {
		this.match = match;
	}



	@Override
	public String toString() {
		return "Change [changeId=" + changeId + ", minute=" + minute + ", playerIn=" + playerIn + ", playerOut="
				+ playerOut + ", lineup=" + lineup + ", match=" + match + "]";
	}



	private static final long serialVersionUID = 1L;

}
