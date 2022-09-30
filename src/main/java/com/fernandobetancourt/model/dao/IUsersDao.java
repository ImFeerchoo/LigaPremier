package com.fernandobetancourt.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandobetancourt.model.entity.UserEntity;

import java.util.Optional;

@Repository
public interface IUsersDao extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);
}
