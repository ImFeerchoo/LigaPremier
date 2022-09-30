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
@Table(name = "journyes")
public class Journey implements Serializable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "journey_id")
	private Long journeyId;
	
	private Integer number;
	
	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "group_id")
	private Group group;
	
	
	
	public Long getJourneyId() {
		return journeyId;
	}



	public void setJourneyId(Long journeyId) {
		this.journeyId = journeyId;
	}



	public Integer getNumber() {
		return number;
	}



	public void setNumber(Integer number) {
		this.number = number;
	}



	public Group getGroup() {
		return group;
	}



	public void setGroup(Group group) {
		this.group = group;
	}



	private static final long serialVersionUID = 1L;

}
