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

	public Change() {
	}

	public Change(Long changeId, Integer minute, Player playerIn, Player playerOut, Lineup lineup, Match match) {
		super();
		this.changeId = changeId;
		this.minute = minute;
		this.playerIn = playerIn;
		this.playerOut = playerOut;
		this.lineup = lineup;
		this.match = match;
	}

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
	public int hashCode() {
		return Objects.hash(changeId, lineup, match, minute, playerIn, playerOut);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Change other = (Change) obj;
		return Objects.equals(changeId, other.changeId) && Objects.equals(lineup, other.lineup)
				&& Objects.equals(match, other.match) && Objects.equals(minute, other.minute)
				&& Objects.equals(playerIn, other.playerIn) && Objects.equals(playerOut, other.playerOut);
	}

	@Override
	public String toString() {
		return "Change [changeId=" + changeId + ", minute=" + minute + ", playerIn=" + playerIn + ", playerOut="
				+ playerOut + ", lineup=" + lineup + ", match=" + match + "]";
	}

	private static final long serialVersionUID = 1L;

}
