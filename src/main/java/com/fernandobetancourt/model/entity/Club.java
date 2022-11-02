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
@Table(name = "clubes")
public class Club implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_id")
	private Long clubId;
	
	private String name;
	
	private String stadium;
	
	private String photo;
	
	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "group_id")
	private Group group;
	
	public Club() {}

	//Constructor with arguments to unit tests with id
	public Club(String name, String stadium, String photo, Group group) {
		super();
		this.name = name;
		this.stadium = stadium;
		this.photo = photo;
		this.group = group;
	}
	
	//Constructor with arguments to unit tests without id
		public Club(Long clubId, String name, String stadium, String photo, Group group) {
			super();
			this.clubId = clubId;
			this.name = name;
			this.stadium = stadium;
			this.photo = photo;
			this.group = group;
		}



	public Long getClubId() {
		return clubId;
	}



	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getStadium() {
		return stadium;
	}



	public void setStadium(String stadium) {
		this.stadium = stadium;
	}



	public String getPhoto() {
		return photo;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public Group getGroup() {
		return group;
	}



	public void setGroup(Group group) {
		this.group = group;
	}
	
	



	@Override
	public int hashCode() {
		return Objects.hash(clubId, group, name, photo, stadium);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Club other = (Club) obj;
		return Objects.equals(clubId, other.clubId) && Objects.equals(group, other.group)
				&& Objects.equals(name, other.name) && Objects.equals(photo, other.photo)
				&& Objects.equals(stadium, other.stadium);
	}





	private static final long serialVersionUID = 1L;

}
