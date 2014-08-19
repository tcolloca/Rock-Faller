package model.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GameReader {
	
	private int width;
	private int height;
	private int score;
	private char[][] board;
	private int[] tubes;
	
	public GameReader(String fileName) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		try {
			height = Integer.valueOf(reader.readLine());
			width = Integer.valueOf(reader.readLine());
			score = Integer.valueOf(reader.readLine());
			board = readBoard(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private char[][] readBoard(BufferedReader reader) throws IOException {
		char[][] newBoard = new char[height][width];
				
		for (int y = 0; y < height; y++) {
			String line = reader.readLine();
			if (line == null)
				throw new InvalidBoardException("Less rows than expected.");
			
			// String[] strRow = line.split(""); leaves an empty first element
			char[] strRow = line.toCharArray();
			if (strRow.length != width)
				throw new InvalidBoardException("Invalid amount of columns.");
			
			for (int x = 0; x < width; x++) {
				if (isValidCharacter(strRow[x]))
					throw new InvalidBoardException("Invalid character");
				
				newBoard[y][x] = strRow[x];
			}
		}
	
		String line = reader.readLine();
		if (line == null)
			throw new InvalidBoardException("Less rows than expected.");
		
		char[] strRow = line.toCharArray();
		if( strRow[0] != '|' )
			throw new InvalidBoardException("Invalid character");
		
		tubes = new int[width];
		String[] stringTubes = new String[width]; 
	
		for(int i = 0; i < width; i++)
			stringTubes[i] = "";
		
		int k = 1;
		for(int i = 0; i < width; k++)
			if( strRow[k] != '|' ) 
				stringTubes[i] += String.valueOf(strRow[k]); 
			else
				i++;
	
		for(int i = 0; i < stringTubes.length; i++)
			if( stringTubes[i] == "" )
				tubes[i] = 0;
			else
				tubes[i] = Integer.valueOf(stringTubes[i]);
		
		// There are more rows than indicated
		if (reader.readLine() != null) 
			throw new InvalidBoardException("More rows than expected.");
		
		return newBoard;
	}
	
	private boolean isValidCharacter(char c) {
		return Character.isDigit(c) || c == ' ';
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getScore() {
		return score;
	}
	
	public char[][] getBoard() {
		return board;
	}
	
	public int[] getTubes() {
		return tubes;
	}
}
