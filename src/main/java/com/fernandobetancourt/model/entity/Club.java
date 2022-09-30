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
@Table(name = "clubes")
public class Club implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_id")
	private Long clubId;
	
	private String name;
	
	private String stadium;
	
	private String photo;
	
	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "group_id")
	private Group group;
	
	

	public Long getClubId() {
		return clubId;
	}



	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getStadium() {
		return stadium;
	}



	public void setStadium(String stadium) {
		this.stadium = stadium;
	}



	public String getPhoto() {
		return photo;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public Group getGroup() {
		return group;
	}



	public void setGroup(Group group) {
		this.group = group;
	}



	private static final long serialVersionUID = 1L;

}
