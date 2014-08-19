package model.max;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.IllegalPlayException;

public class Max {

	private static int DEPTH = 4;
	private Game game;
	
	public Max(Game game) {
		this.game = game;
	}
	
	public Point getBestPlay(boolean withHeur) {
		Node root = new Node();
		
		max(root, DEPTH, game, withHeur);
		
		for( Node son: root.sons ) {
			if( root.value == son.value ) {
				if( withHeur )
					return son.play;
				else {
					System.out.println("Max score: " + son.value + " p: " + son.play);
					return null;
				}
			}
		}
		
		return null;
	}
	
	public double max( Node node, int height, Game game, boolean withHeur ) {
		if( height == 0 || game.isOver() ) {
			if( withHeur )
				node.value = heuristic(game);
			else 
				node.value = bestScore(game);
			return node.value;
		}
		
		node.value = Double.NEGATIVE_INFINITY;
		
		for( int x = 0; x < game.width(); x++) {
			for( int y = 0; y < game.height(); y++ ) {
				if( game.isValid(x,y) ) {
					try {
						Game gameC = game.clone();
						gameC.play(x,y);
						Node son = new Node(new Point(x,y));
						node.addSon(son);
						node.value = Math.max(node.value, max(son, height - 1, gameC, withHeur));
					} catch(IllegalPlayException e) {
					}
					
				}
			}
		}
		return node.value;
	}
	
	private double heuristic(Game game) {
		return (game.getScore()*(game.getLives()+1))/(game.avgStoneDistance() + 1);
	}
	
	private double bestScore(Game game) {
		return game.getScore();
	}
	
	public void play(int x, int y) throws IllegalPlayException {
		game.play(x, y);
	}
	
	public class Node {
		
		protected List<Node> sons = new ArrayList<Node>();
		protected Point play;
		protected double value;
		
		public Node() {
		}
		
		public Node(Point p) {
			this.play = p;
		}
		
		protected void addSon(Node node) {
			sons.add(node);
		}
	}
}
