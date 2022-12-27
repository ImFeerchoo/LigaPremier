package com.fernandobetancourt;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.LineupPlayer;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.model.entity.Serie;

public class Data {

	// SERIES
	public static final List<Serie> SERIES_WITH_ID = Arrays.asList(new Serie(1L, "Serie A"), new Serie(2L, "Serie B"),
			new Serie(3L, "Serie C"), new Serie(4L, "Serie D"), new Serie(5L, "Serie E"));

	public static final List<Serie> SERIES_WITHOUT_ID = Arrays.asList(new Serie(null, "Serie A"),
			new Serie(null, "Serie B"), new Serie(null, "Serie C"), new Serie(null, "Serie D"),
			new Serie(null, "Serie E"));

	public static final Serie SERIE_WITH_ID_LESS_THAN_1 = new Serie(0L, "Serie A");

	public static Serie getSerieByIdBreakingReference(Long serieId) {
		Serie serie = SERIES_WITH_ID.stream().filter(s -> s.getSerieId().equals(serieId)).findFirst().orElseThrow();

		return new Serie(serie.getSerieId(), serie.getName());
	}

	public static Serie getSerieWithoutIdBreakingReference(int index) {
		Serie serie = SERIES_WITHOUT_ID.get(index);
		return new Serie(serie.getName());
	}

	// GROUPS
//	Long groupId, String name, Serie serie
	public static final List<Group> GROUPS_WITH_ID = Arrays.asList(new Group(1L, "Grupo 1", SERIES_WITH_ID.get(0)),
			new Group(2L, "Grupo 2", SERIES_WITH_ID.get(0)), new Group(3L, "Grupo 1", SERIES_WITH_ID.get(1)),
			new Group(4L, "Grupo 2", SERIES_WITH_ID.get(1)), new Group(5L, "Grupo 1", SERIES_WITH_ID.get(2)));

	public static final List<Group> GROUPS_WITHOUT_ID = Arrays.asList(new Group(null, "Grupo 1", SERIES_WITH_ID.get(0)),
			new Group(null, "Grupo 2", SERIES_WITH_ID.get(0)), new Group(null, "Grupo 1", SERIES_WITH_ID.get(1)),
			new Group(null, "Grupo 2", SERIES_WITH_ID.get(1)), new Group(null, "Grupo 1", SERIES_WITH_ID.get(2)));

	public static final Group GROUP_WITH_ID_LESS_THAN_1 = new Group(0L, "Grupo 1", SERIES_WITH_ID.get(2));

	public static Group getGroupByIdBreakingReference(Long groupId) {
		Group group = GROUPS_WITH_ID.stream().filter(g -> g.getGroupId().equals(groupId)).findFirst().orElseThrow();
		return new Group(group.getGroupId(), group.getName(), group.getSerie());
	}

	public static Group getGroupWithoutIdByPositionBreakingReference(int index) {
		Group group = GROUPS_WITHOUT_ID.get(index);
		return new Group(group.getGroupId(), group.getName(), group.getSerie());

	}

//	public static List<Group> getGroupsBySerie(Serie serie){
//		return GROUPS_WITH_ID
//					.stream()
//					.filter(g -> g.getSerie().equals(serie))
//					.collect(Collectors.toList());
//	}

	public static List<Group> getGroupsBySerie(Long serieId) {
		return GROUPS_WITH_ID.stream().filter(g -> g.getSerie().getSerieId().equals(serieId))
				.collect(Collectors.toList());
	}

	// JOURNEYS

	public static final List<Journey> JOURNEYS_WITH_ID = Arrays.asList(
			new Journey(1L, 1, GROUPS_WITH_ID.get(0)),
			new Journey(2L, 2, GROUPS_WITH_ID.get(0)),
			new Journey(3L, 3, GROUPS_WITH_ID.get(0)),
			new Journey(4L, 1, GROUPS_WITH_ID.get(1)),
			new Journey(5L, 2, GROUPS_WITH_ID.get(1)),
			new Journey(6L, 3, GROUPS_WITH_ID.get(1)),
			new Journey(7L, 1, GROUPS_WITH_ID.get(2)),
			new Journey(8L, 2, GROUPS_WITH_ID.get(2)),
			new Journey(9L, 3, GROUPS_WITH_ID.get(2)),
			new Journey(10L, 1, GROUPS_WITH_ID.get(3)),
			new Journey(11L, 2, GROUPS_WITH_ID.get(3)),
			new Journey(12L, 3, GROUPS_WITH_ID.get(3)),
			new Journey(13L, 1, GROUPS_WITH_ID.get(4)),
			new Journey(14L, 2, GROUPS_WITH_ID.get(4)),
			new Journey(15L, 3, GROUPS_WITH_ID.get(4))
			);

	public static final List<Journey> JOURNEYS_WITHOUT_ID = Arrays.asList(new Journey(null, 1, GROUPS_WITH_ID.get(0)),
			new Journey(null, 2, GROUPS_WITH_ID.get(0)), new Journey(null, 3, GROUPS_WITH_ID.get(0)),
			new Journey(null, 1, GROUPS_WITH_ID.get(1)), new Journey(null, 2, GROUPS_WITH_ID.get(1)),
			new Journey(null, 3, GROUPS_WITH_ID.get(1)), new Journey(null, 1, GROUPS_WITH_ID.get(2)),
			new Journey(null, 2, GROUPS_WITH_ID.get(2)), new Journey(null, 3, GROUPS_WITH_ID.get(2)),
			new Journey(null, 1, GROUPS_WITH_ID.get(3)), new Journey(null, 2, GROUPS_WITH_ID.get(3)),
			new Journey(null, 3, GROUPS_WITH_ID.get(3)), new Journey(null, 1, GROUPS_WITH_ID.get(4)),
			new Journey(null, 2, GROUPS_WITH_ID.get(4)), new Journey(null, 3, GROUPS_WITH_ID.get(4)));

	public static final Journey JOURNEY_WITH_ID_LESS_THAN_1 = new Journey(0L, 1, GROUPS_WITH_ID.get(0));

	public static Journey getJourneyByIdBreakingReference(Long journeyId) {
		Journey journey = JOURNEYS_WITH_ID.stream().filter(j -> j.getJourneyId().equals(journeyId)).findFirst()
				.orElseThrow();

		return new Journey(journey.getJourneyId(), journey.getNumber(), journey.getGroup());
	}

	public static Journey getJourneyWithoutIdByPositionBreakingReference(int index) {
		Journey journey = JOURNEYS_WITHOUT_ID.get(index);
		return new Journey(journey.getJourneyId(), journey.getNumber(), journey.getGroup());
	}
	
	public static List<Journey> getJourneysByGroup(Long groupId){
		return JOURNEYS_WITH_ID.stream()
					.filter(j -> j.getGroup().getGroupId().equals(groupId))
					.collect(Collectors.toList());
	}

	// CLUBES

//	String name, String stadium, String photo, Group group
	public static final List<Club> CLUBES_WITH_ID = Arrays.asList(
			new Club(1L, "Cruz Azul", "Estadio Azteca", "crz", GROUPS_WITH_ID.get(0)),
			new Club(2L, "Catedráticos Élite FC", "Núcleo Deportivo y Centro de Espectáculos Ameca",
					"catedraticos-elite-fc", GROUPS_WITH_ID.get(0)),
			new Club(3L, "Cimarrones de Sonora FC", "Héroe de Nacozari", "cimarrones-de-sonora-fc",
					GROUPS_WITH_ID.get(1)),
			new Club(4L, "Colima Futbol Club", "Olimpico Universitario de Colima", "colima-futbol-club",
					GROUPS_WITH_ID.get(1)),
			new Club(5L, "Cafetaleros de Chiapas FC", "Victor Manuel Reyna", "cafetaleros-de-chiapas-fc",
					GROUPS_WITH_ID.get(2)),
			new Club(6L, "Aguacateros Club Deportivo Uruapan", "Unidad Deportiva Hermanos López Rayón",
					"aguacateros-club-deportivo-uruapan", GROUPS_WITH_ID.get(2)),
			new Club(7L, "Alebrijes de Oaxaca", "Instituto Tecnológico de Oaxaca", "alebrijes-de-oaxaca",
					GROUPS_WITH_ID.get(3)),
			new Club(8L, "Club de prueba", "Estadio de Prueba", "pue", GROUPS_WITH_ID.get(3)),
			new Club(9L, "America", "Estadio Azteca", "ame", GROUPS_WITH_ID.get(4)),
			new Club(10L, "Nuevo León", "Estadio de leones", "leones", GROUPS_WITH_ID.get(4)));

	public static final List<Club> CLUBES_WITHOUT_ID = Arrays.asList(
			new Club(null, "Cruz Azul", "Estadio Azteca", "crz", GROUPS_WITH_ID.get(0)),
			new Club(null, "Catedráticos Élite FC", "Núcleo Deportivo y Centro de Espectáculos Ameca",
					"catedraticos-elite-fc", GROUPS_WITH_ID.get(0)),
			new Club(null, "Cimarrones de Sonora FC", "Héroe de Nacozari", "cimarrones-de-sonora-fc",
					GROUPS_WITH_ID.get(1)),
			new Club(null, "Colima Futbol Club", "Olimpico Universitario de Colima", "colima-futbol-club",
					GROUPS_WITH_ID.get(1)),
			new Club(null, "Cafetaleros de Chiapas FC", "Victor Manuel Reyna", "cafetaleros-de-chiapas-fc",
					GROUPS_WITH_ID.get(2)),
			new Club(null, "Aguacateros Club Deportivo Uruapan", "Unidad Deportiva Hermanos López Rayón",
					"aguacateros-club-deportivo-uruapan", GROUPS_WITH_ID.get(2)),
			new Club(null, "Alebrijes de Oaxaca", "Instituto Tecnológico de Oaxaca", "alebrijes-de-oaxaca",
					GROUPS_WITH_ID.get(3)),
			new Club(null, "Club de prueba", "Estadio de Prueba", "pue", GROUPS_WITH_ID.get(3)),
			new Club(null, "America", "Estadio Azteca", "ame", GROUPS_WITH_ID.get(4)),
			new Club(null, "Nuevo León", "Estadio de leones", "leones", GROUPS_WITH_ID.get(4)));

	public static final Club CLUB_WITH_ID_LESS_THAN_1 = new Club(0L, "Cruz Azul", "Estadio Azteca", "crz",
			GROUPS_WITH_ID.get(0));

	public static List<Club> findClubsByGroup(Long groupId) {
		return CLUBES_WITH_ID.stream().filter(c -> c.getGroup().getGroupId().equals(groupId))
				.collect(Collectors.toList());
	}

	public static Club getClubByIdBreakingReference(Long clubcId) {
		Optional<Club> clubOptional = CLUBES_WITH_ID.stream().filter(c -> c.getClubId().equals(clubcId)).findFirst();

		Club club = new Club(clubOptional.orElseThrow().getClubId(), clubOptional.orElseThrow().getName(),
				clubOptional.orElseThrow().getStadium(), clubOptional.orElseThrow().getPhoto(),
				clubOptional.orElseThrow().getGroup());

		return club;
	}

	public static Club getClubWithoutIdByPositionBreakingReference(int index) {
		Club clubRecovered = CLUBES_WITHOUT_ID.get(index);
		return new Club(clubRecovered.getName(), clubRecovered.getStadium(), clubRecovered.getPhoto(),
				clubRecovered.getGroup());
	}

	// PLAYERS
//	Long playerId, String names, String lastNames, Integer number, String position, Integer age,
//	Double weight, Double height, String nationality, String photo, Club club			1. 2. 4. 5
	public static List<Player> PLAYERS_WITH_ID = Arrays.asList(
			new Player(1L, "José Rafael", "Castrejon Ramos", 1, "Portero", 21, 89.0, 1.86, "Mexicano", "JCastrjon",
					getClubByIdBreakingReference(1L)),
			new Player(2L, "Brandón de Jesús", "Torres Montes", 12, "Portero", 27, 83.0, 1.83, "Mexicano", "BTorres",
					getClubByIdBreakingReference(1L)),
			new Player(3L, "Alexis Edmundo", "Silva Montes", 40, "Portero", 22, 78.0, 1.86, "Mexicano", "ASilva",
					getClubByIdBreakingReference(1L)),
			new Player(4L, "José Alejandro", "Pintado Leyva", 101, "Portero", 19, 76.0, 1.73, "Mexicano", "JPintado",
					getClubByIdBreakingReference(2L)),
			new Player(5L, "Abraham", "Reyes Ramos", 82, "Defensa", 22, 84.0, 1.92, "Mexicano", "AReyes",
					getClubByIdBreakingReference(2L)),
			new Player(6L, "Manuel Ramos", "Rivera Sosa", 81, "Medio", 22, 70.0, 1.68, "Mexicano", "MRivera",
					getClubByIdBreakingReference(2L)),
			new Player(7L, "Alejandro Jair", "Peláez Correa", 81, "Portero", 28, 82.0, 1.88, "Mexicano", "APeláez",
					getClubByIdBreakingReference(3L)),
			new Player(8L, "Victor Guadalupe", "Torres Chávez", 82, "Defensa", 26, 80.0, 1.75, "Mexicano", "VTorres",
					getClubByIdBreakingReference(3L)),
			new Player(9L, "José Juan", "Guillén Rangel", 85, "Medio", 23, 70.0, 1.7, "Mexicano", "JGuillén",
					getClubByIdBreakingReference(3L)),
			new Player(10L, "José Misael", "Corona Canseco", 12, "Portero", 19, 71.0, 1.76, "Mexicano", "JCorona",
					getClubByIdBreakingReference(4L)),
			new Player(11L, "Uriem", "Castrejón Sotelo", 2, "Defensa Lateral", 22, 68.0, 1.7, "Mexicano", "UCastrejón",
					getClubByIdBreakingReference(4L)),
			new Player(12L, "Eduardo Alfredo", "Quiróz Juárez", 4, "Medio Ofensivo", 23, 74.0, 1.79, "Mexicano",
					"EQuiróz", getClubByIdBreakingReference(4L)),
			new Player(13L, "José Pedro", "Pérez Hernández", 40, "Medio", 27, 75.0, 1.69, "Mexicano", "JPérez",
					getClubByIdBreakingReference(1L)),
			new Player(14L, "Rafael", "Juárez Ramos", 56, "Defensa", 24, 71.0, 1.80, "Mexicano", "RJuárez",
					getClubByIdBreakingReference(1L)),
			new Player(15L, "Alfredo", "Hernández Martínez", 12, "Defensa", 19, 68.0, 1.68, "Mexicano", "AHernández",
					getClubByIdBreakingReference(1L)),
			new Player(16L, "Cesar Hernan", "Cortéz Pérez", 7, "Delantero", 20, 77.0, 1.79, "Mexicano", "CCortéz",
					getClubByIdBreakingReference(1L)),
			new Player(17L, "Luis Michel", "Toledo Alfaro", 50, "Medio", 27, 69.0, 1.73, "Mexicano", "LToledo",
					getClubByIdBreakingReference(1L)),
			new Player(18L, "Jesús Ángel", "Barrales Barrera", 20, "Delantero", 26, 70.0, 1.72, "Mexicano", "JBarrales",
					getClubByIdBreakingReference(1L)),
			new Player(19L, "Juan Diego", "Mejía Quiróz", 1, "Medio Defensivo", 24, 74.0, 1.75, "Mexicano", "JMejía",
					getClubByIdBreakingReference(1L)),
			new Player(20L, "Alberto Alan", "Ramírez García", 23, "Defensa Central", 32, 80.0, 1.81, "Mexicano",
					"ARamírez", getClubByIdBreakingReference(1L)),
			new Player(21L, "Rogelio", "Nabajas Salázar", 1, "Portero", 25, 75.0, 1.79, "Mexicano", "RNabajas",
					getClubByIdBreakingReference(2L)),
			new Player(22L, "Ramon", "Valdez Nazario", 65, "Portero", 20, 60.0, 1.70, "Mexicano", "RValdez",
					getClubByIdBreakingReference(2L)),
			new Player(23L, "Eduardo Ramiro", "Sotelo Hernández", 11, "Defensa Central", 35, 81.0, 1.78, "Mexicano",
					"ESotelo", getClubByIdBreakingReference(2L)),
			new Player(24L, "Diego", "González Puertas", 4, "Defensa Ofensiva", 28, 69.0, 1.73, "Mexicano", "DGonzález",
					getClubByIdBreakingReference(2L)),
			new Player(25L, "Juan Jesús", "Sánchez Baez", 27, "Delantero", 22, 74.0, 1.80, "Mexicano", "JSánchez",
					getClubByIdBreakingReference(2L)),
			new Player(26L, "Ramiro Pedro", "Martínez Pelaez", 3, "Medio Defensivo", 18, 71.0, 1.75, "Mexicano",
					"RMartínez", getClubByIdBreakingReference(2L)),
			new Player(27L, "Rodrigo Cesar", "Barroso Gúzman", 49, "Defensa Central", 33, 80.0, 1.81, "Mexicano",
					"RBarroso", getClubByIdBreakingReference(2L)),
			new Player(28L, "Brandon Guadalupe", "Fernández Zamano", 6, "Delantero Izquierdo", 24, 74.0, 1.80,
					"Mexicano", "BFernández", getClubByIdBreakingReference(2L)),
			new Player(29L, "Juan", "Rojas García", 20, "Centro Ofensivo", 22, 74.0, 1.70, "Mexicano", "JRojas",
					getClubByIdBreakingReference(1L)));

	public static List<Player> PLAYERS_WITHOUT_ID = Arrays.asList(
			new Player(null, "José Rafael", "Castrejon Ramos", 1, "Portero", 21, 89.0, 1.86, "Mexicano", "JCastrjon",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Brandón de Jesús", "Torres Montes", 12, "Portero", 27, 83.0, 1.83, "Mexicano", "BTorres",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Alexis Edmundo", "Silva Montes", 40, "Portero", 22, 78.0, 1.86, "Mexicano", "ASilva",
					getClubByIdBreakingReference(1L)),
			new Player(null, "José Alejandro", "Pintado Leyva", 101, "Portero", 19, 76.0, 1.73, "Mexicano", "JPintado",
					getClubByIdBreakingReference(2L)),
			new Player(null, "Abraham", "Reyes Ramos", 82, "Defensa", 22, 84.0, 1.92, "Mexicano", "AReyes",
					getClubByIdBreakingReference(2L)),
			new Player(null, "Manuel Ramos", "Rivera Sosa", 81, "Medio", 22, 70.0, 1.68, "Mexicano", "MRivera",
					getClubByIdBreakingReference(2L)),
			new Player(null, "Alejandro Jair", "Peláez Correa", 81, "Portero", 28, 82.0, 1.88, "Mexicano", "APeláez",
					getClubByIdBreakingReference(3L)),
			new Player(null, "Victor Guadalupe", "Torres Chávez", 82, "Defensa", 26, 80.0, 1.75, "Mexicano", "VTorres",
					getClubByIdBreakingReference(3L)),
			new Player(null, "José Juan", "Guillén Rangel", 85, "Medio", 23, 70.0, 1.7, "Mexicano", "JGuillén",
					getClubByIdBreakingReference(3L)),
			new Player(null, "José Misael", "Corona Canseco", 12, "Portero", 19, 71.0, 1.76, "Mexicano", "JCorona",
					getClubByIdBreakingReference(4L)),
			new Player(null, "Uriem", "Castrejón Sotelo", 2, "Defensa Lateral", 22, 68.0, 1.7, "Mexicano", "UCastrejón",
					getClubByIdBreakingReference(4L)),
			new Player(null, "Eduardo Alfredo", "Quiróz Juárez", 4, "Medio Ofensivo", 23, 74.0, 1.79, "Mexicano",
					"EQuiróz", getClubByIdBreakingReference(4L)),
			new Player(null, "José Pedro", "Pérez Hernández", 40, "Medio", 27, 75.0, 1.69, "Mexicano", "JPérez",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Rafael", "Juárez Ramos", 56, "Defensa", 24, 71.0, 1.80, "Mexicano", "RJuárez",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Alfredo", "Hernández Martínez", 12, "Defensa", 19, 68.0, 1.68, "Mexicano", "AHernández",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Cesar Hernan", "Cortéz Pérez", 7, "Delantero", 20, 77.0, 1.79, "Mexicano", "CCortéz",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Luis Michel", "Toledo Alfaro", 50, "Medio", 27, 69.0, 1.73, "Mexicano", "LToledo",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Jesús Ángel", "Barrales Barrera", 20, "Delantero", 26, 70.0, 1.72, "Mexicano",
					"JBarrales", getClubByIdBreakingReference(1L)),
			new Player(null, "Juan Diego", "Mejía Quiróz", 1, "Medio Defensivo", 24, 74.0, 1.75, "Mexicano", "JMejía",
					getClubByIdBreakingReference(1L)),
			new Player(null, "Alberto Alan", "Ramírez García", 23, "Defensa Central", 32, 80.0, 1.81, "Mexicano",
					"ARamírez", getClubByIdBreakingReference(1L)),
			new Player(null, "Rogelio", "Nabajas Salázar", 1, "Portero", 25, 75.0, 1.79, "Mexicano", "RNabajas",
					getClubByIdBreakingReference(2L)),
			new Player(null, "Ramon", "Valdez Nazario", 65, "Portero", 20, 60.0, 1.70, "Mexicano", "RValdez",
					getClubByIdBreakingReference(2L)),
			new Player(null, "Eduardo Ramiro", "Sotelo Hernández", 11, "Defensa Central", 35, 81.0, 1.78, "Mexicano",
					"ESotelo", getClubByIdBreakingReference(2L)),
			new Player(null, "Diego", "González Puertas", 4, "Defensa Ofensiva", 28, 69.0, 1.73, "Mexicano",
					"DGonzález", getClubByIdBreakingReference(2L)),
			new Player(null, "Juan Jesús", "Sánchez Baez", 27, "Delantero", 22, 74.0, 1.80, "Mexicano", "JSánchez",
					getClubByIdBreakingReference(2L)),
			new Player(null, "Ramiro Pedro", "Martínez Pelaez", 3, "Medio Defensivo", 18, 71.0, 1.75, "Mexicano",
					"RMartínez", getClubByIdBreakingReference(2L)),
			new Player(null, "Rodrigo Cesar", "Barroso Gúzman", 49, "Defensa Central", 33, 80.0, 1.81, "Mexicano",
					"RBarroso", getClubByIdBreakingReference(2L)),
			new Player(null, "Brandon Guadalupe", "Fernández Zamano", 6, "Delantero Izquierdo", 24, 74.0, 1.80,
					"Mexicano", "BFernández", getClubByIdBreakingReference(2L)),
			new Player(null, "Juan", "Rojas García", 20, "Centro Ofensivo", 22, 74.0, 1.70, "Mexicano", "JRojas",
					getClubByIdBreakingReference(1L)));

	public static final Player PLAYER_WITH_ID_LESS_THAN_1 = new Player(0L, "José Rafael", "Castrejon Ramos", 1,
			"Portero", 21, 89.0, 1.86, "Mexicano", "JCastrjon", getClubByIdBreakingReference(1L));

	public static Player getPlayerByIdBreakingReference(Long playerId) {
		Player player = PLAYERS_WITH_ID.stream().filter(p -> p.getPlayerId().equals(playerId)).findFirst()
				.orElseThrow();
		return new Player(player.getPlayerId(), player.getNames(), player.getLastNames(), player.getNumber(),
				player.getPosition(), player.getAge(), player.getWeight(), player.getHeight(), player.getNationality(),
				player.getPhoto(), player.getClub());
	}

	public static Player getPlayerWithoutIdByPositionBreakingReference(int index) {
		Player player = PLAYERS_WITHOUT_ID.get(index);
		return new Player(player.getPlayerId(), player.getNames(), player.getLastNames(), player.getNumber(),
				player.getPosition(), player.getAge(), player.getWeight(), player.getHeight(), player.getNationality(),
				player.getPhoto(), player.getClub());
	}

	public static Player getPlayerByName(String name) {
		Player player = PLAYERS_WITH_ID.stream().filter(p -> p.getNames().equals(name)).findFirst().orElseThrow();
		return new Player(player.getPlayerId(), player.getNames(), player.getLastNames(), player.getNumber(),
				player.getPosition(), player.getAge(), player.getWeight(), player.getHeight(), player.getNationality(),
				player.getPhoto(), player.getClub());
	}

	public static List<Player> getPlayersByClub(Long clubId) {
		return PLAYERS_WITH_ID.stream().filter(p -> p.getClub().getClubId().equals(clubId))
				.collect(Collectors.toList());
	}

	// COACHING_STAFFS
//	Long coachingStaffId, String names, String lastNames, String position, Integer age,
//	Double weight, Double height, String nationality, String photo, Club club
	public static List<CoachingStaff> COACHING_STAFFS_WITH_ID = Arrays.asList(
			new CoachingStaff(1L, "Jorge Humberto", "Torres Mata", "Director Técnico", 59, 85.0, 1.76, "Mexicano",
					"JTorres", getClubByIdBreakingReference(1L)),
			new CoachingStaff(2L, "Miguel", "Orozco Flores", "Médico", 67, 86.0, 1.79, "Mexicano", "MOrozco",
					getClubByIdBreakingReference(1L)),
			new CoachingStaff(3L, "José", "Islas Rodríguez", "Director Técnico", 37, 80.0, 1.82, "Mexicano", "JIslas",
					getClubByIdBreakingReference(2L)),
			new CoachingStaff(4L, "Hugo Omar", "Santacruz Cornejo", "Preparador Físico", 35, 78.0, 1.71, "Mexicano",
					"HSantacruz", getClubByIdBreakingReference(2L)),
			new CoachingStaff(5L, "Alberto", "Cortéz Piedradita", "Director Técnico", 50, 71.0, 1.70, "Mexicano",
					"ACortéz", getClubByIdBreakingReference(3L)),
			new CoachingStaff(6L, "Mitchelle Katerine", "Pérez Báez", "Médico", 26, 54.0, 1.56, "Mexicano", "MPérez",
					getClubByIdBreakingReference(4L)),
			new CoachingStaff(7L, "Ángela", "Rangel Lemus", "Auxiliar Técnico", 42, 42.0, 1.61, "Mexicano", "ÁRangel",
					getClubByIdBreakingReference(5L)),
			new CoachingStaff(8L, "Sofia Ivet", "Morales Gónzales", "Médico", 35, 43.0, 1.70, "Mexicano", "SMorales",
					getClubByIdBreakingReference(6L)),
			new CoachingStaff(9L, "Juan Pedro", "Hernández Cortés", "Director Técnico", 54, 88.0, 1.72, "Mexicano",
					"JHernández", getClubByIdBreakingReference(7L)),
			new CoachingStaff(10L, "Matias Moises", "Robles Ramírez", "Preparador Físico", 34, 75.0, 1.85, "Mexicano",
					"MRobles", getClubByIdBreakingReference(8L)),
			new CoachingStaff(11L, "Yaroslaf Hugo", "Zamano Juárez", "Médico", 84, 64.0, 1.71, "Mexicano", "YZamano",
					getClubByIdBreakingReference(9L)),
			new CoachingStaff(12L, "Andrea Marlene", "López Barrera", "Auxiliar Técnico", 37, 59.0, 1.77, "Mexicano",
					"ALópez", getClubByIdBreakingReference(10L)));

	public static List<CoachingStaff> COACHING_STAFFS_WITHOUT_ID = Arrays.asList(
			new CoachingStaff(null, "Jorge Humberto", "Torres Mata", "Director Técnico", 59, 85.0, 1.76, "Mexicano",
					"JTorres", getClubByIdBreakingReference(1L)),
			new CoachingStaff(null, "Miguel", "Orozco Flores", "Médico", 67, 86.0, 1.79, "Mexicano", "MOrozco",
					getClubByIdBreakingReference(1L)),
			new CoachingStaff(null, "José", "Islas Rodríguez", "Director Técnico", 37, 80.0, 1.82, "Mexicano", "JIslas",
					getClubByIdBreakingReference(2L)),
			new CoachingStaff(null, "Hugo Omar", "Santacruz Cornejo", "Preparador Físico", 35, 78.0, 1.71, "Mexicano",
					"HSantacruz", getClubByIdBreakingReference(2L)),
			new CoachingStaff(null, "Alberto", "Cortéz Piedradita", "Director Técnico", 50, 71.0, 1.70, "Mexicano",
					"ACortéz", getClubByIdBreakingReference(3L)),
			new CoachingStaff(null, "Mitchelle Katerine", "Pérez Báez", "Médico", 26, 54.0, 1.56, "Mexicano", "MPérez",
					getClubByIdBreakingReference(4L)),
			new CoachingStaff(null, "Ángela", "Rangel Lemus", "Auxiliar Técnico", 42, 42.0, 1.61, "Mexicano", "ÁRangel",
					getClubByIdBreakingReference(5L)),
			new CoachingStaff(null, "Sofia Ivet", "Morales Gónzales", "Médico", 35, 43.0, 1.70, "Mexicano", "SMorales",
					getClubByIdBreakingReference(6L)),
			new CoachingStaff(null, "Juan Pedro", "Hernández Cortés", "Director Técnico", 54, 88.0, 1.72, "Mexicano",
					"JHernández", getClubByIdBreakingReference(7L)),
			new CoachingStaff(null, "Matias Moises", "Robles Ramírez", "Preparador Físico", 34, 75.0, 1.85, "Mexicano",
					"MRobles", getClubByIdBreakingReference(8L)),
			new CoachingStaff(null, "Yaroslaf Hugo", "Zamano Juárez", "Médico", 84, 64.0, 1.71, "Mexicano", "YZamano",
					getClubByIdBreakingReference(9L)),
			new CoachingStaff(null, "Andrea Marlene", "López Barrera", "Auxiliar Técnico", 37, 59.0, 1.77, "Mexicano",
					"ALópez", getClubByIdBreakingReference(10L)));

	public static final CoachingStaff COACHING_STAFF_WITH_ID_LESS_THAN_1 = new CoachingStaff(0L, "Jorge Humberto",
			"Torres Mata", "Director Técnico", 59, 85.0, 1.76, "Mexicano", "JTorres", getClubByIdBreakingReference(1L));

	public static List<CoachingStaff> getCoachingStaffsByClub(Long clubId) {
		return COACHING_STAFFS_WITH_ID.stream().filter(c -> c.getClub().getClubId().equals(clubId))
				.collect(Collectors.toList());
	}

	public static CoachingStaff getCoachingStaffByIdBreakingReference(Long coachingStaffId) {
		CoachingStaff coachingStaff = COACHING_STAFFS_WITH_ID.stream()
				.filter(c -> c.getCoachingStaffId().equals(coachingStaffId)).findFirst().orElseThrow();
		return new CoachingStaff(coachingStaff.getCoachingStaffId(), coachingStaff.getNames(),
				coachingStaff.getLastNames(), coachingStaff.getPosition(), coachingStaff.getAge(),
				coachingStaff.getWeight(), coachingStaff.getHeight(), coachingStaff.getNationality(),
				coachingStaff.getPhoto(), coachingStaff.getClub());
	}

	public static CoachingStaff getCoachingStaffWithoutIdByPositionBreakingReference(int index) {
		CoachingStaff coachingStaff = COACHING_STAFFS_WITHOUT_ID.get(index);
		return new CoachingStaff(coachingStaff.getCoachingStaffId(), coachingStaff.getNames(),
				coachingStaff.getLastNames(), coachingStaff.getPosition(), coachingStaff.getAge(),
				coachingStaff.getWeight(), coachingStaff.getHeight(), coachingStaff.getNationality(),
				coachingStaff.getPhoto(), coachingStaff.getClub());
	}

	// SCOREBOARDS

	public static Scoreboard getScoreboadWithId() {
		return new Scoreboard(1L);
	}

	public static Scoreboard getScoreboadWithId2() {
		return new Scoreboard(2L);
	}

	public static Scoreboard getScoreboadWithoutId() {
		return new Scoreboard(null);
	}

	public static Scoreboard getScoreboadWithIdLessThan1() {
		return new Scoreboard(-1L);
	}

	// LINEUPS

	public static Lineup getLineupWithId() {
		return new Lineup(1L);
	}

	public static Lineup getLineupWithId2() {
		return new Lineup(2L);
	}

	public static Lineup getLineupWithoutId() {
		return new Lineup(null);
	}

	// CLUBESSCOREBOARDS

	// Long clubScoreboardId, String clubStatus, Scoreboard scoreboard, Club club

	public static ClubScoreboard getClubScoreboardWithIdLocal() {
		return new ClubScoreboard(1L, "Local", getScoreboadWithId(), getClubByIdBreakingReference(1L));
	}

	public static ClubScoreboard getClubScoreboardWithIdVisitor() {
		return new ClubScoreboard(2L, "Visitor", getScoreboadWithId(), getClubByIdBreakingReference(2L));
	}

	public static ClubScoreboard getClubScoreboardWithoutId() {
		return new ClubScoreboard(null, "Local", getScoreboadWithId(), getClubByIdBreakingReference(1L));
	}

	public static ClubScoreboard getClubScoreboardWithIdLessThan1() {
		return new ClubScoreboard(-1L, "Local", getScoreboadWithId(), getClubByIdBreakingReference(1L));
	}

	// MATCHES
	// Long matchId, String stadium, String referee, LocalDateTime date, Journey
	// journey, Scoreboard scoreboard

	// 15 journeys
	// Diferentes matchs no pueden tener el mismo Scoreboard, pero para pruebas lo
	// voy a dejar así

	public static final List<Match> MATCHES_WITH_ID = Arrays.asList(
			new Match(1L, "Estadio Azteca", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(1L), getScoreboadWithId()),
			new Match(2L, "Estadio Metropolitano", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(2L), getScoreboadWithId()),
			new Match(3L, "Estadio Santiago Bernabéu", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(3L), getScoreboadWithId()),
			new Match(4L, "Estadio Nou", "El Chiqui Drácula", LocalDateTime.now(), getJourneyByIdBreakingReference(4L),
					getScoreboadWithId()),
			new Match(5L, "Estadio Wembley", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(5L), getScoreboadWithId()),
			new Match(6L, "Estadio Croke Park", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(6L), getScoreboadWithId2()),
			new Match(7L, "Estadio France", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(7L), getScoreboadWithId2()),
			new Match(8L, "Estadio Millennium", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(8L), getScoreboadWithId2()),
			new Match(9L, "Estadio Olimpico de Berlín", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(9L), getScoreboadWithId2()),
			new Match(10L, "Estadio Olimpico de Roma", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(10L), getScoreboadWithId2()));

	public static final List<Match> MATCHES_WITHOUT_ID = Arrays.asList(
			new Match(null, "Estadio Azteca", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(1L), getScoreboadWithId()),
			new Match(null, "Estadio Metropolitano", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(2L), getScoreboadWithId()),
			new Match(null, "Estadio Santiago Bernabéu", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(3L), getScoreboadWithId()),
			new Match(null, "Estadio Nou", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(4L), getScoreboadWithId()),
			new Match(null, "Estadio Wembley", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(5L), getScoreboadWithId()),
			new Match(null, "Estadio Croke Park", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(6L), getScoreboadWithId2()),
			new Match(null, "Estadio France", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(7L), getScoreboadWithId2()),
			new Match(null, "Estadio Millennium", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(8L), getScoreboadWithId2()),
			new Match(null, "Estadio Olimpico de Berlín", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(9L), getScoreboadWithId2()),
			new Match(null, "Estadio Olimpico de Roma", "El Chiqui Drácula", LocalDateTime.now(),
					getJourneyByIdBreakingReference(10L), getScoreboadWithId2()));

	public static final Match MATCH_WITH_ID_LESS_THAN_1 = new Match(0L, "Estadio Azteca", "El Chiqui Drácula",
			LocalDateTime.now(), getJourneyByIdBreakingReference(1L), getScoreboadWithId());

	public static Match getMatchByIdBreakingReference(Long id) {
		Match match = MATCHES_WITH_ID.stream().filter(m -> m.getMatchId().equals(id)).findFirst().orElseThrow();
		return new Match(match.getMatchId(), match.getStadium(), match.getReferee(), match.getDate(),
				match.getJourney(), match.getScoreboard());
	}

	public static Match getMatchWithoutIdByPositionBreakingReference(int index) {
		Match match = MATCHES_WITHOUT_ID.get(index);
		return new Match(match.getMatchId(), match.getStadium(), match.getReferee(), match.getDate(),
				match.getJourney(), match.getScoreboard());
	}

	// LINEUPMATCHES

	public static LineupMatch getLineupMatchLocal() {
		return new LineupMatch(1L, "Local", getMatchByIdBreakingReference(1L), getLineupWithId());
	}

	public static LineupMatch getLineupMatchVisitor() {
		return new LineupMatch(2L, "Visitor", getMatchByIdBreakingReference(1L), getLineupWithId2());
	}

	public static final List<LineupMatch> LINEUPMATCHES = Arrays.asList(getLineupMatchLocal(), getLineupMatchVisitor());

	public static LineupMatch getLineupMatchWithoutId() {
		return new LineupMatch(null, "Local", getMatchByIdBreakingReference(1L), getLineupWithId());
	}

	// CLUBESMATCHES

	public static ClubMatch getClubMatch() {
		return new ClubMatch(1L, getClubByIdBreakingReference(1L), getClubByIdBreakingReference(2L),
				getMatchByIdBreakingReference(1L));
	}

	public static ClubMatch getClubMatchWithoutId() {
		return new ClubMatch(null, getClubByIdBreakingReference(1L), getClubByIdBreakingReference(2L),
				getMatchByIdBreakingReference(1L));
	}

	public static ClubMatch getClubMatchWithIdLessThan1() {
		return new ClubMatch(0L, getClubByIdBreakingReference(1L), getClubByIdBreakingReference(2L),
				getMatchByIdBreakingReference(1L));
	}

	// LINEUPPLAYERS

//	Long lineupPlayerId, String playerStatus, Player player, Lineup lineup

	public static final List<LineupPlayer> LINEUP_PLAYERS_WITH_ID_CLUB_1 = Arrays.asList(
			new LineupPlayer(1L, "Titular", getPlayerByIdBreakingReference(1L), getLineupWithId()),
			new LineupPlayer(2L, "Titular", getPlayerByIdBreakingReference(2L), getLineupWithId()),
			new LineupPlayer(3L, "Titular", getPlayerByIdBreakingReference(3L), getLineupWithId()),
			new LineupPlayer(4L, "Titular", getPlayerByIdBreakingReference(13L), getLineupWithId()),
			new LineupPlayer(5L, "Titular", getPlayerByIdBreakingReference(14L), getLineupWithId()),
			new LineupPlayer(6L, "Titular", getPlayerByIdBreakingReference(15L), getLineupWithId()),
			new LineupPlayer(7L, "Titular", getPlayerByIdBreakingReference(16L), getLineupWithId()),
			new LineupPlayer(8L, "Titular", getPlayerByIdBreakingReference(17L), getLineupWithId()),
			new LineupPlayer(9L, "Titular", getPlayerByIdBreakingReference(18L), getLineupWithId()),
			new LineupPlayer(10L, "Titular", getPlayerByIdBreakingReference(19L), getLineupWithId()),
			new LineupPlayer(11L, "Titular", getPlayerByIdBreakingReference(20L), getLineupWithId()));

	public static final List<LineupPlayer> LINEUP_PLAYERS_WITH_ID_CLUB_2 = Arrays.asList(
			new LineupPlayer(12L, "Titular", getPlayerByIdBreakingReference(4L), getLineupWithId2()),
			new LineupPlayer(13L, "Titular", getPlayerByIdBreakingReference(5L), getLineupWithId2()),
			new LineupPlayer(14L, "Titular", getPlayerByIdBreakingReference(6L), getLineupWithId2()),
			new LineupPlayer(15L, "Titular", getPlayerByIdBreakingReference(21L), getLineupWithId2()),
			new LineupPlayer(16L, "Titular", getPlayerByIdBreakingReference(22L), getLineupWithId2()),
			new LineupPlayer(17L, "Titular", getPlayerByIdBreakingReference(23L), getLineupWithId2()),
			new LineupPlayer(18L, "Titular", getPlayerByIdBreakingReference(24L), getLineupWithId2()),
			new LineupPlayer(19L, "Titular", getPlayerByIdBreakingReference(25L), getLineupWithId2()),
			new LineupPlayer(20L, "Titular", getPlayerByIdBreakingReference(26L), getLineupWithId2()),
			new LineupPlayer(21L, "Titular", getPlayerByIdBreakingReference(27L), getLineupWithId2()),
			new LineupPlayer(22L, "Titular", getPlayerByIdBreakingReference(28L), getLineupWithId2()));

	public static final List<LineupPlayer> LINEUP_PLAYERS_WITHOUT_ID_CLUB_1 = Arrays.asList(
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(1L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(2L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(3L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(13L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(14L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(15L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(16L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(17L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(18L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(19L), getLineupWithId()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(20L), getLineupWithId()));

	public static final List<LineupPlayer> LINEUP_PLAYERS_WITHOUT_ID_CLUB_2 = Arrays.asList(
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(4L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(5L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(6L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(21L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(22L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(23L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(24L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(25L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(26L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(27L), getLineupWithId2()),
			new LineupPlayer(null, "Titular", getPlayerByIdBreakingReference(28L), getLineupWithId2()));

	public static List<LineupPlayer> LINEUP_PLAYERS_MATCH() {
		List<LineupPlayer> lineupPlayers = new ArrayList<>();
		LINEUP_PLAYERS_WITH_ID_CLUB_1.forEach(lp -> lineupPlayers.add(lp));
		LINEUP_PLAYERS_WITH_ID_CLUB_2.forEach(lp -> lineupPlayers.add(lp));
		return lineupPlayers;
	}

	//Mandar a llamar al método de arriba para obtener todos los lineups player en vez de solo los del club 1 
	public static LineupPlayer getLineupPlayerByIdBreakingReference(Long id) {
//		LineupPlayer lineupPlayer = LINEUP_PLAYERS_WITH_ID_CLUB_1
//										.stream()
//										.filter(l -> l.getLineupPlayerId().equals(id))
//										.findFirst().orElseThrow(() -> new InformationNotFoundException(""));
		LineupPlayer lineupPlayer = LINEUP_PLAYERS_MATCH()
				.stream()
				.filter(l -> l.getLineupPlayerId().equals(id))
				.findFirst().orElseThrow(() -> new InformationNotFoundException(""));
		
		return new LineupPlayer(lineupPlayer.getLineupPlayerId(), lineupPlayer.getPlayerStatus(), lineupPlayer.getPlayer(), lineupPlayer.getLineup());
	}

	// LINEUPCOACHINGSTAFFS
	// Long lineupCoachingStaffId, CoachingStaff coachingStaff, Lineup lineup 	Club 1: 1, 2 Club 2: 3, 4

	public static final List<LineupCoachingStaff> LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1 = Arrays.asList(
			new LineupCoachingStaff(1L, getCoachingStaffByIdBreakingReference(1L), getLineupWithId()),
			new LineupCoachingStaff(2L, getCoachingStaffByIdBreakingReference(2L), getLineupWithId()));

	public static final List<LineupCoachingStaff> LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2 = Arrays.asList(
			new LineupCoachingStaff(3L, getCoachingStaffByIdBreakingReference(3L), getLineupWithId2()),
			new LineupCoachingStaff(4L, getCoachingStaffByIdBreakingReference(4L), getLineupWithId2()));

	public static final List<LineupCoachingStaff> LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1 = Arrays.asList(
			new LineupCoachingStaff(null, getCoachingStaffByIdBreakingReference(1L), getLineupWithId()),
			new LineupCoachingStaff(null, getCoachingStaffByIdBreakingReference(2L), getLineupWithId()));

	public static final List<LineupCoachingStaff> LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_2 = Arrays.asList(
			new LineupCoachingStaff(null, getCoachingStaffByIdBreakingReference(3L), getLineupWithId2()),
			new LineupCoachingStaff(null, getCoachingStaffByIdBreakingReference(4L), getLineupWithId2()));
	
	public static List<LineupCoachingStaff> LINEUP_COACHING_STAFFS_MATCH(){
		List<LineupCoachingStaff> lineupCoachingStaffs = new ArrayList<>();
		LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1.forEach(lc -> lineupCoachingStaffs.add(lc));
		LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2.forEach(lc -> lineupCoachingStaffs.add(lc));
		return lineupCoachingStaffs;
	}
	
	public static LineupCoachingStaff getLineupCoachingStaffByIdBreakingReference(Long id) {
		LineupCoachingStaff lineupCoachingStaff = LINEUP_COACHING_STAFFS_MATCH()
													.stream()
													.filter(lc -> lc.getLineupCoachingStaffId().equals(id))
													.findFirst()
													.orElseThrow(() -> new InformationNotFoundException(""));
		return new LineupCoachingStaff(lineupCoachingStaff.getLineupCoachingStaffId(), lineupCoachingStaff.getCoachingStaff(), lineupCoachingStaff.getLineup());
	}

	// GOALS

	// Long goalId, Integer minute, Player player, Scoreboard scoreboard

	// Player Club 1: 1, 2, 3, 13-20
	// Player Club 2: 4, 5, 6, 21-28

	public static final List<Goal> GOALS_WITH_ID_MATCH_1 = Arrays.asList(
			new Goal(1L, 45, getPlayerByIdBreakingReference(1L), getScoreboadWithId()),
			new Goal(2L, 53, getPlayerByIdBreakingReference(13L), getScoreboadWithId()),
			new Goal(3L, 59, getPlayerByIdBreakingReference(20L), getScoreboadWithId()),
			new Goal(4L, 75, getPlayerByIdBreakingReference(16L), getScoreboadWithId()),
			new Goal(5L, 82, getPlayerByIdBreakingReference(4L), getScoreboadWithId()),
			new Goal(6L, 87, getPlayerByIdBreakingReference(6L), getScoreboadWithId()),
			new Goal(7L, 90, getPlayerByIdBreakingReference(23L), getScoreboadWithId()));

	public static final List<Goal> GOALS_WITHOUT_ID_MATCH_1 = Arrays.asList(
			new Goal(null, 45, getPlayerByIdBreakingReference(1L), null),
			new Goal(null, 53, getPlayerByIdBreakingReference(13L), null),
			new Goal(null, 59, getPlayerByIdBreakingReference(20L), null),
			new Goal(null, 75, getPlayerByIdBreakingReference(16L), null),
			new Goal(null, 82, getPlayerByIdBreakingReference(4L), null),
			new Goal(null, 87, getPlayerByIdBreakingReference(6L), null),
			new Goal(null, 90, getPlayerByIdBreakingReference(23L), null));

	public static final Goal GOAL_WITH_ID_LESS_THAN_1 = new Goal(0L, 45, getPlayerByIdBreakingReference(1L),
			getScoreboadWithId());

	public static final Goal GOAL_WITH_PLAYER_OUT_OF_MATCH = new Goal(null, 45, getPlayerByIdBreakingReference(8L),
			null);

	public static Goal getGoalByIdBreakingReference(Long id) {
		Goal goal = GOALS_WITH_ID_MATCH_1.stream().filter(g -> g.getGoalId().equals(id)).findFirst().orElseThrow();
		return new Goal(goal.getGoalId(), goal.getMinute(), goal.getPlayer(), goal.getScoreboard());
	}

	public static Goal getGoalWithoutIdByPositionBreakingReference(int index) {
		Goal goal = GOALS_WITHOUT_ID_MATCH_1.get(index);
		return new Goal(goal.getGoalId(), goal.getMinute(), goal.getPlayer(), goal.getScoreboard());
	}

	// CARDS
	// Long cardId, Integer minute, String color, String photo, Player player, Match
	// match

	public static final List<Card> CARDS_WITH_ID_MATCH_1 = Arrays.asList(
			new Card(1L, 20, "Red", "Red", getPlayerByIdBreakingReference(1L), getMatchByIdBreakingReference(1L)),
			new Card(2L, 22, "Yellow", "Yellow", getPlayerByIdBreakingReference(2L), getMatchByIdBreakingReference(1L)),
			new Card(3L, 27, "Red", "Red", getPlayerByIdBreakingReference(3L), getMatchByIdBreakingReference(1L)),
			new Card(4L, 45, "Yellow", "Yellow", getPlayerByIdBreakingReference(4L), getMatchByIdBreakingReference(1L)),
			new Card(5L, 84, "Yellow", "Yellow", getPlayerByIdBreakingReference(5L), getMatchByIdBreakingReference(1L)),
			new Card(6L, 90, "Yellow", "Yellow", getPlayerByIdBreakingReference(6L),
					getMatchByIdBreakingReference(1L)));

	public static final List<Card> CARDS_WITHOUT_ID_MATCH_1 = Arrays.asList(
			new Card(null, 20, "Red", "Red", getPlayerByIdBreakingReference(1L), null),
			new Card(null, 22, "Yellow", "Yellow", getPlayerByIdBreakingReference(2L), null),
			new Card(null, 27, "Red", "Red", getPlayerByIdBreakingReference(3L), null),
			new Card(null, 45, "Yellow", "Yellow", getPlayerByIdBreakingReference(4L), null),
			new Card(null, 84, "Yellow", "Yellow", getPlayerByIdBreakingReference(5L), null),
			new Card(null, 90, "Yellow", "Yellow", getPlayerByIdBreakingReference(6L), null));

	public static final Card CARD_WITH_ID_LESS_THAN_1 = new Card(0L, 20, "Red", "Red",
			getPlayerByIdBreakingReference(1L), null);

	public static final Card CARD_WITH_PLAYER_OUT_OF_MATCH = new Card(null, 20, "Red", "Red",
			getPlayerByIdBreakingReference(8L), null);

	public static Card getCardByIdBreakingReference(Long id) {
		Card card = CARDS_WITH_ID_MATCH_1.stream().filter(c -> c.getCardId().equals(id)).findFirst().orElseThrow();
		return new Card(card.getCardId(), card.getMinute(), card.getColor(), card.getPhoto(), card.getPlayer(),
				card.getMatch());
	}

	public static Card getCardWithoutIdByPositionBreakingReference(int index) {
		Card card = CARDS_WITHOUT_ID_MATCH_1.get(index);
		return new Card(card.getCardId(), card.getMinute(), card.getColor(), card.getPhoto(), card.getPlayer(),
				card.getMatch());
	}

	// CHANGES

//	Long changeId, Integer minute, Player playerIn, Player playerOut, Lineup lineup, Match match

	public static final List<Change> CHANGES_WITH_ID_MATCH_1 = Arrays.asList(
			new Change(1L, 20, getPlayerByIdBreakingReference(1L), getPlayerByIdBreakingReference(2L),
					getLineupWithId(), getMatchByIdBreakingReference(1L)),
			new Change(2L, 48, getPlayerByIdBreakingReference(13L), getPlayerByIdBreakingReference(20L),
					getLineupWithId(), getMatchByIdBreakingReference(1L)),
			new Change(3L, 60, getPlayerByIdBreakingReference(15L), getPlayerByIdBreakingReference(18L),
					getLineupWithId(), getMatchByIdBreakingReference(1L)),
			new Change(4L, 75, getPlayerByIdBreakingReference(4L), getPlayerByIdBreakingReference(5L),
					getLineupWithId2(), getMatchByIdBreakingReference(1L)),
			new Change(5L, 79, getPlayerByIdBreakingReference(21L), getPlayerByIdBreakingReference(28L),
					getLineupWithId2(), getMatchByIdBreakingReference(1L)),
			new Change(6L, 83, getPlayerByIdBreakingReference(23L), getPlayerByIdBreakingReference(26L),
					getLineupWithId2(), getMatchByIdBreakingReference(1L)));

	public static final List<Change> CHANGES_WITHOUT_ID_MATCH_1 = Arrays.asList(
			new Change(null, 20, getPlayerByIdBreakingReference(1L), getPlayerByIdBreakingReference(2L), null, null),
			new Change(null, 48, getPlayerByIdBreakingReference(13L), getPlayerByIdBreakingReference(20L), null, null),
			new Change(null, 60, getPlayerByIdBreakingReference(15L), getPlayerByIdBreakingReference(18L), null, null),
			new Change(null, 75, getPlayerByIdBreakingReference(4L), getPlayerByIdBreakingReference(5L), null, null),
			new Change(null, 79, getPlayerByIdBreakingReference(21L), getPlayerByIdBreakingReference(28L), null, null),
			new Change(null, 83, getPlayerByIdBreakingReference(23L), getPlayerByIdBreakingReference(26L), null, null));

	public static final Change CHANGE_WITH_PLAYER_IN_OUT_OF_MATCH = new Change(1L, 20,
			getPlayerByIdBreakingReference(8L), getPlayerByIdBreakingReference(2L), null, null);

	public static final Change CHANGE_WITH_PLAYER_OUT_OUT_OF_MATCH = new Change(1L, 20,
			getPlayerByIdBreakingReference(1L), getPlayerByIdBreakingReference(8L), null, null);

	public static final Change CHANGE_WITH_SAME_PLAYERS_MATCH = new Change(1L, 20, getPlayerByIdBreakingReference(1L),
			getPlayerByIdBreakingReference(1L), null, null);

	public static Change getChangeByIdBreakingReference(Long id) {
		Change change = CHANGES_WITH_ID_MATCH_1.stream().filter(c -> c.getChangeId().equals(id)).findFirst()
				.orElseThrow();
		return new Change(change.getChangeId(), change.getMinute(), change.getPlayerIn(), change.getPlayerOut(),
				change.getLineup(), change.getMatch());
	}

	public static Change getChangeWithoutIdByPositionBreakingReference(int index) {
		Change change = CHANGES_WITHOUT_ID_MATCH_1.get(index);
		return new Change(change.getChangeId(), change.getMinute(), change.getPlayerIn(), change.getPlayerOut(),
				change.getLineup(), change.getMatch());
	}
}
