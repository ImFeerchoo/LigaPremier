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
@Table(name = "users_roles")
public class UserRole implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "users_roles_id")
	private Long userRoleId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private UserEntity user;
	
	
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "role_id")
	private Role role;


	public Long getUserRoleId() {
		return userRoleId;
	}


	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}


	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	

}
