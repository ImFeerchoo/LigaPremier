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
	
	public Journey() {}

	public Journey(Long journeyId, Integer number, Group group) {
		this.journeyId = journeyId;
		this.number = number;
		this.group = group;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(group, journeyId, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Journey other = (Journey) obj;
		return Objects.equals(group, other.group) && Objects.equals(journeyId, other.journeyId)
				&& Objects.equals(number, other.number);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Journey [journeyId=").append(journeyId).append(", number=").append(number).append(", group=")
				.append(group).append("]");
		return builder.toString();
	}



	private static final long serialVersionUID = 1L;

}
