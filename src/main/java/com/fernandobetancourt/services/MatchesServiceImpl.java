package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandobetancourt.exceptions.AddingMatchException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IMatchesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Scoreboard;

@Service
public class MatchesServiceImpl implements IMatchesService {
	
	@Autowired
	private IMatchesDao matchesDao;
	
	@Autowired
	private IScoreboardsService scoreboardsService;
	
	@Autowired
	private IJourneysService journeysService;
	
	@Autowired
	private IClubesScoreboardsService clubesScoreboardsService;
	
	@Autowired
	private IClubesService clubesService;
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	
	//GET
	
	@Override
	public List<Match> getAllMatches(Long id) {
		return this.matchesDao.findAll();
	}

	@Override
	public Match getMatch(Long id) throws InformationNotFoundException{
		return this.matchExists(id);
	}
	
	//HACER UN MÉTODO PARA TRAER MATCHES POR JORNADA
	
	//POST

	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public Match addMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException{
		
		
		this.isMatchValidToSave(match, localClubId, visitorClubId);
		
		//Le asignamos un Scoreboard al match para persistirlo en cascada
		match.setScoreboard(new Scoreboard());
		
		//Persistimos el match y el scoreboard en cascada
		Match matchSaved = this.matchesDao.save(match);
		
		//Crear el DAO y servicio para el ClubScoreboard
		//Aquí debo de crear los ClubesScoreboards con el scoreboardId de match.getScoreboard.getScoreboardId();
		//Debo de recibir los clubId como parámetros
		
		//Creando el ClubScoreboard del equipo local - Inicio
		
		Scoreboard scoreboard = match.getScoreboard(); //A lo mejor y tengo que pedir el scoreboard desde algun servicio
		Club localClub = this.clubesService.getClubById(localClubId);
		//En este punto estamos seguros que los clubes existen ya que en la primara validación se checo. Esto se vuelve a checar en la línea de
		//arriba y en el addClubScoreboard
		//Igualmente se verifica que el Scoreboard exista en el addClubScoreboard
		this.clubesScoreboardsService.addClubScoreboard(new ClubScoreboard("Local", scoreboard, localClub));
		
		//Creando el ClubScoreboard del equipo local - Final
		//Creando el ClubScoreboard del equipo visitante - Inicio
		
		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		this.clubesScoreboardsService.addClubScoreboard(new ClubScoreboard("Visitor", scoreboard, visitorClub));
		
		//Creando el ClubScoreboard del equipo visitante - Final
		
		//%%%%%%%%%%%%%%%%%%Debo de crear el LineupMatch%%%%%%%%%%%%%%%%%%%%%%%%%
		
		this.lineupsMatchesService.addLineupMatch(new LineupMatch("Local", matchSaved, new Lineup()));
		this.lineupsMatchesService.addLineupMatch(new LineupMatch("Visitor", matchSaved, new Lineup()));
		
		//%%%%%%%%%%%%%%%%%%Debo de crear el ClubMatch%%%%%%%%%%%%%%%%%%%%%%%%%
		
		this.clubesMatchesService.addClubMatch(new ClubMatch(localClub, visitorClub, matchSaved));
		
		return matchSaved;
	}
	
	//PUT
	
	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public Match updateMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException {
		
		/*
		 * Al actualizar
		 * -No tiene sentido modificar el lineup
		 * -No tiene sentido modificar el lineupMatch
		 * -No tiene sentido modificar el scoreboard
		 * -Si se modifican los clubesScoreboads
		 * -Si se modifican los clubesMatches
		 * -Si se modifica el Match
		 */
		
		this.isMatchValidToUpdate(match, localClubId, visitorClubId);
		
		//Recupero los clubes
		Club localClub = this.clubesService.getClubById(localClubId);
		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		
		//Actualizar clubesScoreboards
		//Los ClubesScoreboard los voy a obtener a través del scoreboard y del clubStatus, para eso debo de crear un Query Method que haga eso
		//Si hace falta alguno de los scoreboards debo de borrar todo el match y no hacer rollback
		ClubScoreboard localClubScoreboard = this.clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(match.getScoreboard().getScoreboardId(), "Local");
		localClubScoreboard.setClub(localClub);
		this.clubesScoreboardsService.updateClubScoreboard(localClubScoreboard);
		
		ClubScoreboard visitorClubScoreboard = this.clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(match.getScoreboard().getScoreboardId(), "Visitor");
		visitorClubScoreboard.setClub(visitorClub);
		this.clubesScoreboardsService.updateClubScoreboard(visitorClubScoreboard);
		
		//Actualizar el ClubMatch
		//Lo voy a recuperar a traves del Match
		ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(match);
		clubMatchRecovered.setLocalClub(localClub);
		clubMatchRecovered.setVisitorClub(visitorClub);
		this.clubesMatchesService.updateClubMatch(clubMatchRecovered);
		
		//Actualizar match    -    Es lo ultimo que voy a persistir para lanzarlo directamente
		return this.matchesDao.save(match);
	}
	
	//DELETE

	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public Match deleteMatch(Long id) throws InformationNotFoundException {
		
		Match matchRecovered = this.matchExists(id);
		
		//Eliminar el ClubMatch - Si no lo encuentro igual debo de borrar lo demás
		ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(matchRecovered);
		this.clubesMatchesService.deleteClubMatch(clubMatchRecovered.getClubMatchId());
		
		//Eliminar Scoreboard - Se elimina en cascada
		//Al eliminar el Scoreboard se borran en cascada los ClubesScoreboards		
		
		//Eliminar los dos LineupMatch
		List<LineupMatch> lineupMatchesRecovered = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered);
		lineupMatchesRecovered.forEach(lineupMatch -> {
			this.lineupsMatchesService.deleteLineupMatch(lineupMatch.getLineupMatchId());
		});
		
		//Eliminar el lineup - Se elimina en cascada
		//Al eliminar el Lineup se eliminan los LineupPlayers y LineupCoachingStaffs
		
		
		//Eliminar el Match
		this.matchesDao.deleteById(id);
		
		return matchRecovered;
		
	}
	
	//VALIDATIONS
	//DEBO DE PONER QUE LOS ClubId DEBEN DE SER DIFERENTES ENTRE SI

	@Override
	public boolean isMatchValidToSave(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException {
		
		//Al crear el match no debe de tener un scoreboard, este se crea en automático
		if(		match == null ||
				match.getDate() == null ||
				match.getStadium() == null || match.getStadium().trim().equals("") ||
				match.getReferee() == null || match.getReferee().trim().equals("") ||
				match.getJourney() == null || match.getJourney().getJourneyId() == null || match.getJourney().getJourneyId() < 1 ||
				localClubId == null || localClubId < 1 ||
				visitorClubId == null || visitorClubId < 1
				) {
			
			throw new AddingMatchException("Match is not valid to save");
		}
		
		//Verificar que el scoreboard y la jornada existan
		//El Scoreboard en este punto no existe
//		this.scoreboardsService.scoreboardExists(match.getScoreboard().getScoreboardId());
		this.journeysService.journeyExists(match.getJourney().getJourneyId());
		//Verificamos que los clubes proporcionados existan
		this.clubesService.getClubById(localClubId);
		this.clubesService.getClubById(visitorClubId);
		
		return true;
	}
	
	//Debemos de actualizar este método a como esta el isMatchValidToSave 
	public boolean isMatchValidToUpdate(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException{

		if(		match == null ||
				match.getMatchId() == null || match.getMatchId() < 1 ||
				match.getDate() == null ||
				match.getStadium() == null || match.getStadium().trim().equals("") ||
				match.getReferee() == null || match.getReferee().trim().equals("") ||
				match.getJourney() == null || match.getJourney().getJourneyId() == null || match.getJourney().getJourneyId() < 1 ||
				match.getScoreboard() == null || match.getScoreboard().getScoreboardId() == null || match.getScoreboard().getScoreboardId() < 1||
				localClubId == null || localClubId < 1 ||
				visitorClubId == null || visitorClubId < 1
				) {
			
			throw new AddingMatchException("Match is not valid to save");
		}
		
		//Verificar que el scoreboard y la jornada existan
		this.scoreboardsService.scoreboardExists(match.getScoreboard().getScoreboardId());
		this.journeysService.journeyExists(match.getJourney().getJourneyId());
		//Verificamos que los clubes proporcionados existan
		this.clubesService.getClubById(localClubId);
		this.clubesService.getClubById(visitorClubId);
		//Verificamos que el match exista
		this.matchExists(match.getMatchId());
		
		return true;
	}
	
	
	
	public Match matchExists(Long id) throws InformationNotFoundException{
		return this.matchesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Match with id ").append(id).append(" has not been found");
			throw new MatchNotFoundException(sb.toString());
		});
	}

}
