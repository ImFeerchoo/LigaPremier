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

import com.fernandobetancourt.exceptions.AddingCardException;
import com.fernandobetancourt.exceptions.CardNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.model.dao.ICardsDao;
import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.CardValidator;

@SpringBootTest(classes = {CardsServiceImpl.class, CardValidator.class})
class CardsServiceImplTest {

	@MockBean
	private ICardsDao cardsDao;

	@MockBean
	private IPlayersService playersService;

	@MockBean
	private IMatchesService matchesService;

	@MockBean
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private ICardsService cardsService;

	// GET

	@Test
	void testGetCardSuccessed() {
		// Given
		when(cardsDao.findById(anyLong()))
				.then(invocation -> Optional.of(getCardByIdBreakingReference(invocation.getArgument(0))));

		// When
		Card card = cardsService.getCard(1L);

		// Then
		assertEquals(getCardByIdBreakingReference(1L), card);
		verify(cardsDao).findById(1L);
	}

	@Test
	void testGetCardFailed() {
		// Given
		when(cardsDao.findById(anyLong())).thenReturn(Optional.empty());

		// Then
		assertThrows(CardNotFoundException.class, () -> {
			// When
			cardsService.getCard(1L);
		});

		verify(cardsDao).findById(1L);
	}

	@Test
	void testGetCardsByMatchSuccessed() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(cardsDao.findByMatch(any(Match.class))).thenReturn(CARDS_WITH_ID_MATCH_1);

		// When
		List<Card> cards = cardsService.getCardsByMatch(1L);

		// Then
		assertEquals(CARDS_WITH_ID_MATCH_1, cards);
		verify(matchesService).getMatch(1L);
		verify(cardsDao).findByMatch(any(Match.class));
	}

	@Test
	void testGetCardsByMatchEmptyList() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(cardsDao.findByMatch(any(Match.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(CardNotFoundException.class, () -> {
			// When
			cardsService.getCardsByMatch(1L);
		});

		verify(matchesService).getMatch(1L);
		verify(cardsDao).findByMatch(any(Match.class));
	}

	// POST

	@Test
	void testAddCardSuccessed() {
		// Given
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		when(cardsDao.save(any(Card.class))).then(invocation -> {
			Card card = invocation.getArgument(0);
			card.setCardId(1L);
			return card;
		});
		
		// When
		Card cardSaved = cardsService.addCard(getCardWithoutIdByPositionBreakingReference(0), 1L);

		// Then
		assertEquals(getCardByIdBreakingReference(1L), cardSaved);
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService).getMatch(anyLong());
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(cardsDao).save(any(Card.class));
	}

	@Test
	void testAddCardFailedCardNull() {
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(null, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedMinuteNull() {
		//Given
		Card cardMinuteNull = getCardWithoutIdByPositionBreakingReference(0);
		cardMinuteNull.setMinute(null);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardMinuteNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedMinuteLessThan0() {
		//Given
		Card cardMinuteLessThan0 = getCardWithoutIdByPositionBreakingReference(0);
		cardMinuteLessThan0.setMinute(-1);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardMinuteLessThan0, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedColorNull() {
		//Given
		Card cardColorNull = getCardWithoutIdByPositionBreakingReference(0);
		cardColorNull.setColor(null);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardColorNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedColorEmptyString() {
		//Given
		Card cardColorEmptyString = getCardWithoutIdByPositionBreakingReference(0);
		cardColorEmptyString.setColor("");
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardColorEmptyString, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedPlayerNull() {
		//Given
		Card cardPlayerNull = getCardWithoutIdByPositionBreakingReference(0);
		cardPlayerNull.setPlayer(null);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardPlayerNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedPlayerIdNull() {
		//Given
		Card cardPlayerIdNull = getCardWithoutIdByPositionBreakingReference(0);
		cardPlayerIdNull.setPlayer(getPlayerWithoutIdByPositionBreakingReference(0));
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardPlayerIdNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedPlayerIdLessThan1() {
		//Given
		Card cardPlayerIdLessThan1 = getCardWithoutIdByPositionBreakingReference(0);
		cardPlayerIdLessThan1.setPlayer(PLAYER_WITH_ID_LESS_THAN_1);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(cardPlayerIdLessThan1, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedMatchIdNull() {
		//Given
		Card card = getCardWithoutIdByPositionBreakingReference(0);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(card, null);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedMatchIdLessThan1() {
		//Given
		Card card = getCardWithoutIdByPositionBreakingReference(0);
		
		//Then
		assertThrows(AddingCardException.class, () -> {
			//When
			cardsService.addCard(card, 0L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedPlayerDoesNotExists() {
		//Given
		when(playersService.getPlayerById(anyLong())).thenThrow(PlayerNotFoundException.class);
		Card card = getCardWithoutIdByPositionBreakingReference(0);
		
		//Then
		assertThrows(PlayerNotFoundException.class, () -> {
			//When
			cardsService.addCard(card, 1L);
		});
		
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedMatchDoesNotExists() {
		//Given
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).thenThrow(MatchNotFoundException.class);
		Card card = getCardWithoutIdByPositionBreakingReference(0);
		
		//Then
		assertThrows(MatchNotFoundException.class, () -> {
			//When
			cardsService.addCard(card, 1L);
		});
		
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}

	@Test
	void testAddCardFailedPlayerDoesNotBelongToMatch() {
		// Given
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		// Then
		assertThrows(AddingCardException.class, () -> {
			// When
			cardsService.addCard(CARD_WITH_PLAYER_OUT_OF_MATCH, 1L);
		});

		verify(playersService).getPlayerById(anyLong());
		verify(matchesService).getMatch(anyLong());
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(cardsDao, never()).save(any(Card.class));
	}
	

	// PUT

//	@Test
//	void testUpdateCard() {
//	}

	// DELETE

	@Test
	void testDeleteCardSuccessed() {
		//Given
		when(cardsDao.findById(anyLong())).then(invocation -> Optional.of(getCardByIdBreakingReference(invocation.getArgument(0))));
		
		//When
		Card card = cardsService.deleteCard(1L);
		
		//Then
		assertEquals(getCardByIdBreakingReference(1L), card);
		verify(cardsDao).findById(1L);
		verify(cardsDao).deleteById(1L);
	}
	
	@Test
	void testDeleteCardFailed() {
		//Given
		when(cardsDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(CardNotFoundException.class, () -> {
			//When
			cardsService.deleteCard(1L);
		});
		
		verify(cardsDao).findById(1L);
		verify(cardsDao, never()).deleteById(1L);
	}

}
