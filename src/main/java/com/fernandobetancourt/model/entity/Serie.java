package com.fernandobetancourt.model.entity;

import java.io.Serializable;
import java.util.Objects;

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

	public Serie() {}

	//Constructor with arguments to unit tests with id
	public Serie(Long serieId, String name) {
		super();
		this.serieId = serieId;
		this.name = name;
	}
	
	//Constructor with arguments to unit tests with id
	public Serie(String name) {
		super();
		this.name = name;
	}

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


	@Override
	public int hashCode() {
		return Objects.hash(name, serieId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Serie other = (Serie) obj;
		return Objects.equals(name, other.name) && Objects.equals(serieId, other.serieId);
	}






	private static final long serialVersionUID = 1L;	
	
}
