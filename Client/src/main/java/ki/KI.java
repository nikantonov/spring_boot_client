package ki;


import java.util.ArrayList;

import java.util.HashSet;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.Move;
import MessagesBase.Terrain;
import MessagesGameState.FortState;
import MessagesGameState.FullMapNode;
import controller.Controller;



/**
 * This is a KI, it calculates the movements on the map
 *
 */
public class KI {
	ArrayList<FullMapNode> besucht = new ArrayList<FullMapNode>();
	ArrayList<FullMapNode> zweimal_besucht = new ArrayList<FullMapNode>();
	ArrayList<FullMapNode> next = new ArrayList<FullMapNode>();
	ArrayList<FullMapNode> next_nicht_besucht = new ArrayList<FullMapNode>();
	ArrayList<FullMapNode> next_nicht_besucht_2 = new ArrayList<FullMapNode>();
	ArrayList<FullMapNode> next_nicht_berg = new ArrayList<FullMapNode>();
	java.util.Set<FullMapNode> map = new HashSet<FullMapNode>();
	Random random = new Random();
	boolean treasure_besitzen = false;
	int i = 0;
	int halb = 0;
	FullMapNode act_pos = null;
	FullMapNode next_pos = null;
	private static Logger logger = LoggerFactory.getLogger(KI.class);
	
	public KI() {
		
	}
	
	public void setAct(FullMapNode fmn) {
		act_pos = fmn;
	}
	
	public void setHalb(int i) {
		halb = i;
	}
	
	public void setTreasure(boolean r) {
		treasure_besitzen = r;
	}
	
	//Constructor
	public KI(FullMapNode fmn,java.util.Set<FullMapNode> map2){
		act_pos = fmn;
		if(act_pos.getY() <= 3) {
			halb = 1;
		}else
			halb = 2;
		for(FullMapNode fmnit : map2) {
			map.add(fmnit);
		}
		for(FullMapNode fmnit : map) {
			if(fmnit.getFortState() == FortState.MyFortPresent) {
				besucht.add(fmnit);
			}
		}
		besucht.add(act_pos);
	}
	
	public void setTB(boolean i) {
		treasure_besitzen = i;
	}
	
	/**
	 * This is a main algorithm. Here it calculates the next action and returns it(RIGHT, LEFT, UP or DOWN)
	 */
	public Move getMove(java.util.Set<FullMapNode> fmn3) {
		logger.debug("Starting the next move calculation");
		map.clear();
		
		for(FullMapNode fmn : fmn3) {
			map.add(fmn);
		}
		
		Move mv = null;
		if(treasure_besitzen == false && halb == 1) {
			rechnen_eigene_halfte_1();
		}
		
		if(treasure_besitzen == false && halb == 2) {
			rechnen_eigene_halfte_2();
		}
		
		/**
		 * This is used, when the client has treasure already found, but is on its own half of the map. So when it is 
		 * is possible, it always tries to go to the another half fast
		 */
		if(treasure_besitzen == true && halb == 1 && act_pos.getY() <= 3) {
		    rechnen();
		    for(FullMapNode fmn : next) {
		    	if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() + 1)) {
		    		mv = Move.Down;
		    		zahlen(act_pos, fmn);
		    		next_pos = fmn;
		    		act_pos = next_pos;
		    		besucht.add(next_pos);
		            next.clear();
		            return mv;
		    	}
		    }
		}
		
		/**
		 * This is used, when the client has treasure already found, but is on its own half of the map. So when it is 
		 * is possible, it always tries to go to the another half fast
		 */
		if(treasure_besitzen == true && halb == 2 && act_pos.getY() >= 4) {
		    rechnen();
		    for(FullMapNode fmn : next) {
		    	if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() - 1)) {
		    		mv = Move.Up;
		    		zahlen(act_pos, fmn);
		    		next_pos = fmn;
		    		act_pos = next_pos;
		    		besucht.add(next_pos);
		            next.clear();
		            return mv;
		    	}
		    }
		}
		
		if(treasure_besitzen == true && halb == 1 && act_pos.getY() >= 4) {
		    rechnen_eigene_halfte_2();
		}
		
		if(treasure_besitzen == true && halb == 2 && act_pos.getY() <= 3) {
		    rechnen_eigene_halfte_1();
		}
		
		for(FullMapNode fmn : next) {
			next_nicht_besucht.add(fmn);
		}
		for(FullMapNode fmn : next) {
			for(FullMapNode fmn2 : besucht) {
				if(fmn.equals(fmn2)) {
					next_nicht_besucht.remove(fmn);
				}
			}
		}
		
		/**
		 * The next if-statements are to sort the possible moves to find the best one
		 */
		if(next_nicht_besucht.size() > 0) {
			next.clear();
			for(FullMapNode fmn : next_nicht_besucht) {
				next.add(fmn);
			}
		}
		
		if(next_nicht_besucht.size() == 0) {
			for(FullMapNode fmn : next) {
				next_nicht_besucht_2.add(fmn);
				System.out.println(fmn.getTerrain());
			}
			
			for(FullMapNode fmn : next) {
				for(FullMapNode fmn2 : zweimal_besucht) {
					if(fmn.equals(fmn2)) {
						next_nicht_besucht_2.remove(fmn);
					}
				}
			}
			if(next_nicht_besucht_2.size() > 0) {
				next.clear();
				for(FullMapNode fmn : next_nicht_besucht_2) {
					next.add(fmn);
				}
			}
		}
		
		
		for(FullMapNode fmn: next) {
			next_nicht_berg.add(fmn);
		}
		for(FullMapNode fmn: next) {
			if(fmn.getTerrain() == Terrain.Mountain) {
				next_nicht_berg.remove(fmn);
			}
		}
		
		if(next_nicht_berg.size() > 0) {
			next.clear();
			for(FullMapNode fmn : next_nicht_berg) {
				next.add(fmn);
			}
		}
		
		boolean end = false;
		if(treasure_besitzen == true) {
			for(FullMapNode fmn : next) {
				if(fmn.getFortState() == FortState.EnemyFortPresent) {
					next_pos = fmn;
					end = true;
				}
			}
		}
		if(end == false) {
		    next_pos = next.get(random.nextInt(next.size()));
		}
		
		
        
        if(next_pos.getX() == act_pos.getX()) {
        	if(next_pos.getY() == (act_pos.getY() - 1)) {
        		mv = Move.Up;
        	}
        	
        	if(next_pos.getY() == (act_pos.getY() + 1)) {
        		mv = Move.Down;
        	}
        }
        
        if(next_pos.getY() == act_pos.getY()) {
        	if(next_pos.getX() == (act_pos.getX() - 1)) {
        		mv = Move.Left;
        	}
        	
        	if(next_pos.getX() == (act_pos.getX() + 1)) {
        		mv = Move.Right;
        	}
        }
        zahlen(act_pos, next_pos);
        act_pos = next_pos;
        if(next_nicht_besucht.size() == 0) {
        	zweimal_besucht.add(next_pos);
        }
        System.out.println("2"+zweimal_besucht.size());
        besucht.add(next_pos);
        next.clear();
        next_nicht_besucht.clear();
        next_nicht_besucht_2.clear();
        next_nicht_berg.clear();
        return mv;
    }
	
	/**
	 * 
	 * @return number of necessary steps(2, 3 or 4)
	 */
	public int getI() {
		return i;
	}
	
	public void zahlen(FullMapNode start, FullMapNode finish) {
		if(start.getTerrain() == Terrain.Grass) {
			if(finish.getTerrain() == Terrain.Grass) {
				i = 2;
			}
			
			if(finish.getTerrain() == Terrain.Mountain) {
				i = 3;
			}
		}
		
		if(start.getTerrain() == Terrain.Mountain) {
			if(finish.getTerrain() == Terrain.Grass) {
				i = 3;
			}
			
			if(finish.getTerrain() == Terrain.Mountain) {
				i = 4;
			}
		}
	}
	
	/**
	 * This method saves the all possible moves in this point on the whole map
	 */
	private void rechnen() {
		if(act_pos.getX() - 1 >= 0) {
			for(FullMapNode fmn : map) {
				if(fmn.getTerrain() != Terrain.Water) {
					if(fmn.getX() == (act_pos.getX() - 1) && fmn.getY() == act_pos.getY()) {
						next.add(fmn);
					}
				}
			}
		}
		
		if(act_pos.getX() + 1 <= 7) {
			for(FullMapNode fmn : map) {
				if(fmn.getTerrain() != Terrain.Water) {
					if(fmn.getX() == (act_pos.getX() + 1) && fmn.getY() == act_pos.getY()) {
						next.add(fmn);
					}
				}
			}
		}
		
		if(act_pos.getY() + 1 <= 7) {
			for(FullMapNode fmn : map) {
				if(fmn.getTerrain() != Terrain.Water) {
					if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() + 1)) {
						next.add(fmn);
					}
				}
			}
		}
		
		if(act_pos.getY() - 1 >= 0) {
			for(FullMapNode fmn : map) {
				if(fmn.getTerrain() != Terrain.Water) {
					if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() - 1)) {
						next.add(fmn);
					}
				}
			}
		}
	}




	/**
	 * This method saves the all possible moves in this point on the first half of the map
	 */
private void rechnen_eigene_halfte_1() { 
	if(act_pos.getX() - 1 >= 0) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == (act_pos.getX() - 1) && fmn.getY() == act_pos.getY()) {
					next.add(fmn);
				}
			}
		}
	}
	
	if(act_pos.getX() + 1 <= 7) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == (act_pos.getX() + 1) && fmn.getY() == act_pos.getY()) {
					next.add(fmn);
				}
			}
		}
	}
	
	if(act_pos.getY() + 1 <= 3) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() + 1)) {
					next.add(fmn);
				}
			}
		}
	}
	
	if(act_pos.getY() - 1 >= 0) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() - 1)) {
					next.add(fmn);
				}
			}
		}
	}
}


/**
 * This method saves the all possible moves in this point on the second half of the map
 */
private void rechnen_eigene_halfte_2() { 
	if(act_pos.getX() - 1 >= 0) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == (act_pos.getX() - 1) && fmn.getY() == act_pos.getY()) {
					next.add(fmn);
				}
			}
		}
	}
	
	if(act_pos.getX() + 1 <= 7) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == (act_pos.getX() + 1) && fmn.getY() == act_pos.getY()) {
					next.add(fmn);
				}
			}
		}
	}
	
	if(act_pos.getY() + 1 <= 7) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() + 1)) {
					next.add(fmn);
				}
			}
		}
	}
	
	if(act_pos.getY() - 1 >= 4) {
		for(FullMapNode fmn : map) {
			if(fmn.getTerrain() != Terrain.Water) {
				if(fmn.getX() == act_pos.getX() && fmn.getY() == (act_pos.getY() - 1)) {
					next.add(fmn);
				}
			}
		}
	}
}

}



