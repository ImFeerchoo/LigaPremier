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
@Table(name = "cards")
public class Card implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_id")
	private Long cardId;
	
	private Integer minute;
	
	private String color;
	
	private String photo;
	
	@ManyToOne
	@JoinColumn(name = "player_id", referencedColumnName = "player_id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "match_id", referencedColumnName = "match_id")
	private Match match;
	
	public Card() {}
	
	public Card(Long cardId, Integer minute, String color, String photo, Player player, Match match) {
		this.cardId = cardId;
		this.minute = minute;
		this.color = color;
		this.photo = photo;
		this.player = player;
		this.match = match;
	}



	public Long getCardId() {
		return cardId;
	}



	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}



	public Integer getMinute() {
		return minute;
	}



	public void setMinute(Integer minute) {
		this.minute = minute;
	}



	public String getColor() {
		return color;
	}



	public void setColor(String color) {
		this.color = color;
	}



	public String getPhoto() {
		return photo;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public Player getPlayer() {
		return player;
	}



	public void setPlayer(Player player) {
		this.player = player;
	}



	public Match getMatch() {
		return match;
	}



	public void setMatch(Match match) {
		this.match = match;
	}



	private static final long serialVersionUID = 1L;

}
