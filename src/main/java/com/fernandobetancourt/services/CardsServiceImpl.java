package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.CardNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ICardsDao;
import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.CardValidator;

@Service
public class CardsServiceImpl implements ICardsService {
	
	@Autowired
	private ICardsDao cardsDao;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private CardValidator cardValidator;
	
	//GET

	@Override
	public Card getCard(Long id) throws InformationNotFoundException {
		return cardValidator.cardExists(id);
	}

//	@Override
//	public List<Card> getCardsByMatch(Long matchId) throws InformationNotFoundException {
//		Match matchRecovered = this.matchesService.getMatch(matchId);
//		return this.cardsDao.findByMatch(matchRecovered).orElseThrow(() -> {
//			throw new CardNotFoundException("This match doesn't have cards");
//		});
//	}
	
	@Override
	public List<Card> getCardsByMatch(Long matchId) throws InformationNotFoundException {
		Match matchRecovered = this.matchesService.getMatch(matchId);
		List<Card> cards = cardsDao.findByMatch(matchRecovered);
		if(cards.isEmpty()) throw new CardNotFoundException("This match doesn't have cards");
		return cards;
	}
	
	//POST
	
	//Mejor le quiero pasar el matchId en vez de pasarle el Match dentro del Card

//	@Override
//	public Card addCard(Card card) throws InformationNotFoundException, WritingInformationException {
//		this.isCardValidToSave(card);
//		return this.cardsDao.save(card);
//	}
	
	@Override
	public Card addCard(Card card, Long matchId) throws InformationNotFoundException, WritingInformationException {
		cardValidator.isCardValidToSave(card, matchId);
		return this.cardsDao.save(card);
	}
	
	//PUT
//
//	@Override
//	public Card updateCard(Card card, Long matchId) throws InformationNotFoundException, WritingInformationException {
//		cardValidator.isCardValidToUpdate(card, matchId);
//		return this.cardsDao.save(card);
//	}
	
	//DELETE

	@Override
	public Card deleteCard(Long id) throws InformationNotFoundException {
		Card cardDeleted = cardValidator.cardExists(id);
		this.cardsDao.deleteById(id);
		return cardDeleted;
	}
	


}
