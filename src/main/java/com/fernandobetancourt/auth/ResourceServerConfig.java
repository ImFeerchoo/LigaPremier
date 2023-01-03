package com.fernandobetancourt.auth;

//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
public class ResourceServerConfig{
	
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
////		http.authorizeRequests().antMatchers("/api/players", "/api/testUsuario").permitAll()
////		.antMatchers("/api/player/{id}").hasAnyRole("USER", "ADMIN")
////		.antMatchers("/api/playersClub/{clubId}").hasRole("ADMIN")
////		.anyRequest().authenticated();
//
//		//TODOS PUEDEN LEER PERO SOLO LOS ADMINS PUEDEN ESCRIBIR
//		
//		//READ
//		//PLAYERS
//		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/players", "/api/player/{id}", "/api/playersClub/{clubId}").permitAll()
//		//COACHING STAFFS
//		.antMatchers(HttpMethod.GET, "/api/coachingStaffs", "/api/coachingStaffs/{coachingStaffsId}").permitAll()
//		//CLUBES
//		.antMatchers(HttpMethod.GET, "/api/clubes", "/api/clubes/{id}").permitAll()
//		//SERIES
//		.antMatchers(HttpMethod.GET, "/api/series", "/api/serie/{serieId}").permitAll()
//		//GROUPS
//		.antMatchers(HttpMethod.GET, "/api/groups", "/api/groups/{groupId}").permitAll()
//		//JOURNEYS
//		.antMatchers(HttpMethod.GET, "/api/journeys", "/api/journeys/{journeyId}").permitAll()
//		//MATCHES
//		.antMatchers(HttpMethod.GET, "/api/matches/{matchId}").permitAll()
//		
//		
//		
//		//TEST
//		.antMatchers("/api/testUsuario").hasAnyRole("USER", "ADMIN")
//		
//		.anyRequest().hasRole("ADMIN");
//	}
	

}
