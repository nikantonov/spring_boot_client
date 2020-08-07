package model;

import java.util.Observable;

import MessagesGameState.PlayerGameState;

/**
 * This is my own small pair-structure to combine x and y in one
 */
public class Pair {
	int x;
	int y;
	int test;
	
	public Pair(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	
}

