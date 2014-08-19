package model;

import model.Game;

public class LifeTube implements Tube{

	private int value;
	
	public LifeTube(int value) {
		this.value = value;
	}
	
	public void evaluate(Game game) {
		game.addLives(value);
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	public Tube clone() {
		return new LifeTube(value);
	}
	
	public int getValue() {
		return value;
	}
}
