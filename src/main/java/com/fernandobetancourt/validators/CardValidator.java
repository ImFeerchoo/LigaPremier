package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingCardException;
import com.fernandobetancourt.exceptions.CardNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ICardsDao;
import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.services.IClubesMatchesService;
import com.fernandobetancourt.services.IMatchesService;
import com.fernandobetancourt.services.IPlayersService;

@Component
public class CardValidator {
	
	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	
	@Autowired
	private ICardsDao cardsDao;
	
	public boolean isCardValidToSave(Card card, Long matchId) throws InformationNotFoundException, WritingInformationException {
		
		if(		card == null ||
				card.getMinute() == null || card.getMinute() < 0 ||
				card.getColor() == null || card.getColor().trim().equals("") ||
				card.getPlayer() == null || card.getPlayer().getPlayerId() == null || card.getPlayer().getPlayerId() < 1 ||
				matchId == null || matchId < 1) {
			
			throw new AddingCardException("Card is not valid to save");
			
		}
		
		Player playerRecovered = this.playersService.getPlayerById(card.getPlayer().getPlayerId());
		Match matchRecovered = this.matchesService.getMatch(matchId);
		
		this.playerBelongToMatch(playerRecovered, matchRecovered);
		card.setMatch(matchRecovered);
		
		return true;
	}
	
//	@Override
//	public boolean isCardValidToUpdate(Card card, Long matchId) throws InformationNotFoundException, WritingInformationException {
//		
//		if(		card == null ||
//				card.getCardId() == null || card.getCardId() < 1 ||
//				card.getMinute() == null || card.getMinute() < 0 ||
//				card.getColor() == null || card.getColor().trim().equals("") ||
//				card.getPlayer() == null || card.getPlayer().getPlayerId() == null || card.getPlayer().getPlayerId() < 1 ||
//				matchId == null || matchId < 1) {
//			
//			throw new AddingCardException("Card is not valid to save");
//			
//		}
//		
//		this.cardExists(card.getCardId());
//		Player playerRecovered = this.playersService.getPlayerById(card.getPlayer().getPlayerId());
//		Match matchRecovered = this.matchesService.getMatch(card.getMatch().getMatchId());
//		
//		this.playerBelongToMatch(playerRecovered, matchRecovered);
//		card.setMatch(matchRecovered);
//		
//		return true;
//	}

	public Card cardExists(Long id) throws InformationNotFoundException {
		return this.cardsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Card with id ").append(id).append(" has not been found");
			throw new CardNotFoundException(sb.toString());
		});
	}
	
	public boolean playerBelongToMatch(Player player, Match match) throws WritingInformationException {
		
		ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(match);
		
		if(		clubMatchRecovered.getLocalClub().getClubId() != player.getClub().getClubId() &&
				clubMatchRecovered.getVisitorClub().getClubId() != player.getClub().getClubId()) {
			
			StringBuilder sb = new StringBuilder().append("Player with id ").append(player.getPlayerId()).append(" doesn't belong to neither club");
			throw new AddingCardException(sb.toString());
			
		}
		
		return true;
	}
}
