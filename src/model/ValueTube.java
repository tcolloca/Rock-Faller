package model;

import model.Game;

public class ValueTube implements Tube{

	private int value;
	
	public ValueTube(int value) {
		this.value = value;
	}
	
	public void evaluate(Game game) {
		game.addScore(value);
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	public Tube clone() {
		return new ValueTube(value);
	}
	
	public int getValue() {
		return value;
	}
}
