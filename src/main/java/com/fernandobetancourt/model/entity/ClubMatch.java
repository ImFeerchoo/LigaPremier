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
@Table(name = "clubes_matches")
public class ClubMatch implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_match_id")
	private Long clubMatchId;

	@ManyToOne
	@JoinColumn(name = "local_club_id", referencedColumnName = "club_id")
	private Club localClub;
	
	@ManyToOne
	@JoinColumn(name = "visitor_club_id", referencedColumnName = "club_id")
	private Club visitorClub;
	
	@ManyToOne
	@JoinColumn(name = "match_id", referencedColumnName = "match_id")
	private Match match;
	
	public ClubMatch() {
		
	}
	
	public ClubMatch(Club localClub, Club visitorClub, Match match) {
		super();
		this.localClub = localClub;
		this.visitorClub = visitorClub;
		this.match = match;
	}




	public Long getClubMatchId() {
		return clubMatchId;
	}




	public void setClubMatchId(Long clubMatchId) {
		this.clubMatchId = clubMatchId;
	}




	public Club getLocalClub() {
		return localClub;
	}




	public void setLocalClub(Club localClub) {
		this.localClub = localClub;
	}




	public Club getVisitorClub() {
		return visitorClub;
	}




	public void setVisitorClub(Club visitorClub) {
		this.visitorClub = visitorClub;
	}




	public Match getMatch() {
		return match;
	}




	public void setMatch(Match match) {
		this.match = match;
	}




	private static final long serialVersionUID = 1L;

}
