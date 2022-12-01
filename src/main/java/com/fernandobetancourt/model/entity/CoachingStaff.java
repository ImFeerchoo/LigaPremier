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

	public CoachingStaff() {
	}

	public CoachingStaff(Long coachingStaffId, String names, String lastNames, String position, Integer age,
			Double weight, Double height, String nationality, String photo, Club club) {
		super();
		this.coachingStaffId = coachingStaffId;
		this.names = names;
		this.lastNames = lastNames;
		this.position = position;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.nationality = nationality;
		this.photo = photo;
		this.club = club;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(age, club, coachingStaffId, height, lastNames, names, nationality, photo, position, weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoachingStaff other = (CoachingStaff) obj;
		return Objects.equals(age, other.age) && Objects.equals(club, other.club)
				&& Objects.equals(coachingStaffId, other.coachingStaffId) && Objects.equals(height, other.height)
				&& Objects.equals(lastNames, other.lastNames) && Objects.equals(names, other.names)
				&& Objects.equals(nationality, other.nationality) && Objects.equals(photo, other.photo)
				&& Objects.equals(position, other.position) && Objects.equals(weight, other.weight);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoachingStaff [coachingStaffId=").append(coachingStaffId).append(", names=").append(names)
				.append(", lastNames=").append(lastNames).append(", position=").append(position).append(", age=")
				.append(age).append(", weight=").append(weight).append(", height=").append(height)
				.append(", nationality=").append(nationality).append(", photo=").append(photo).append(", club=")
				.append(club).append("]");
		return builder.toString();
	}

	private static final long serialVersionUID = 1L;

}
