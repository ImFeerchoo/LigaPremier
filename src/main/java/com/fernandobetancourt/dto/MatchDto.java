package com.fernandobetancourt.dto;

import java.time.LocalDateTime;

import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Scoreboard;

public class MatchDto {
	private Long matchId;
	private LocalDateTime date;
	private String stadium;
	private String referee;
	private Journey journey;
	private Scoreboard scoreboard;
	private String status;
	private Club localClub;
	private Club visitorClub;
	
	public MatchDto() {}

	public MatchDto(Match match, ClubMatch clubMatch) {
		this.matchId = match.getMatchId();
		this.date = match.getDate();
		this.stadium = match.getStadium();
		this.referee = match.getReferee();
		this.journey = match.getJourney();
		this.scoreboard = match.getScoreboard();
		this.status = match.getStatus();
		this.localClub = clubMatch.getLocalClub();
		this.visitorClub = clubMatch.getVisitorClub();
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getStadium() {
		return stadium;
	}

	public void setStadium(String stadium) {
		this.stadium = stadium;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		this.journey = journey;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Club getLocalClub() {
		return localClub;
	}

	public void setLocalClub(Club localClub) {
		this.localClub = localClub;
	}

	public Club getVisitorClub() {
		return visitorClub;
	}

	public void setVisitorClub(Club visitorClub) {
		this.visitorClub = visitorClub;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MatchResponse [matchId=").append(matchId).append(", date=").append(date).append(", stadium=")
				.append(stadium).append(", referee=").append(referee).append(", journey=").append(journey)
				.append(", scoreboard=").append(scoreboard).append(", status=").append(status).append(", localClub=")
				.append(localClub).append(", visitorClub=").append(visitorClub).append("]");
		return builder.toString();
	}

}
