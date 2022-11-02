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
	
	public Group() {}

	//Constructor with arguments to unit tests with id
	public Group(Long groupId, String name, Serie serie) {
		super();
		this.groupId = groupId;
		this.name = name;
		this.serie = serie;
	}
	
	//Constructor with arguments to unit tests without id
	public Group(String name, Serie serie) {
		super();
		this.name = name;
		this.serie = serie;
	}
	

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


	@Override
	public int hashCode() {
		return Objects.hash(groupId, name, serie);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return Objects.equals(groupId, other.groupId) && Objects.equals(name, other.name)
				&& Objects.equals(serie, other.serie);
	}

	private static final long serialVersionUID = 1L;
}
