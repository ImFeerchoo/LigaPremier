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
@Table(name = "players")
public class Player implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "player_id")
	private Long playerId;

	private String names;

	@Column(name = "last_names")
	private String lastNames;

	private Integer number;

	private String position;

	private Integer age;

	private Double weight;

	private Double height;

	private String nationality;

	private String photo;

	@ManyToOne
	@JoinColumn(name = "club_id", referencedColumnName = "club_id")
	private Club club;

	public Player() {
	}

	public Player(Long playerId, String names, String lastNames, Integer number, String position, Integer age,
			Double weight, Double height, String nationality, String photo, Club club) {
		this.playerId = playerId;
		this.names = names;
		this.lastNames = lastNames;
		this.number = number;
		this.position = position;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.nationality = nationality;
		this.photo = photo;
		this.club = club;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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
		return Objects.hash(age, club, height, lastNames, names, nationality, number, photo, playerId, position,
				weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(age, other.age) && Objects.equals(club, other.club)
				&& Objects.equals(height, other.height) && Objects.equals(lastNames, other.lastNames)
				&& Objects.equals(names, other.names) && Objects.equals(nationality, other.nationality)
				&& Objects.equals(number, other.number) && Objects.equals(photo, other.photo)
				&& Objects.equals(playerId, other.playerId) && Objects.equals(position, other.position)
				&& Objects.equals(weight, other.weight);
	}

	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", names=" + names + ", lastNames=" + lastNames + ", number=" + number
				+ ", position=" + position + ", age=" + age + ", weight=" + weight + ", height=" + height
				+ ", nationality=" + nationality + ", photo=" + photo + ", club=" + club + "]";
	}

	public static final long serialVersionUID = 1L;

}
