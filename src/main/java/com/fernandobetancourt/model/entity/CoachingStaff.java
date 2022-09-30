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
@Table(name = "coaching_staffs")
public class CoachingStaff implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coaching_staff_id")
	private Long coachingStaffId;
	
	private String names;
	
	@Column(name = "last_names")
	private String lastNames;
	
	private String position;
	
	private Integer age;
	
	private Double weight;
	
	private Double height;
	
	private String nationality;
	
	private String photo;
	
	@ManyToOne
	@JoinColumn(name = "club_id", referencedColumnName = "club_id")
	private Club club;
	
	
	
	
	public Long getCoachingStaffId() {
		return coachingStaffId;
	}




	public void setCoachingStaffId(Long coachingStaffId) {
		this.coachingStaffId = coachingStaffId;
	}




	public String getNames() {
		return names;
	}




	public void setNames(String names) {
		this.names = names;
	}




	public String getLastNames() {
		return lastNames;
	}




	public void setLastNames(String lastNames) {
		this.lastNames = lastNames;
	}




	public String getPosition() {
		return position;
	}




	public void setPosition(String position) {
		this.position = position;
	}




	public Integer getAge() {
		return age;
	}




	public void setAge(Integer age) {
		this.age = age;
	}




	public Double getWeight() {
		return weight;
	}




	public void setWeight(Double weight) {
		this.weight = weight;
	}




	public Double getHeight() {
		return height;
	}




	public void setHeight(Double height) {
		this.height = height;
	}




	public String getNationality() {
		return nationality;
	}




	public void setNationality(String nationality) {
		this.nationality = nationality;
	}




	public String getPhoto() {
		return photo;
	}




	public void setPhoto(String photo) {
		this.photo = photo;
	}




	public Club getClub() {
		return club;
	}




	public void setClub(Club club) {
		this.club = club;
	}




	private static final long serialVersionUID = 1L;

}
