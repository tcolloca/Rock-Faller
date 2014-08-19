package model;

import model.Game;

public class EmptyTube implements Tube{

	public void evaluate(Game game) {
	}
	
	public boolean isEmpty() {
		return true;
	}
	
	public Tube clone() {
		return this;
	}
	
	public int getValue() {
		return 0;
	}
}
