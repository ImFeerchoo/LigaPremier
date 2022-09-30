package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingCardException;
import com.fernandobetancourt.exceptions.CardNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ICardsDao;
import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;

@Service
public class CardsServiceImpl implements ICardsService {
	
	@Autowired
	private ICardsDao cardsDao;
	
	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	
	//GET

	@Override
	public Card getCard(Long id) throws InformationNotFoundException {
		return this.cardExists(id);
	}

	@Override
	public List<Card> getCardsByMatch(Long matchId) throws InformationNotFoundException {
		Match matchRecovered = this.matchesService.getMatch(matchId);
		return this.cardsDao.findByMatch(matchRecovered).orElseThrow(() -> {
			throw new CardNotFoundException("This match doesn't have cards");
		});
	}
	
	//POST

	@Override
	public Card addCard(Card card) throws InformationNotFoundException, WritingInformationException {
		this.isCardValidToSave(card);
		return this.cardsDao.save(card);
	}
	
	//PUT

	@Override
	public Card updateCard(Card card) throws InformationNotFoundException, WritingInformationException {
		this.isCardValidToUpdate(card);
		return this.cardsDao.save(card);
	}
	
	//DELETE

	@Override
	public Card deleteCard(Long id) throws InformationNotFoundException {
		Card cardDeleted = this.cardExists(id);
		this.cardsDao.deleteById(id);
		return cardDeleted;
	}
	
	//VALIDATIONS

	@Override
	public boolean isCardValidToSave(Card card) throws InformationNotFoundException, WritingInformationException {
		
		if(		card == null ||
				card.getMinute() == null || card.getMinute() < 0 ||
				card.getColor() == null || card.getColor().trim().equals("") ||
				card.getPlayer() == null || card.getPlayer().getPlayerId() == null || card.getPlayer().getPlayerId() < 1 ||
				card.getMatch() == null || card.getMatch().getMatchId() == null || card.getMatch().getMatchId() < 1) {
			
			throw new AddingCardException("Card is not valid to save");
			
		}
		
		Player playerRecovered = this.playersService.playerExists(card.getPlayer().getPlayerId());
		Match matchRecovered = this.matchesService.matchExists(card.getMatch().getMatchId());
		this.playerBelongToMatch(playerRecovered, matchRecovered);
		
		return true;
	}

	@Override
	public boolean isCardValidToUpdate(Card card) throws InformationNotFoundException, WritingInformationException {
		
		if(		card == null ||
				card.getCardId() == null || card.getCardId() < 1 ||
				card.getMinute() == null || card.getMinute() < 0 ||
				card.getColor() == null || card.getColor().trim().equals("") ||
				card.getPlayer() == null || card.getPlayer().getPlayerId() == null || card.getPlayer().getPlayerId() < 1 ||
				card.getMatch() == null || card.getMatch().getMatchId() == null || card.getMatch().getMatchId() < 1) {
			
			throw new AddingCardException("Card is not valid to save");
			
		}
		
		this.cardExists(card.getCardId());
		Player playerRecovered = this.playersService.playerExists(card.getPlayer().getPlayerId());
		Match matchRecovered = this.matchesService.matchExists(card.getMatch().getMatchId());
		this.playerBelongToMatch(playerRecovered, matchRecovered);
		
		return true;
	}

	@Override
	public Card cardExists(Long id) throws InformationNotFoundException {
		return this.cardsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Card with id ").append(id).append(" has not been found");
			throw new CardNotFoundException(sb.toString());
		});
	}
	
	private boolean playerBelongToMatch(Player player, Match match) throws WritingInformationException {
		
		ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(match);
		
		if(		clubMatchRecovered.getLocalClub().getClubId() != player.getClub().getClubId() &&
				clubMatchRecovered.getVisitorClub().getClubId() != player.getClub().getClubId()) {
			
			StringBuilder sb = new StringBuilder().append("Player with id ").append(player.getPlayerId()).append(" doesn't belong to neither club");
			throw new AddingCardException(sb.toString());
			
		}
		
		return true;
	}

}
