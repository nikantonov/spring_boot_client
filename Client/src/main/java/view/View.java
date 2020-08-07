package view;

import java.net.URISyntaxException;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

import MessagesBase.Terrain;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerGameState;
import MessagesGameState.PlayerPositionState;
import MessagesGameState.PlayerState;
import controller.Controller;
import model.Karte;
import model.Spieler;
import model.Pair;
import myexceptions.ViewCheckedException;
import netzwerk.Netzwerk;

/**
 * This is a view(MVC Pattern) with observer to show map and other important information in CLI
 *
 */
public class View implements Observer{
	private Karte k;
	private Netzwerk nz;
	private Spieler s;
	private PlayerGameState ps;
	private java.util.Set<FullMapNode> fmn = null;
	private static Logger logger = LoggerFactory.getLogger(View.class);
	public View () {
		
	}
	public View(Karte k, Netzwerk nz, Spieler s) {
		this.k = k;
		this.nz = nz;
		this.s = s;
		this.k.addObserver(this);
		this.s.addObserver(this);
	}
	public void update(Observable o, Object arg){
		try {
		  proof(o);
		}catch(ViewCheckedException e) {
			logger.error("View error: ",e);
			System.err.println(e);
		}
		
		if(o instanceof Karte)
		{
			logger.debug("The new CLI generatins is started");
			try {
				ps = nz.getPlayerState();
			} catch (RestClientException | URISyntaxException e1) {
				e1.printStackTrace();
			}
			
			fmn = k.getFull();
			for(int y = 0; y < 8; y++) {
				String s = "";
				for(int x = 0; x < 8; x++) {
					for(FullMapNode fmnode : fmn) {
						if(fmnode.getX() == x && fmnode.getY() == y) {
							String i = null;
							if(fmnode.getTerrain() == Terrain.Water) {
								i = "W";
							} else if(fmnode.getTerrain() == Terrain.Mountain) {
								i = "B";
							} else if(fmnode.getTerrain() == Terrain.Grass) {
								i = "G";
							}
							if(fmnode.getPlayerPositionState() == PlayerPositionState.NoPlayerPresent) {
								s = s + i;
							} else if (fmnode.getPlayerPositionState() == PlayerPositionState.MyPosition) {
								s = s+"[I,"+i+"]";
							}else if (fmnode.getPlayerPositionState() == PlayerPositionState.EnemyPlayerPosition) {
								s = s+"[E,"+i+"]";
							}else if (fmnode.getPlayerPositionState() == PlayerPositionState.BothPlayerPosition) {
								s = s+"[E, I,"+i+"]";
							}
						}
					} 
				}System.out.println(s);
			}try {
				System.out.println("My current state: "+ps);
			} catch (RestClientException e) {
				 logger.error("PROBLEMS WITH VIEW!", e);
				e.printStackTrace();
			}
			
	    }
		
		if(o instanceof Spieler) {
			logger.info("Your endstate"+s.getEndstate());
			System.out.println("Endstate: "+s.getEndstate());
		}
	}
	
	private void proof(Observable o) throws ViewCheckedException{
		if(!(o instanceof Karte) && !(o instanceof Spieler)) {
			throw new ViewCheckedException("We don't have view for it!");
		}
	}

}