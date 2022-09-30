package com.fernandobetancourt.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "series")
public class Serie implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serie_id")
	private Long serieId;
	
	private String name;

	public Long getSerieId() {
		return serieId;
	}




	public void setSerieId(Long serieId) {
		this.serieId = serieId;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	private static final long serialVersionUID = 1L;	
	
}
