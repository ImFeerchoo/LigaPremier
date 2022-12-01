package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingPlayerException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.model.dao.IPlayersDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Player;

@SpringBootTest
class PlayersServiceImplTest {

	@MockBean
	private IPlayersDao playersDao;

	@MockBean
	private IClubesService clubesService;

	@Autowired
	private IPlayersService playersService;

	// GET

	@Test
	void testGetPlayerByIdSuccessed() {
		// Given
		when(playersDao.findById(anyLong())).thenReturn(Optional.of(getPlayerByIdBreakingReference(1L)));

		// When
		Player player = playersService.getPlayerById(1L);

		// Then
		assertEquals(getPlayerByIdBreakingReference(1L), player);
		verify(playersDao).findById(1L);
	}

	@Test
	void testGetPlayerByIdFailed() {
		// Given
		when(playersDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(PlayerNotFoundException.class, () -> {
			// When
			playersService.getPlayerById(1L);
		});

		verify(playersDao).findById(1L);

	}

	@Test
	void testGetPlayerByNameSuccessed() {
		// Given
		when(playersDao.findByNames(anyString())).thenReturn(Optional.of(getPlayerByName("José Rafael")));

		// When
		Player player = playersService.getPlayerByName("José Rafael");

		// Then
		assertEquals(getPlayerByIdBreakingReference(1L), player);

		verify(playersDao).findByNames("José Rafael");
	}

	@Test
	void testGetPlayerByNameFailed() {
		// Given
		when(playersDao.findByNames(anyString())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(PlayerNotFoundException.class, () -> {
			// When
			playersService.getPlayerByName("José Rafael");
		});

		verify(playersDao).findByNames("José Rafael");
	}

	@Test
	void testGetAllPlayersSuccessed() {
		// Given
		when(playersDao.findAll()).thenReturn(PLAYERS_WITH_ID);

		// When
		List<Player> players = playersService.getAllPlayers();

		// Then
		assertFalse(players.isEmpty());
		assertEquals(29, players.size());
		assertTrue(players.contains(getPlayerByIdBreakingReference(1L)));
		assertTrue(players.contains(getPlayerByIdBreakingReference(12L)));

		verify(playersDao).findAll();
	}

//	@Test
//	void testGetAllPlayersEmptyList() {
//		// Given
//		when(playersDao.findAll()).thenReturn(Collections.emptyList());
//
//		// When
//		List<Player> players = playersService.getAllPlayers();
//
//		// Then
//		assertTrue(players.isEmpty());
//		verify(playersDao).findAll();
//	}
	
	@Test
	void testGetAllPlayersEmptyList() {
		// Given
		when(playersDao.findAll()).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(PlayerNotFoundException.class, () -> {
			// When
			playersService.getAllPlayers();
		});
		
		verify(playersDao).findAll();
	}

	@Test
	void testGetPlayersByClubSuccessed() {
		// Given
		when(clubesService.getClubById(anyLong())).thenReturn(getClubByIdBreakingReference(1L));
		when(playersDao.findByClub(any(Club.class))).thenReturn(getPlayersByClub(1L));

		// When
		List<Player> players = playersService.getPlayersByClub(1L);

		// Then
		assertFalse(players.isEmpty());
		assertEquals(12, players.size());
		assertTrue(players.contains(getPlayerByIdBreakingReference(1L)));
		assertTrue(players.contains(getPlayerByIdBreakingReference(2L)));
		assertTrue(players.contains(getPlayerByIdBreakingReference(3L)));

		verify(clubesService).getClubById(1L);
		verify(playersDao).findByClub(any(Club.class));

	}

	@Test
	void testGetPlayersByClubEmptyList() {
		// Given
		when(clubesService.getClubById(anyLong())).thenReturn(getClubByIdBreakingReference(1L));
		when(playersDao.findByClub(any(Club.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(PlayerNotFoundException.class, () -> {
			// When
			playersService.getPlayersByClub(1L);
		});

		verify(clubesService).getClubById(1L);
		verify(playersDao).findByClub(any(Club.class));
	}
	
	//Lanzo una lista vacía pero no una excepción
//	@Test
//	void testGetPlayersByClubEmptyList() {
//		// Given
//		when(clubesService.getClubById(anyLong())).thenReturn(getClubByIdBreakingReference(1L));
//		when(playersDao.findByClub(any(Club.class))).thenReturn(Collections.emptyList());
//		
//		// When
//		List<Player> players = playersService.getPlayersByClub(1L);
//		
//		// Then
//		assertTrue(players.isEmpty());
//		verify(clubesService).getClubById(1L);
//		verify(playersDao).findByClub(any(Club.class));
//	}

	@Test
	void testGetPlayersByClubClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			playersService.getPlayersByClub(1L);
		});

		verify(clubesService).getClubById(1L);
	}

	// POST

	@Test
	void testAddPlayerSuccessed() {
		// Given
		when(playersDao.save(any(Player.class))).then(invocation -> {
			Player player = invocation.getArgument(0);
			player.setPlayerId(1L);
			return player;
		});

		// When
		Player player = playersService.addPlayer(getPlayerWithoutIdByPositionBreakingReference(0));

		// Then
		assertEquals(1L, player.getPlayerId());
		assertEquals("José Rafael", player.getNames());
		assertEquals("Castrejon Ramos", player.getLastNames());
		assertEquals(1, player.getNumber());
		assertEquals("Portero", player.getPosition());
		assertEquals(21, player.getAge());
		assertEquals(89.0, player.getWeight());
		assertEquals(1.86, player.getHeight());
		assertEquals("Mexicano", player.getNationality());
		assertEquals("JCastrjon", player.getPhoto());
		assertEquals(getClubByIdBreakingReference(1L), player.getClub());

		verify(clubesService).getClubById(1L);
		verify(playersDao).save(any(Player.class));
	}

	@Test
	void testAddPlayerFailedPlayerNull() {
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(null);
		});
	}

	@Test
	void testAddPlayerFailedNamesNull() {
		// Given
		Player playerNameNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerNameNull.setNames(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerNameNull);
		});
	}

	@Test
	void testAddPlayerFailedNamesEmptyString() {
		// Given
		Player playerNameEmptyString = getPlayerWithoutIdByPositionBreakingReference(0);
		playerNameEmptyString.setNames("");

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerNameEmptyString);
		});
	}

	@Test
	void testAddPlayerFailedLastNamesNull() {
		// Given
		Player playerLastNamesNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerLastNamesNull.setLastNames(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerLastNamesNull);
		});
	}

	@Test
	void testAddPlayerFailedLastNamesEmptyString() {
		// Given
		Player playerLastNamesEmptyString = getPlayerWithoutIdByPositionBreakingReference(0);
		playerLastNamesEmptyString.setLastNames("");

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerLastNamesEmptyString);
		});
	}

	@Test
	void testAddPlayerFailedNumberNull() {
		// Given
		Player playerNumberNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerNumberNull.setNumber(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerNumberNull);
		});
	}

	@Test
	void testAddPlayerFailedNumberLessThan1() {
		// Given
		Player playerNumberLessThan1 = getPlayerWithoutIdByPositionBreakingReference(0);
		playerNumberLessThan1.setNumber(0);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerNumberLessThan1);
		});
	}

	@Test
	void testAddPlayerFailedPositionNull() {
		// Given
		Player playerPositionNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerPositionNull.setPosition(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerPositionNull);
		});
	}

	@Test
	void testAddPlayerFailedPositionEmptyString() {
		// Given
		Player playerPositionEmptyString = getPlayerWithoutIdByPositionBreakingReference(0);
		playerPositionEmptyString.setPosition("");

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerPositionEmptyString);
		});
	}

	@Test
	void testAddPlayerFailedAgeNull() {
		// Given
		Player playerAgeNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerAgeNull.setAge(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerAgeNull);
		});
	}

	@Test
	void testAddPlayerFailedAgeLessThan1() {
		// Given
		Player playerLessThan1 = getPlayerWithoutIdByPositionBreakingReference(0);
		playerLessThan1.setAge(0);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerLessThan1);
		});
	}

	@Test
	void testAddPlayerFailedWeightNull() {
		// Given
		Player playerWeightNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerWeightNull.setWeight(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerWeightNull);
		});
	}

	@Test
	void testAddPlayerFailedWeightLessThan1() {
		// Given
		Player playerWeightLessThan1 = getPlayerWithoutIdByPositionBreakingReference(0);
		playerWeightLessThan1.setWeight(0.0);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerWeightLessThan1);
		});
	}

	@Test
	void testAddPlayerFailedHeightNull() {
		// Given
		Player playerHeightNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerHeightNull.setHeight(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerHeightNull);
		});
	}

	@Test
	void testAddPlayerFailedHeightLessThan1() {
		// Given
		Player playerHeightLessThan1 = getPlayerWithoutIdByPositionBreakingReference(0);
		playerHeightLessThan1.setHeight(0.0);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerHeightLessThan1);
		});
	}

	@Test
	void testAddPlayerFailedNationalityNull() {
		// Given
		Player playerNationalityNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerNationalityNull.setNationality(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerNationalityNull);
		});
	}

	@Test
	void testAddPlayerFailedNationalityEmptyString() {
		// Given
		Player playerNationalityEmptyString = getPlayerWithoutIdByPositionBreakingReference(0);
		playerNationalityEmptyString.setNationality("");

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerNationalityEmptyString);
		});
	}

	@Test
	void testAddPlayerFailedClubNull() {
		// Given
		Player playerClubNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerClubNull.setClub(null);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerClubNull);
		});
	}

	@Test
	void testAddPlayerFailedClubIdNull() {
		// Given
		Player playerClubIdNull = getPlayerWithoutIdByPositionBreakingReference(0);
		playerClubIdNull.setClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerClubIdNull);
		});
	}

	@Test
	void testAddPlayerFailedClubIdLessThan1() {
		// Given
		Player playerClubIdLessThan1 = getPlayerWithoutIdByPositionBreakingReference(0);
		playerClubIdLessThan1.setClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.addPlayer(playerClubIdLessThan1);
		});
	}

	@Test
	void testAddPlayerFailedClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			playersService.addPlayer(getPlayerWithoutIdByPositionBreakingReference(0));
		});

		verify(clubesService).getClubById(anyLong());
	}

	// PUT

	@Test
	void testUpdatePlayerSuccessed() {
		// Given
		when(playersDao.findById(anyLong())).thenReturn(Optional.of(getPlayerByIdBreakingReference(1L)));

		when(playersDao.save(any(Player.class))).then(invocation -> {
			return invocation.getArgument(0);
		});

		Player player = getPlayerByIdBreakingReference(1L);
		player.setNames("José Juan");
		player.setLastNames("Pérez García");
		player.setNumber(10000);
		player.setPosition("Recogedor");
		player.setAge(100);
		player.setWeight(100.0);
		player.setHeight(2.23);
		player.setNationality("Canadiense");
		player.setPhoto("JPérez");
		player.setClub(getClubByIdBreakingReference(10L));

		// When
		player = playersService.updatePlayer(player);

		// Then
		assertEquals("José Juan", player.getNames());
		assertEquals("Pérez García", player.getLastNames());
		assertEquals(10000, player.getNumber());
		assertEquals("Recogedor", player.getPosition());
		assertEquals(100, player.getAge());
		assertEquals(100.0, player.getWeight());
		assertEquals(2.23, player.getHeight());
		assertEquals("Canadiense", player.getNationality());
		assertEquals("JPérez", player.getPhoto());
		assertEquals(getClubByIdBreakingReference(10L), player.getClub());

		verify(playersDao).findById(1L);
		verify(clubesService).getClubById(10L);
		verify(playersDao).save(any(Player.class));
	}

	@Test
	void testUpdatePlayerFailedPlayerNull() {
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(null);
		});
	}

	@Test
	void testUpdatePlayerFailedPlayerIdNull() {
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(getPlayerWithoutIdByPositionBreakingReference(0));
		});
	}

	@Test
	void testUpdatePlayerFailedPlayerIdLessThan1() {
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(PLAYER_WITH_ID_LESS_THAN_1);
		});
	}

	@Test
	void testUpdatePlayerFailedNamesNull() {
		//Given
		Player playerNamesNull = getPlayerByIdBreakingReference(1L);
		playerNamesNull.setNames(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerNamesNull);
		});
	}

	@Test
	void testUpdatePlayerFailedNamesEmptyString() {
		//Given
		Player playerNamesEmptyString = getPlayerByIdBreakingReference(1L);
		playerNamesEmptyString.setNames("");
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerNamesEmptyString);
		});
	}

	@Test
	void testUpdatePlayerFailedLastNamesNull() {
		//Given
		Player playerLastNamesNull = getPlayerByIdBreakingReference(1L);
		playerLastNamesNull.setLastNames(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerLastNamesNull);
		});
	}

	@Test
	void testUpdatePlayerFailedLastNamesEmptyString() {
		//Given
		Player playerLastNamesEmptyString = getPlayerByIdBreakingReference(1L);
		playerLastNamesEmptyString.setLastNames("");
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerLastNamesEmptyString);
		});
	}

	@Test
	void testUpdatePlayerFailedNumberNull() {
		//Given
		Player playerNumberNull = getPlayerByIdBreakingReference(1L);
		playerNumberNull.setNumber(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerNumberNull);
		});
	}

	@Test
	void testUpdatePlayerFailedNumberLessThan1() {
		//Given
		Player playerNumberLessThan1 = getPlayerByIdBreakingReference(1L);
		playerNumberLessThan1.setNumber(0);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerNumberLessThan1);
		});
	}

	@Test
	void testUpdatePlayerFailedPositionNull() {
		//Given
		Player playerPositionNull = getPlayerByIdBreakingReference(1L);
		playerPositionNull.setPosition(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerPositionNull);
		});
	}

	@Test
	void testUpdatePlayerFailedPositionEmptyString() {
		//Given
		Player playerPositionEmptyString = getPlayerByIdBreakingReference(1L);
		playerPositionEmptyString.setPosition("");
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerPositionEmptyString);
		});
	}

	@Test
	void testUpdatePlayerFailedAgeNull() {
		//Given
		Player playerAgeNull = getPlayerByIdBreakingReference(1L);
		playerAgeNull.setAge(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerAgeNull);
		});
	}

	@Test
	void testUpdatePlayerFailedAgeLessThan1() {
		//Given
		Player playerAgeLessThan1 = getPlayerByIdBreakingReference(1L);
		playerAgeLessThan1.setAge(0);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerAgeLessThan1);
		});
	}

	@Test
	void testUpdatePlayerFailedWeightNull() {
		//Given
		Player playerWeightNull = getPlayerByIdBreakingReference(1L);
		playerWeightNull.setWeight(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerWeightNull);
		});
	}

	@Test
	void testUpdatePlayerFailedWeightLessThan1() {
		//Given
		Player playerWeightLessThan1 = getPlayerByIdBreakingReference(1L);
		playerWeightLessThan1.setWeight(0.0);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerWeightLessThan1);
		});
	}

	@Test
	void testUpdatePlayerFailedHeightNull() {
		//Given
		Player playerHeightNull = getPlayerByIdBreakingReference(1L);
		playerHeightNull.setHeight(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerHeightNull);
		});
	}

	@Test
	void testUpdatePlayerFailedHeightLessThan1() {
		//Given
		Player playerHeightLessThan1 = getPlayerByIdBreakingReference(1L);
		playerHeightLessThan1.setHeight(0.0);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerHeightLessThan1);
		});
	}

	@Test
	void testUpdatePlayerFailedNationalityNull() {
		//Given
		Player playerNationalityNull = getPlayerByIdBreakingReference(1L);
		playerNationalityNull.setNationality(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerNationalityNull);
		});
	}

	@Test
	void testUpdatePlayerFailedNationalityEmptyString() {
		//Given
		Player playerNationalityEmptyString = getPlayerByIdBreakingReference(1L);
		playerNationalityEmptyString.setNationality("");
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerNationalityEmptyString);
		});
	}

	@Test
	void testUpdatePlayerFailedClubNull() {
		//Given
		Player playerClubNull = getPlayerByIdBreakingReference(1L);
		playerClubNull.setClub(null);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerClubNull);
		});
	}

	@Test
	void testUpdatePlayerFailedClubIdNull() {
		//Given
		Player playerClubIdNull = getPlayerByIdBreakingReference(1L);
		playerClubIdNull.setClub(getClubWithoutIdByPositionBreakingReference(0));
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerClubIdNull);
		});
	}

	@Test
	void testUpdatePlayerFailedClubIdLessThan1() {
		//Given
		Player playerClubIdLessThan1 = getPlayerByIdBreakingReference(1L);
		playerClubIdLessThan1.setClub(CLUB_WITH_ID_LESS_THAN_1);
		
		// Then
		assertThrows(AddingPlayerException.class, () -> {
			// When
			playersService.updatePlayer(playerClubIdLessThan1);
		});
	}

	@Test
	void testUpdatePlayerFailedClubDoesNotExists() {
		//Given
		when(playersDao.findById(anyLong())).thenReturn(Optional.of(getPlayerByIdBreakingReference(1L)));
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);
		
		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			playersService.updatePlayer(getPlayerByIdBreakingReference(1L));
		});
		
		verify(playersDao).findById(anyLong());
		verify(clubesService).getClubById(1L);
	}

	@Test
	void testUpdatePlayerFailedPlayerDoesNotExists() {
		//Given
		when(playersDao.findById(anyLong())).thenThrow(PlayerNotFoundException.class);
		
		// Then
		assertThrows(PlayerNotFoundException.class, () -> {
			// When
			playersService.updatePlayer(getPlayerByIdBreakingReference(1L));
		});
		
		verify(playersDao).findById(anyLong());
	}

	// DELETE

	@Test
	void testDeletePlayerSuccessed() {
		//Given
		when(playersDao.findById(anyLong())).thenReturn(Optional.of(getPlayerByIdBreakingReference(1L)));
		
		//When
		Player player = playersService.deletePlayer(1L);
		
		//Then
		assertEquals(getPlayerByIdBreakingReference(1L), player);
		
		verify(playersDao).findById(1L);
		verify(playersDao).deleteById(1L);
	}
	
	@Test
	void testDeletePlayerFailed() {
		//Given
		when(playersDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		//Then
		assertThrows(PlayerNotFoundException.class, () -> {
			//When
			playersService.deletePlayer(1L);
		});
		
		verify(playersDao).findById(1L);
	}

}
