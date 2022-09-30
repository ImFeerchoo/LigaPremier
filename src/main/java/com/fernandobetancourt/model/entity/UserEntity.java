package com.fernandobetancourt.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	private String username;
	
	private String password;
	
	private String name;
	
	@Column(name = "last_name")
	private String lastName;
	
	private String email;
	
	private Boolean enable;
	
	@OneToMany(mappedBy = "user")
	private List<UserRole> usersRoles;

	public Long getUserId() {
		return userId;
	}




	public void setUserId(Long userId) {
		this.userId = userId;
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getLastName() {
		return lastName;
	}




	public void setLastName(String lastName) {
		this.lastName = lastName;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
	public List<UserRole> getUsersRoles() {
		return usersRoles;
	}




	public void setUsersRoles(List<UserRole> usersRoles) {
		this.usersRoles = usersRoles;
	}




	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", name=" + name
				+ ", lastName=" + lastName + ", email=" + email + ", enable=" + enable + ", usersRoles=" + usersRoles
				+ "]";
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
