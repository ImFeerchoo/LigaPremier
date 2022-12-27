package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Card;

public interface ICardsService {
	
	//GET
	public abstract Card getCard(Long id) throws InformationNotFoundException;
	public abstract List<Card> getCardsByMatch(Long matchId) throws InformationNotFoundException;
	
	//POST
//	public abstract Card addCard(Card card) throws InformationNotFoundException, WritingInformationException;
	public abstract Card addCard(Card card, Long matchId) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
//	public abstract Card updateCard(Card card) throws InformationNotFoundException, WritingInformationException;
//	public abstract Card updateCard(Card card, Long matchId) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Card deleteCard(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
//	public abstract boolean isCardValidToSave(Card card) throws InformationNotFoundException, WritingInformationException;
//	public abstract boolean isCardValidToUpdate(Card card) throws InformationNotFoundException, WritingInformationException;
}
