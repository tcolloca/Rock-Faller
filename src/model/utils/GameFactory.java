package model.utils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import model.EmptyTube;
import model.Game;
import model.Game.Tile;
import model.LifeTube;
import model.Tube;
import model.ValueTube;

public class GameFactory {

	private static Map<Character, Tile> colors = new HashMap<Character, Tile>();
	private String filename;
	
	public GameFactory(String filename) {
		initializeColors();
		this.filename = filename;
	}
	
	private static void initializeColors() {
		colors.put(' ', Tile.EMPTY);
		colors.put('R', Tile.RED);
		colors.put('A', Tile.BLUE);
		colors.put('V', Tile.GREEN);
		colors.put('G', Tile.GRAY);
		colors.put('P', Tile.STONE);
		colors.put('?', Tile.UNKNOWN);
	}
	
	public Game getGame() throws FileNotFoundException {
		GameReader gameReader = new GameReader(filename);
		return new Game(makeBoard(gameReader), makeTubes(gameReader), gameReader.getScore(), 1);
	}
	
	private Tile[][] makeBoard(GameReader gameReader) {
		char[][] charBoard = gameReader.getBoard();
		int height = gameReader.getHeight();
		int width = gameReader.getWidth();
		Tile[][] board = new Tile[height][width];	
		
		for( int i = 0; i < height; i++ )
			for( int j = 0; j < width; j++ )
				board[i][j] = colors.get(charBoard[i][j]);
			
		return board;
	}
	
	private Tube[] makeTubes(GameReader gameReader) {
		int width = gameReader.getWidth();
		
		int[] intTubes = gameReader.getTubes();
		Tube[] tubes = new Tube[width];
		
		for( int i = 0; i < width; i++ ) {
			tubes[i] = getTube(intTubes[i]);
		}
		
		return tubes;
	}
	
	private Tube getTube(int value) {
		if( value > 0 && value <= 10 )
			return new LifeTube(value);
		if( value >= 1500 )
			return new ValueTube(value);
		return new EmptyTube();
	}
}
