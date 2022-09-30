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
@Table(name = "lineups_coaching_staff")
public class LineupCoachingStaff implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_coaching_staff_id")
	private Long lineupCoachingStaffId;
	
//	@Column(name = "club_status")
//	private String clubStatus;
	
	@ManyToOne
	@JoinColumn(name = "coaching_staff_id", referencedColumnName = "coaching_staff_id")
	private CoachingStaff coachingStaff;
	
	@ManyToOne
	@JoinColumn(name = "lineup_id", referencedColumnName = "lineup_id")
	private Lineup lineup;
	
	
	
	
	public Long getLineupCoachingStaffId() {
		return lineupCoachingStaffId;
	}




	public void setLineupCoachingStaffId(Long lineupCoachingStaffId) {
		this.lineupCoachingStaffId = lineupCoachingStaffId;
	}




//	public String getClubStatus() {
//		return clubStatus;
//	}
//
//
//
//
//	public void setClubStatus(String clubStatus) {
//		this.clubStatus = clubStatus;
//	}




	public CoachingStaff getCoachingStaff() {
		return coachingStaff;
	}




	public void setCoachingStaff(CoachingStaff coachingStaff) {
		this.coachingStaff = coachingStaff;
	}




	public Lineup getLineup() {
		return lineup;
	}




	public void setLineup(Lineup lineup) {
		this.lineup = lineup;
	}




	private static final long serialVersionUID = 1L;

}
