package com.fernandobetancourt;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Serie;

public class Data {
	
	//SERIES
	public static final List<Serie> SERIES_WITH_ID = Arrays.asList(
				new Serie(1L, "Serie A"),
				new Serie(2L, "Serie B"),
				new Serie(3L, "Serie C"),
				new Serie(4L, "Serie D"),
				new Serie(5L, "Serie E")
			);
	
	public static final List<Serie> SERIES_WITHOUT_ID = Arrays.asList(
			new Serie(null, "Serie A"),
			new Serie(null, "Serie B"),
			new Serie(null, "Serie C"),
			new Serie(null, "Serie D"),
			new Serie(null, "Serie E")
			);
	
	public static final Serie SERIE_WITH_ID_LESS_THAN_1 = new Serie(0L, "Serie A");
	
	public static Serie getSerieByIdBreakingReference(Long serieId) {
		Serie serie = SERIES_WITH_ID
											.stream()
											.filter(s -> s.getSerieId().equals(serieId))
											.findFirst()
											.orElseThrow();
		
		return new Serie(serie.getSerieId(), serie.getName());
	}
	
	public static Serie getSerieWithoutIdBreakingReference(int index) {
		Serie serie = SERIES_WITHOUT_ID.get(index);
		return new Serie(serie.getName());
	}
	
	
	//GROUPS
//	Long groupId, String name, Serie serie
	public static final List<Group> GROUPS_WITH_ID = Arrays.asList(
				new Group(1L, "Grupo 1", SERIES_WITH_ID.get(0)),
				new Group(2L, "Grupo 2", SERIES_WITH_ID.get(0)),
				new Group(3L, "Grupo 1", SERIES_WITH_ID.get(1)),
				new Group(4L, "Grupo 2", SERIES_WITH_ID.get(1)),
				new Group(5L, "Grupo 1", SERIES_WITH_ID.get(2))
			);
	
	public static final List<Group> GROUPS_WITHOUT_ID = Arrays.asList(
			new Group(null, "Grupo 1", SERIES_WITH_ID.get(0)),
			new Group(null, "Grupo 2", SERIES_WITH_ID.get(0)),
			new Group(null, "Grupo 1", SERIES_WITH_ID.get(1)),
			new Group(null, "Grupo 2", SERIES_WITH_ID.get(1)),
			new Group(null, "Grupo 1", SERIES_WITH_ID.get(2))
			);
	
	public static final Group GROUP_WITH_ID_LESS_THAN_1 = new Group(0L, "Grupo 1", SERIES_WITH_ID.get(2));
	
	
	//CLUBES
	
//	String name, String stadium, String photo, Group group
	public static final List<Club> CLUBES_WITH_ID = Arrays.asList(
			new Club(1L, "Cruz Azul", "Estadio Azteca", "crz", GROUPS_WITH_ID.get(0)),
			new Club(2L, "Catedráticos Élite FC", "Núcleo Deportivo y Centro de Espectáculos Ameca", "catedraticos-elite-fc", GROUPS_WITH_ID.get(0)),
			new Club(3L, "Cimarrones de Sonora FC", "Héroe de Nacozari", "cimarrones-de-sonora-fc", GROUPS_WITH_ID.get(1)),
			new Club(4L, "Colima Futbol Club", "Olimpico Universitario de Colima", "colima-futbol-club", GROUPS_WITH_ID.get(1)),
			new Club(5L, "Cafetaleros de Chiapas FC", "Victor Manuel Reyna", "cafetaleros-de-chiapas-fc", GROUPS_WITH_ID.get(2)),
			new Club(6L, "Aguacateros Club Deportivo Uruapan", "Unidad Deportiva Hermanos López Rayón", "aguacateros-club-deportivo-uruapan", GROUPS_WITH_ID.get(2)),
			new Club(7L, "Alebrijes de Oaxaca", "Instituto Tecnológico de Oaxaca", "alebrijes-de-oaxaca", GROUPS_WITH_ID.get(3)),
			new Club(8L, "Club de prueba", "Estadio de Prueba", "pue", GROUPS_WITH_ID.get(3)),
			new Club(9L, "America", "Estadio Azteca", "ame", GROUPS_WITH_ID.get(4)),
			new Club(10L, "Nuevo León", "Estadio de leones", "leones", GROUPS_WITH_ID.get(4))
			);

	public static final List<Club> CLUBES_WITHOUT_ID = Arrays.asList(
			new Club(null, "Cruz Azul", "Estadio Azteca", "crz", GROUPS_WITH_ID.get(0)),
			new Club(null, "Catedráticos Élite FC", "Núcleo Deportivo y Centro de Espectáculos Ameca", "catedraticos-elite-fc", GROUPS_WITH_ID.get(0)),
			new Club(null, "Cimarrones de Sonora FC", "Héroe de Nacozari", "cimarrones-de-sonora-fc", GROUPS_WITH_ID.get(1)),
			new Club(null, "Colima Futbol Club", "Olimpico Universitario de Colima", "colima-futbol-club", GROUPS_WITH_ID.get(1)),
			new Club(null, "Cafetaleros de Chiapas FC", "Victor Manuel Reyna", "cafetaleros-de-chiapas-fc", GROUPS_WITH_ID.get(2)),
			new Club(null, "Aguacateros Club Deportivo Uruapan", "Unidad Deportiva Hermanos López Rayón", "aguacateros-club-deportivo-uruapan", GROUPS_WITH_ID.get(2)),
			new Club(null, "Alebrijes de Oaxaca", "Instituto Tecnológico de Oaxaca", "alebrijes-de-oaxaca", GROUPS_WITH_ID.get(3)),
			new Club(null, "Club de prueba", "Estadio de Prueba", "pue", GROUPS_WITH_ID.get(3)),
			new Club(null, "America", "Estadio Azteca", "ame", GROUPS_WITH_ID.get(4)),
			new Club(null, "Nuevo León", "Estadio de leones", "leones", GROUPS_WITH_ID.get(4))
			);
	
	public static final Club CLUB_WITH_ID_LESS_THAN_1 = new Club(0L, "Cruz Azul", "Estadio Azteca", "crz", GROUPS_WITH_ID.get(0));
	
	public static List<Club> findClubsByGroup(Long groupId){
		return CLUBES_WITH_ID
				.stream()
				.filter(c -> c.getGroup().getGroupId().equals(groupId))
				.collect(Collectors.toList());
	}
	
	public static Club getClubByIdBreakingReference(Long clubcId){
		Optional<Club> clubOptional = CLUBES_WITH_ID
					.stream()
					.filter(c -> c.getClubId().equals(clubcId))
					.findFirst();
		
		Club club = new Club(clubOptional.orElseThrow().getClubId(), clubOptional.orElseThrow().getName(), clubOptional.orElseThrow().getStadium(), clubOptional.orElseThrow().getPhoto(), clubOptional.orElseThrow().getGroup());
		
		return club;
	}
	
	public static Club getClubWithoutIdByPositionbreakingReference(int index) {
		Club clubRecovered = CLUBES_WITHOUT_ID.get(index);
		return new Club(clubRecovered.getName(), clubRecovered.getStadium(), clubRecovered.getPhoto(), clubRecovered.getGroup());
	}
}
