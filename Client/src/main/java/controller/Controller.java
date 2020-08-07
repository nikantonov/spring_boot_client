package controller;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import MessagesBase.HalfMap;
import MessagesBase.Move;
import MessagesBase.PlayerMove;
import MessagesBase.RequestState;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerGameState;
import ki.KI;
import main.MainClient;
import model.Karte;
import model.Spieler;
import myexceptions.ControllerNetworkProblem;
import netzwerk.Netzwerk;
import view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a controller(MVC Pattern), that executes the whole application logic 
 */
public class Controller {
	String serverBaseUrl;
	String gameId;
	Spieler ich;
	private static Logger logger = LoggerFactory.getLogger(Controller.class);
	
	public Controller() {
		
	}
	
	public Controller(String serverBaseUrl, String gameId, Spieler ich){
		this.serverBaseUrl = serverBaseUrl;
		this.gameId = gameId;
		this.ich = ich;
	}
	
	public void start() throws RestClientException, URISyntaxException, ControllerNetworkProblem{
		Karte k = new Karte();
		do {
		k = new Karte();
		}while(k.proof() == false);
		
		RestTemplate restTemplate = new RestTemplate();
		Netzwerk nz = new Netzwerk(serverBaseUrl, gameId);
		
		logger.debug("Starting the registration\n");
		UniquePlayerIdentifier uniqueID = nz.registrate(ich.getVorname(), ich.getNachname(), ich.getMatrikelnummer());
		if(uniqueID == null) {
			throw new ControllerNetworkProblem("Problems with connection");
		}
		ich.setUniqueID(uniqueID);
		logger.debug("Registration is ended\n");
		
		
		View v = new View(k, nz, ich);
		
		do {
		}while(nz.getAmountPlayers() < 2);
		
		do {
		}while(nz.getPlayerState().equals(PlayerGameState.ShouldWait));
		
		logger.debug("Starting the halfmap sending\n");
		HalfMap hm = new HalfMap(uniqueID.getUniquePlayerID(), k.getChunks());
		nz.postHalfMap(hm);
		logger.debug("Halfmap sending is ended\n");
	
	    do {
	    }while(nz.getAmountChunks() != 64);
	    
	    nz.setFullMap(k);
	    
	    do {
	    }while(k.getActuellePosition() == null);
	    
	    KI ki = new KI(k.getActuellePosition(), k.getFull());
	    
	    do {
	    	if(nz.getPlayerState().equals(PlayerGameState.Won)) {
	    		ich.setEndstate(PlayerGameState.Won);
	    		System.exit(0);
	    	}
	    	if(nz.getPlayerState().equals(PlayerGameState.Lost)) {
	    		ich.setEndstate(PlayerGameState.Lost);
	    		System.exit(0);
	    	}
	    	
	    	java.util.Set<FullMapNode> fmmm = nz.getFullMap();
	    	Move next_move = ki.getMove(fmmm);
	    	int ii = ki.getI();
	    	
	    	while(ii > 0) {
	    		if(nz.getPlayerState().equals(PlayerGameState.Won)) {
	    			ich.setEndstate(PlayerGameState.Won);
		    		System.exit(0);
		    	}
		    	if(nz.getPlayerState().equals(PlayerGameState.Lost)) {
		    		ich.setEndstate(PlayerGameState.Lost);
		    		System.exit(0);
		    	}
		    	
	    		do {
	    		}while(nz.getPlayerState().equals(PlayerGameState.ShouldWait));
	    		nz.setFullMap(k);
		    	if(nz.checkTreasure() == true) {
		    		ki.setTB(true);
		    	}
	    		if(nz.getPlayerState().equals(PlayerGameState.Won)) {
	    			ich.setEndstate(PlayerGameState.Won);
		    		System.exit(0);
		    	}
		    	if(nz.getPlayerState().equals(PlayerGameState.Lost)) {
		    		ich.setEndstate(PlayerGameState.Lost);
		    		System.exit(0);
		    	}
		    	
	        ResponseEnvelope<RequestState> ref2 = restTemplate.postForObject(serverBaseUrl + "/game/" + gameId + "/move", PlayerMove.of(next_move, uniqueID.getUniquePlayerID()), ResponseEnvelope.class);
	    		 ii--;
	    	}
	    	if(nz.getPlayerState().equals(PlayerGameState.Won)) {
	    		ich.setEndstate(PlayerGameState.Won);
	    		System.exit(0);
	    	}
	    	if(nz.getPlayerState().equals(PlayerGameState.Lost)) {
	    		ich.setEndstate(PlayerGameState.Lost);
	    		System.exit(0);
	    	}
	    }while(true);
	    		 
	    
	}
}
