package com.fernandobetancourt.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.core.userdetails.User;

import com.fernandobetancourt.model.dao.IUsersDao;
import com.fernandobetancourt.model.entity.UserEntity;

@Service
//public class UserServiceImpl implements IUserService, UserDetailsService {
public class UserServiceImpl implements IUserService{
	
//	@Autowired
//	private IUsersDao usersDao;
//
//	@Transactional(readOnly = true)
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		
//		UserEntity user = usersDao.findByUsername(username).orElseThrow(() -> {
//			throw new RuntimeException("User has not been foun");
//		});
//		
//		List<GrantedAuthority> authorities = user.getUsersRoles()
//												.stream()
//												.map(uRole -> uRole.getRole())
//												.map(role -> new SimpleGrantedAuthority(role.getName()))
//												.collect(Collectors.toList());
//			
//		System.out.println(user.getName());
//		System.out.println(user.getUsername());
//		System.out.println(user.getPassword());
//		System.out.println(user.getEmail());
//		System.out.println(user.getEnable());
//		authorities.forEach(s -> System.out.println(s.getAuthority()));
//		
//		return new User(user.getUsername(), user.getPassword(), 
//				user.getEnable(), true, true, true, authorities);
//	}

}
