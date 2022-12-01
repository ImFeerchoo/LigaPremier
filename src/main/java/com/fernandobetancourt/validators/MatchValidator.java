package com.fernandobetancourt.validators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingMatchException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IMatchesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.services.IClubesService;
import com.fernandobetancourt.services.IJourneysService;
import com.fernandobetancourt.services.IScoreboardsService;

@Component
public class MatchValidator {
	
	 @Autowired
	 private IJourneysService journeysService;
	 
	 @Autowired
	 private IClubesService clubesService;
	 
	 @Autowired
	 private IScoreboardsService scoreboardsService;
	 
	 @Autowired
	 private IMatchesDao matchesDao;
	
	public Map<String, Club> isMatchValidToSave(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException {
		
		//Al crear el match no debe de tener un scoreboard, este se crea en automático
		if(		match == null ||
				match.getDate() == null ||
				match.getStadium() == null || match.getStadium().trim().equals("") ||
				match.getReferee() == null || match.getReferee().trim().equals("") ||
				match.getJourney() == null || match.getJourney().getJourneyId() == null || match.getJourney().getJourneyId() < 1 ||
				localClubId == null || localClubId < 1 ||
				visitorClubId == null || visitorClubId < 1 ||
				localClubId.equals(visitorClubId)
				) {
			
			throw new AddingMatchException("Match is not valid to save");
		}
		
		//Verificar que el scoreboard y la jornada existan
		//El Scoreboard en este punto no existe
//		this.scoreboardsService.scoreboardExists(match.getScoreboard().getScoreboardId());
		
		//Borre el exists, lo rempazo con el getJourney
//		this.journeysService.journeyExists(match.getJourney().getJourneyId());
		this.journeysService.getJourney(match.getJourney().getJourneyId());
		
		//Verificamos que los clubes proporcionados existan
		Club localClub = this.clubesService.getClubById(localClubId);
		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		
		Map<String, Club> clubes = Map.ofEntries(
										Map.entry("local", localClub),
										Map.entry("visitor", visitorClub)
									);
		
		return clubes;
	}
	
	//Debemos de actualizar este método a como esta el isMatchValidToSave 
	public Map<String, Club> isMatchValidToUpdate(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException{

		if(		match == null ||
				match.getMatchId() == null || match.getMatchId() < 1 ||
				match.getDate() == null ||
				match.getStadium() == null || match.getStadium().trim().equals("") ||
				match.getReferee() == null || match.getReferee().trim().equals("") ||
				match.getJourney() == null || match.getJourney().getJourneyId() == null || match.getJourney().getJourneyId() < 1 ||
				match.getScoreboard() == null || match.getScoreboard().getScoreboardId() == null || match.getScoreboard().getScoreboardId() < 1||
				localClubId == null || localClubId < 1 ||
				visitorClubId == null || visitorClubId < 1 ||
				localClubId.equals(visitorClubId)
				) {
			
			throw new AddingMatchException("Match is not valid to save");
		}
		
		//Verificamos que el match exista
		this.matchExists(match.getMatchId());
		
		//Verificar que el scoreboard y la jornada existan
		//scoreboardExists se cambió por getScoreboard
//		this.scoreboardsService.scoreboardExists(match.getScoreboard().getScoreboardId());
		this.scoreboardsService.getScoreboard(match.getScoreboard().getScoreboardId());
		
		//Borre el exists, lo rempazo con el getJourney
//		this.journeysService.journeyExists(match.getJourney().getJourneyId());
		this.journeysService.getJourney(match.getJourney().getJourneyId());
		
		//Verificamos que los clubes proporcionados existan
		Club localClub = this.clubesService.getClubById(localClubId);
		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		
		Map<String, Club> clubes = Map.ofEntries(
										Map.entry("local", localClub),
										Map.entry("visitor", visitorClub)
									);
		
		return clubes;
	}
	
	
	
	public Match matchExists(Long id) throws InformationNotFoundException{
		return this.matchesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Match with id ").append(id).append(" has not been found");
			throw new MatchNotFoundException(sb.toString());
		});
	}
}
