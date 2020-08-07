package main;




import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.web.client.RestClientException;
import controller.Controller;
import model.Spieler;
import myexceptions.ControllerNetworkProblem;
import myexceptions.NichtRichtigeParameter;
import myexceptions.NoParameterError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a main function
 * @author nikita
 */
public class MainClient {
	
	private static Logger logger = LoggerFactory.getLogger(MainClient.class);
	
	public static void main(String[] args) throws RestClientException, URISyntaxException{
		
		try {
		  String tr = args[0];
		  if(tr.equals("http://swe.wst.univie.ac.at:18235")) {
			  throw new NoParameterError("You should have modus, or the evaluation is not possible!");
		  }
		  if(!tr.equals("TR")) {
			  throw new NichtRichtigeParameter("Only Tournament-Modus is possible!");
		  }
		}catch(NoParameterError | NichtRichtigeParameter n) {
			logger.error("This is a modus-error: ",n);
			System.err.println(n);
			System.exit(2);
		}
		
		String serverBaseUrl = args[1];
		try {
		   if(!serverBaseUrl.equals("http://swe.wst.univie.ac.at:18235")) {
			   throw new NichtRichtigeParameter("This URL is not correct!");
		   }
		}catch(NichtRichtigeParameter n) {
			logger.error("This is a serverURL-error: ",n);
			System.err.println(n);
		}
		
		String gameId = args[2];
		
		
		Spieler ich = new Spieler("Nikita", "Antonov", "a1348746");
		logger.info("Your player is created!\n");
		Controller c = new Controller(serverBaseUrl, gameId, ich);
		logger.info("New game begins!\n");
		try {
		  c.start();
		}catch(ControllerNetworkProblem n) {
			logger.error("This is a controller error: ",n);
			System.err.println(n);
		}
	}
}
