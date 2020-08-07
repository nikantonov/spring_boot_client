package model;

import java.util.Observable;


import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerGameState;

/**
 * This is a player model to save the all important information about it
 *
 */
public class Spieler extends Observable{
	private String vorname;
	private String nachname;
	private String matrikelnummer;
	private UniquePlayerIdentifier uniqueID = null;
	private PlayerGameState current_state = null;
	private FullMapNode fmn = null;
	private PlayerGameState end_state = null;

	public Spieler(String v, String n, String m) {
		vorname = v;
		nachname = n;
		matrikelnummer = m;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getMatrikelnummer() {
		return matrikelnummer;
	}

	public void setMatrikelnummer(String matrikelnummer) {
		this.matrikelnummer = matrikelnummer;
	}

	public UniquePlayerIdentifier getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(UniquePlayerIdentifier uniqueID) {
		this.uniqueID = uniqueID;
	}

	public PlayerGameState getCurrent_state() {
		return current_state;
	}

	public void setCurrent_state(PlayerGameState current_state) {
		this.current_state = current_state;
		this.setChanged();
		this.notifyObservers(current_state);
	}
	
	public FullMapNode getNode() {
		return fmn;
	}
	
	public void setNode(FullMapNode fmn) {
		this.fmn = fmn;
	}
	
	public void setEndstate(PlayerGameState end_state) {
		this.end_state = end_state;
		this.setChanged();
		this.notifyObservers(end_state);
	}
	
	public PlayerGameState getEndstate() {
		return end_state;
	}
	
}