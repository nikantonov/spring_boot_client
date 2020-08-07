package netzwerk;

import java.net.MalformedURLException;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.RequestState;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerGameState;
import MessagesGameState.PlayerState;
import model.Karte;
import view.View;

/**
 * This is a network to pack the network communication
 *
 */
public class Netzwerk {
	UniquePlayerIdentifier uniqueID = null;
	URL stateUrl = null;
	String serverBaseUrl = null;
	String gameId = null;
	private static Logger logger = LoggerFactory.getLogger(View.class);
	
	
	public Netzwerk (String serverBaseUrl, String gameId){
		this.serverBaseUrl = serverBaseUrl;
		this.gameId = gameId;
	}
	
	public UniquePlayerIdentifier registrate(String vorname, String nachname, String nummer) {
		RestTemplate restTemplate = new RestTemplate();
		PlayerRegistration playerReg = new PlayerRegistration(vorname, nachname, nummer);
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = restTemplate.postForObject(
				serverBaseUrl + "/game/" + gameId + "/register",
				playerReg, ResponseEnvelope.class);
		RequestState rsr = resultReg.getState();
		
		UniquePlayerIdentifier uniqueID2 = resultReg.getData().get();
		this.uniqueID = uniqueID2;
		
		try {
			stateUrl = new URL(serverBaseUrl + "/game/" + gameId + "/state/" + uniqueID.getUniquePlayerID());
		} catch (MalformedURLException e) {
			logger.error("PROBLEMS WITH GENERATING URL FOR STATE!", e);
			e.printStackTrace();
		}
		return uniqueID2;
	}
	
	public void postHalfMap(HalfMap hm) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<RequestState> ref = restTemplate.postForObject(serverBaseUrl + "/game/" + gameId + "/halfmap", hm, ResponseEnvelope.class);
	}
	
	public PlayerGameState getPlayerState() throws RestClientException, URISyntaxException {
		PlayerGameState antwort = null;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<GameState> requestResult = restTemplate.getForObject(stateUrl.toURI(), ResponseEnvelope.class);
		GameState gs = requestResult.getData().get();
		java.util.Set<PlayerState> ps = gs.getPlayers();
		for(PlayerState p : ps) {
			if(p.getUniquePlayerID().equals(uniqueID.getUniquePlayerID())) antwort = p.getState();
		}
		return antwort;
	}
	
	public boolean checkTreasure() throws RestClientException, URISyntaxException {
		boolean antwort = false;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<GameState> requestResult = restTemplate.getForObject(stateUrl.toURI(), ResponseEnvelope.class);
		GameState gs = requestResult.getData().get();
		java.util.Set<PlayerState> ps = gs.getPlayers();
		for(PlayerState p : ps) {
			if(p.getUniquePlayerID().equals(uniqueID.getUniquePlayerID())) antwort = p.hasCollectedTreasure();
		}
		return antwort;
	}
	
	public int getAmountPlayers() throws RestClientException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<GameState> requestResult = restTemplate.getForObject(stateUrl.toURI(), ResponseEnvelope.class);
		GameState gs = requestResult.getData().get();
		java.util.Set<PlayerState> ps = gs.getPlayers();
		return ps.size();
	}
	
	public int getAmountChunks() throws RestClientException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<GameState> requestResult = restTemplate.getForObject(stateUrl.toURI(), ResponseEnvelope.class);
		GameState gs = requestResult.getData().get();
		java.util.Optional<FullMap> fm = gs.getMap();
		FullMap fm2 = fm.get();
		java.util.Set<FullMapNode> fmn = fm2.getMapNodes();
		return fmn.size();
	}
	
	public void setFullMap(Karte k) throws RestClientException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<GameState> requestResult = restTemplate.getForObject(stateUrl.toURI(), ResponseEnvelope.class);
		GameState gs = requestResult.getData().get();
		java.util.Optional<FullMap> fm = gs.getMap();
		FullMap fm2 = fm.get();
		java.util.Set<FullMapNode> fmn2 = fm2.getMapNodes();
		k.setFull(fmn2);
	}
	
	public java.util.Set<FullMapNode> getFullMap() throws RestClientException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEnvelope<GameState> requestResult = restTemplate.getForObject(stateUrl.toURI(), ResponseEnvelope.class);
		GameState gs = requestResult.getData().get();
		java.util.Optional<FullMap> fm = gs.getMap();
		FullMap fm2 = fm.get();
		java.util.Set<FullMapNode> fmn2 = new HashSet<FullMapNode>();
		fmn2 = fm2.getMapNodes();
		return fmn2;
	}

}
