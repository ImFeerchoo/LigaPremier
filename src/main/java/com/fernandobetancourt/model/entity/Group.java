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
@Table(name = "groupss")
public class Group implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private Long groupId;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "serie_id", referencedColumnName = "serie_id")
	private Serie serie;
	
	

	public Long getGroupId() {
		return groupId;
	}



	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Serie getSerie() {
		return serie;
	}



	public void setSerie(Serie serie) {
		this.serie = serie;
	}



	private static final long serialVersionUID = 1L;
}
