package model;

import model.Game;

public interface Tube {

	public void evaluate(Game game);
	
	public int getValue();
	
	public boolean isEmpty();
	
	public Tube clone();
}
