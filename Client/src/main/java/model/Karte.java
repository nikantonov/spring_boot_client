package model;

import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMapNode;
import MessagesBase.Terrain;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerPositionState;
import controller.Controller;

/**
 * This is a map model
 *
 */
public class Karte extends Observable{
	private java.util.Set<HalfMapNode> chunks = new HashSet<HalfMapNode>();
	private java.util.Set<FullMapNode> fmn = new HashSet<FullMapNode>();
	private java.util.Set<HalfMapNode> not_water_chunks = new HashSet<HalfMapNode>();
	private java.util.Vector<HalfMapNode> near = new Vector<HalfMapNode>(50);
	private int[] wahl = new int[]{7};
	private int[] random_lang = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
	private int[] random_breit = new int[] {0, 1, 2, 3};
	private Random rand = new Random();
	private static Logger logger = LoggerFactory.getLogger(Karte.class);
	
	
	public Karte(){
		generate();
	}
	
	public java.util.Set<HalfMapNode> getHalfmap() {
		return chunks;
	}
	
	/**
	 * This is a private method to generate the random halfmap
	 */
	  private void generate() {
		  java.util.Set<Pair> pairs = new HashSet<Pair>();
			int anzahl_berg_wasser = wahl_element();
			int anzahl_wasser = 4;
			int anzahl_berg = anzahl_berg_wasser - anzahl_wasser;
			
			
			int x_burg = wahl_lang();
			int y_burg = wahl_breit();
			int x_berg, y_berg;
			int x_wasser;
			//Here we add a node with burg
			HalfMapNode burg = new HalfMapNode(x_burg, y_burg, true, Terrain.Grass);
			chunks.add(burg);
			pairs.add(new Pair(x_burg, y_burg));
			if (anzahl_berg < 3) {
				anzahl_berg = 3;
			}
			
			int ti = 0;
			while(ti < anzahl_berg) {
				boolean prb = true;
				x_berg = wahl_lang();
				y_berg = wahl_breit();
				for(Pair p : pairs) {
					if(x_berg == p.getX() && y_berg == p.getY()) prb = false;
				}
				
				//Here we add mountains
				if(prb == true) {
					HalfMapNode berg = new HalfMapNode(x_berg, y_berg, false, Terrain.Mountain);
					chunks.add(berg);
					pairs.add(new Pair(x_berg, y_berg));
				    ti++;
				}
			}
			
		
		    	boolean wrp = true;
					/**
					 * The next code is to add random water nodes
					 */
					do {
						wrp = true;
						x_wasser = wahl_lang();
					for(Pair p : pairs) {
						if(x_wasser == p.getX() && p.getY() == 0) wrp = false;
					}}while(wrp == false);
					
					if(wrp == true) {
						HalfMapNode berg = new HalfMapNode(x_wasser, 0, false, Terrain.Water);
						chunks.add(berg);
						pairs.add(new Pair(x_wasser, 0));
					    ti++;
					}
					
					boolean wrp1 = true;
					do {
					   wrp1 = true;
					   x_wasser = wahl_lang();
					for(Pair p : pairs) {
						if(x_wasser == p.getX() && p.getY() == 1) wrp1 = false;
					}
					}while(wrp1 == false);
					
					if(wrp1 == true) {
						HalfMapNode berg = new HalfMapNode(x_wasser, 1, false, Terrain.Water);
						chunks.add(berg);
						pairs.add(new Pair(x_wasser, 1));
					    ti++;
					}
					
					boolean wrp2 = true;
					
					do {
						wrp2 = true;
						x_wasser = wahl_lang();
					for(Pair p : pairs) {
						if(x_wasser == p.getX() && p.getY() == 2) wrp2 = false;
					}}while(wrp2 == false);
					
					if(wrp2 == true) {
						HalfMapNode berg = new HalfMapNode(x_wasser, 2, false, Terrain.Water);
						chunks.add(berg);
						pairs.add(new Pair(x_wasser, 2));
					    ti++;
					}
					
					boolean wrp3 = true;
					
					do {
						wrp3 = true;
						x_wasser = wahl_lang();
					for(Pair p : pairs) {
						if(x_wasser == p.getX() && p.getY() == 3) wrp3 = false;
					}}while(wrp3 == false);
					
					if(wrp3 == true) {
						HalfMapNode berg = new HalfMapNode(x_wasser, 3, false, Terrain.Water);
						chunks.add(berg);
						pairs.add(new Pair(x_wasser, 3));
					    ti++;
					}
		    
		    //Here we add grass
		    for(int y = 0; y < 4; y++) {
		    	for(int x = 0; x < 8; x++) {
		    		
		    		boolean ff = true;
		    		for(Pair p : pairs) {
		    			if(x == p.getX() && y == p.getY())ff = false;
		    		}
		    		if(ff == true) {
		    			HalfMapNode wiese = new HalfMapNode(x, y, false, Terrain.Grass);
						chunks.add(wiese);
					}
		    	}
		    }
		    
	  }
	
	  /**
	   * This is an algorithm to proof the possible islands and to proof water on the border
	   * 
	   */
	public boolean proof() {
		boolean i = true;
		int ii = 0;
		int iii = 0;
		for (HalfMapNode hmn : chunks) {
			if(hmn.getX() == 0 && hmn.getTerrain() == Terrain.Water) {
				ii++;
			}
			if(hmn.getX() == 7 && hmn.getTerrain() == Terrain.Water) {
				iii++;
			}
		}
		
		if (ii > 1) i = false;
		if (iii > 1) i = false;
		if (select_node() != 28) {//HERE
			i = false;
		}
		if(i == false) {
			logger.info("You have islands, the map is starting the new generation");
		}else
			logger.info("You don't have islands, this map is OK");
		return i;
	}
	
	private int select_node() {
		HalfMapNode r = null;
		for(HalfMapNode hmn : chunks) {
			if(hmn.getTerrain() != Terrain.Water) {
				r = hmn; break;
			}
		}
		not_water_chunks.add(r);
		anti_inseln(r);
		while (near.size() != 0) {
			HalfMapNode ex = near.get(0);
			near.remove(0);
			anti_inseln(ex);
		}
		logger.info("You have not water chunks:"+ not_water_chunks.size());
		return not_water_chunks.size();
	}
	
	private void anti_inseln(HalfMapNode hmn) {
		int X = hmn.getX();
		int Y = hmn.getY();
		HalfMapNode actual = null;
		if((X - 1) >= 0) {
			actual = select_node_2(X-1,Y);
			add_node(actual);
		}
		
		if((Y - 1) >= 0) {
			actual = select_node_2(X,Y-1);
			add_node(actual);
		}
		
		if((Y + 1) <= 3) {
			actual = select_node_2(X,Y+1);
			add_node(actual);
		}
		
		if((X + 1) <= 7) {
			actual = select_node_2(X+1,Y);
			add_node(actual);
		}
	}
	
	private void add_node(HalfMapNode hmn) {
		HalfMapNode actual = hmn;
		boolean i = true;
		if(actual.getTerrain() != Terrain.Water) { i = true;
		for(HalfMapNode iterator : not_water_chunks) {
			if(actual.equals(iterator)) {
				i = false;
			}
		}
		if(i == true) {
		  not_water_chunks.add(actual);
		  near.add(actual);
		}
	}
	}
	
	private HalfMapNode select_node_2(int x, int y) {
		int x2 = x;
		int y2 = y;
		HalfMapNode result = null;
		for (HalfMapNode hmn : chunks) {
			if(hmn.getX() == x2 && hmn.getY() == y2) {
				result = hmn; break;
			}
		}
		return result;
	}
	
	
	
	private int wahl_element(){
		return wahl[rand.nextInt(wahl.length)];
    }
	
	//after test better private
	public int wahl_lang() {
		return random_lang[rand.nextInt(random_lang.length)];
	}
	
	//after test better private
	public int wahl_breit() {
		return random_breit[rand.nextInt(random_breit.length)];
	}
	
	public java.util.Set<HalfMapNode> getChunks(){
		return chunks;
	}
	
	public java.util.Set<FullMapNode> getFull(){
		return fmn;
	}
	
	public void setFull(java.util.Set<FullMapNode> fmn){
		this.fmn = fmn;
		this.setChanged();
		this.notifyObservers(fmn);
	}
	
	public FullMapNode getActuellePosition() {
		FullMapNode antwort = null;
		for (FullMapNode fmn2 : fmn) {
			if(fmn2.getPlayerPositionState() == PlayerPositionState.MyPosition || fmn2.getPlayerPositionState()  == PlayerPositionState.BothPlayerPosition) {
				antwort = fmn2;
			}
		}
		return antwort;
	}

}
