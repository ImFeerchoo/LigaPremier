package com.fernandobetancourt.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandobetancourt.dto.MatchDto;
import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IMatchesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.validators.MatchValidator;

@Service
public class MatchesServiceImpl implements IMatchesService {
	
	@Autowired
	private IMatchesDao matchesDao;
	
	@Autowired
	private IJourneysService journeysService;
	
	@Autowired
	private IClubesScoreboardsService clubesScoreboardsService;
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	
	@Autowired
	private MatchValidator matchValidator;
	
	//GET
	
//	@Override
//	public List<Match> getAllMatches(Long id) {
//		return this.matchesDao.findAll();
//	}
	
	//Le quité el id que recibia
	
	@Override
	public List<Match> getAllMatches() {
		List<Match> matches = matchesDao.findAll();
		if(matches.isEmpty()) throw new MatchNotFoundException("There are not matches available");
		return matches;
	}
	
	//Implementar tests
	@Override
	public List<Match> getMatchesByJourney(Long journeyId) throws InformationNotFoundException{
		Journey journey = journeysService.getJourney(journeyId);
		List<Match> matches = matchesDao.findByJourney(journey);
		if(matches.isEmpty()) throw new MatchNotFoundException("There ain't journeys available to this journey");
		return matches;
	}
	
	//Implementar tets
	@Override
	public List<MatchDto> getMatchesByJourneyWithClubes(Long journeyId) throws InformationNotFoundException{
		Journey journey = journeysService.getJourney(journeyId);
		List<Match> matches = matchesDao.findByJourney(journey);
		if(matches.isEmpty()) throw new MatchNotFoundException("There ain't matches available to this journey");
		List<MatchDto> matchesResponse = matches.stream()
						.map(match ->{
							ClubMatch clubMatch = clubesMatchesService.getClubMatchByMatch(match);
							return new MatchDto(match, clubMatch);
						})
						.collect(Collectors.toList());
		
		return matchesResponse;
	}

	@Override
	public Match getMatch(Long id) throws InformationNotFoundException{
		return matchValidator.matchExists(id);
	}
	
	//HACER UN MÉTODO PARA TRAER MATCHES POR JORNADA
	
	//POST

	//En vez de obtener el socoreboard del match, obtenerlo del MatchSaved ya que este si tiene un ID
	//Para no hacer la petición de obtener los clubes otra vez, desde el isMatchValidToSave podemos retornarlos
	//Ver como recibir la fecha sin que sea con un LocalDateTime
	
	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public Match addMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException{
		
		
		Map<String, Club> clubes = matchValidator.isMatchValidToSave(match, localClubId, visitorClubId);
		
		//Le asignamos un Scoreboard al match para persistirlo en cascada
		match.setScoreboard(new Scoreboard());
		
		//Persistimos el match y el scoreboard en cascada
		Match matchSaved = this.matchesDao.save(match);
		
		//Crear el DAO y servicio para el ClubScoreboard
		//Aquí debo de crear los ClubesScoreboards con el scoreboardId de match.getScoreboard.getScoreboardId();
		//Debo de recibir los clubId como parámetros
		
		//Creando el ClubScoreboard del equipo local - Inicio
		
		Scoreboard scoreboard = match.getScoreboard(); //A lo mejor y tengo que pedir el scoreboard desde algun servicio
		
//		Club localClub = this.clubesService.getClubById(localClubId);
		Club localClub = clubes.get("local");
		
		//En este punto estamos seguros que los clubes existen ya que en la primara validación se checo. Esto se vuelve a checar en la línea de
		//arriba y en el addClubScoreboard
		//Igualmente se verifica que el Scoreboard exista en el addClubScoreboard
		this.clubesScoreboardsService.addClubScoreboard(new ClubScoreboard("Local", scoreboard, localClub));
		
		//Creando el ClubScoreboard del equipo local - Final
		//Creando el ClubScoreboard del equipo visitante - Inicio
		
//		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		Club visitorClub = clubes.get("visitor");
		
		this.clubesScoreboardsService.addClubScoreboard(new ClubScoreboard("Visitor", scoreboard, visitorClub));
		
		//Creando el ClubScoreboard del equipo visitante - Final
		
		//%%%%%%%%%%%%%%%%%%Debo de crear el LineupMatch%%%%%%%%%%%%%%%%%%%%%%%%%
		
		this.lineupsMatchesService.addLineupMatch(new LineupMatch("Local", matchSaved, new Lineup()));
		this.lineupsMatchesService.addLineupMatch(new LineupMatch("Visitor", matchSaved, new Lineup()));
		
		//%%%%%%%%%%%%%%%%%%Debo de crear el ClubMatch%%%%%%%%%%%%%%%%%%%%%%%%%
		
		this.clubesMatchesService.addClubMatch(new ClubMatch(localClub, visitorClub, matchSaved));
		
		return matchSaved;
	}
	
	//Validar en el validador que el status venga dentro del MatchDto
	@Override
	public MatchDto addMatch(MatchDto matchDto) throws InformationNotFoundException, WritingInformationException{
		
		Match match = new Match();
		match.setDate(matchDto.getDate());
		match.setJourney(matchDto.getJourney());
		match.setReferee(matchDto.getReferee());
		match.setStadium(matchDto.getStadium());
		match.setStatus(matchDto.getStatus());
		
		
		Map<String, Club> clubes = matchValidator.isMatchValidToSave(match, matchDto.getLocalClub().getClubId(), matchDto.getVisitorClub().getClubId());
		
		//Le asignamos un Scoreboard al match para persistirlo en cascada
		match.setScoreboard(new Scoreboard());
		
		//Persistimos el match y el scoreboard en cascada
		Match matchSaved = this.matchesDao.save(match);
		
		//Crear el DAO y servicio para el ClubScoreboard
		//Aquí debo de crear los ClubesScoreboards con el scoreboardId de match.getScoreboard.getScoreboardId();
		//Debo de recibir los clubId como parámetros
		
		//Creando el ClubScoreboard del equipo local - Inicio
		
		Scoreboard scoreboard = match.getScoreboard(); //A lo mejor y tengo que pedir el scoreboard desde algun servicio
		
//		Club localClub = this.clubesService.getClubById(localClubId);
		Club localClub = clubes.get("local");
		
		//En este punto estamos seguros que los clubes existen ya que en la primara validación se checo. Esto se vuelve a checar en la línea de
		//arriba y en el addClubScoreboard
		//Igualmente se verifica que el Scoreboard exista en el addClubScoreboard
		this.clubesScoreboardsService.addClubScoreboard(new ClubScoreboard("Local", scoreboard, localClub));
		
		//Creando el ClubScoreboard del equipo local - Final
		//Creando el ClubScoreboard del equipo visitante - Inicio
		
//		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		Club visitorClub = clubes.get("visitor");
		
		this.clubesScoreboardsService.addClubScoreboard(new ClubScoreboard("Visitor", scoreboard, visitorClub));
		
		//Creando el ClubScoreboard del equipo visitante - Final
		
		//%%%%%%%%%%%%%%%%%%Debo de crear el LineupMatch%%%%%%%%%%%%%%%%%%%%%%%%%
		
		this.lineupsMatchesService.addLineupMatch(new LineupMatch("Local", matchSaved, new Lineup()));
		this.lineupsMatchesService.addLineupMatch(new LineupMatch("Visitor", matchSaved, new Lineup()));
		
		//%%%%%%%%%%%%%%%%%%Debo de crear el ClubMatch%%%%%%%%%%%%%%%%%%%%%%%%%
		
		this.clubesMatchesService.addClubMatch(new ClubMatch(localClub, visitorClub, matchSaved));
		
		//Ahora retorno un MatchDto
		
		matchDto.setMatchId(matchSaved.getMatchId());
		
		return matchDto;
		
	}
	
	//PUT
	
	//En vez de recibir el match con el scoreboard, cuando verifico que el match exista puedo regresar ese match que ya trae el scoreboard
	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public Match updateMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException {
		
		//Si queremos modificar el estado del club si tenemos que modificar el LineupMatch
		//Tengo que verificar si el clubLocal o el clubVisitor cambiaron, en caso de que cambien debo de actualizar los lineups
		
//		Yo creo que no tenemos que pasar el scoreboard al momento de actualizar, simplemente pasamos el match y el scoreboard lo traemos desde el servicio
		
		/*
		 * Al actualizar
		 * -No tiene sentido modificar el lineup
		 * -No tiene sentido modificar el lineupMatch
		 * -No tiene sentido modificar el scoreboard
		 * -Si se modifican los clubesScoreboads
		 * -Si se modifican los clubesMatches
		 * -Si se modifica el Match
		 */
		
		Map<String, Club> clubes = matchValidator.isMatchValidToUpdate(match, localClubId, visitorClubId);
		
		//Recupero los clubes
//		Club localClub = this.clubesService.getClubById(localClubId);
//		Club visitorClub = this.clubesService.getClubById(visitorClubId);
		
		Club localClub = clubes.get("local");
		Club visitorClub = clubes.get("visitor");
		
		ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(match);
		boolean clubsAreDifferent = false;
		
		//Actualizar clubesScoreboards
		//Los ClubesScoreboard los voy a obtener a través del scoreboard y del clubStatus, para eso debo de crear un Query Method que haga eso
		//Si hace falta alguno de los scoreboards debo de borrar todo el match y no hacer rollback
		ClubScoreboard localClubScoreboard = this.clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(match.getScoreboard().getScoreboardId(), "Local");
		
		//Después de borrar debo de volver a crear nuevos lineups y nuevos lineupsMatches
		//Si el club local cambio borro todas las relaciones de los lineups y actualizo el clubScoreboard
		if(!localClubScoreboard.getClub().getClubId().equals(localClubId)) {
			LineupMatch l = lineupsMatchesService.getLineupMatchesByMatch(match).stream().filter(x -> x.getClubStatus().equalsIgnoreCase("Local")).findFirst().orElseThrow();
			lineupsMatchesService.deleteLineupMatch(l.getLineupMatchId());
			
			localClubScoreboard.setClub(localClub);
			this.clubesScoreboardsService.updateClubScoreboard(localClubScoreboard);
			
			//%%%%%%%%%%%%%%%%%%Debo de crear de nuevo los LineupMatch%%%%%%%%%%%%%%%%%%%%%%%%%	
			this.lineupsMatchesService.addLineupMatch(new LineupMatch("Local", match, new Lineup()));
			
			clubMatchRecovered.setLocalClub(localClub);
			clubsAreDifferent = true;
		}
		
		
		ClubScoreboard visitorClubScoreboard = this.clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(match.getScoreboard().getScoreboardId(), "Visitor");
	
		//Si el club visitante cambio borro todas las relaciones de los lineups y actualizo el clubScoreboard
		if(!visitorClubScoreboard.getClub().getClubId().equals(visitorClubId)) {
			LineupMatch l = lineupsMatchesService.getLineupMatchesByMatch(match).stream().filter(x -> x.getClubStatus().equalsIgnoreCase("Visitor")).findFirst().orElseThrow();
			lineupsMatchesService.deleteLineupMatch(l.getLineupMatchId());
			
			visitorClubScoreboard.setClub(visitorClub);
			this.clubesScoreboardsService.updateClubScoreboard(visitorClubScoreboard);
			
			//%%%%%%%%%%%%%%%%%%Debo de crear de nuevo los LineupMatch%%%%%%%%%%%%%%%%%%%%%%%%%
			this.lineupsMatchesService.addLineupMatch(new LineupMatch("Visitor", match, new Lineup()));
			
			clubMatchRecovered.setVisitorClub(visitorClub);
			clubsAreDifferent = true;
		}
		
		//Actualizar el ClubMatch
		
		if(clubsAreDifferent) clubesMatchesService.updateClubMatch(clubMatchRecovered);
		
		//Actualizar match    -    Es lo ultimo que voy a persistir para lanzarlo directamente
		return this.matchesDao.save(match);
	}
	
	//DELETE

	//Yo creo que aquí no debería de hacer un rollback, ya que si no existe algo igualmente debo de eliminar lo demás
//	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public Match deleteMatch(Long id) throws InformationNotFoundException {
		
		Match matchRecovered = matchValidator.matchExists(id);
		
		//Eliminar el ClubMatch - Si no lo encuentro igual debo de borrar lo demás
		try {
			ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(matchRecovered);
			this.clubesMatchesService.deleteClubMatch(clubMatchRecovered.getClubMatchId());
		}catch(ClubMatchNotFoundException e) {
			e.printStackTrace(System.out); //Yo creo que lo voy a cambiar por un Logger
		}
		
		
		//Eliminar los dos LineupMatch
		try {
			List<LineupMatch> lineupMatchesRecovered = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered);
			lineupMatchesRecovered.forEach(lineupMatch -> {
				this.lineupsMatchesService.deleteLineupMatch(lineupMatch.getLineupMatchId());
			});
		}catch(LineupMatchNotFoundException e) {
			e.printStackTrace(System.out); //Yo creo que lo voy a cambiar por un Logger
		}
		
		//Eliminar el lineup - Se elimina en cascada al eliminar el LineuoMatch
		//Al eliminar el Lineup se eliminan los LineupPlayers y LineupCoachingStaffs
		
		//Eliminar Scoreboard - Se elimina en cascada al eliminar el match
		//Al eliminar el Scoreboard se borran en cascada los ClubesScoreboards
		
		//Eliminar el Match
		this.matchesDao.deleteById(id);
		
		return matchRecovered;
		
	}

}
