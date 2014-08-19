package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
	
	private static final int[][] moves = new int[][]{ {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
	private static final int[][] sqrMoves = new int[][]{ {0, 0}, {1, 0}, {0, 1}, {1, 1} };
	private Tile[][] board;
	private Tube[] tubes;
	
	private int lives;
	private int score;
	
	public static enum Tile {
		RED('R'), BLUE('A'), GREEN('V'), GRAY('G'), STONE('P'), UNKNOWN('?'), EMPTY(' '); 

		private final char c;

		private Tile(char c) {
			this.c = c; 
		}

		public char getChar() {
			return c;
		}
		
		public boolean isEmpty() {
			return this == EMPTY;
		}
		
		public boolean isStone() {
			return this == STONE;
		}
		
		public boolean isColor() {
			return this == RED || this == BLUE || this == GREEN || this == GRAY;
		}
		
		public boolean isPlayable() {
			return isColor() || isStone();
		}
	}
	
	public Game(Tile[][] board, Tube[] tubes, int score, int lives) {
		this.board = board;
		this.tubes = tubes;
		this.score = score;
		this.lives = lives;
	}
	
	private void rotate(int x, int y) {
		Tile aux = board[y][x];
		board[y][x] = board[y + 1][x];
		board[y + 1][x] = board[y + 1][x + 1];
		board[y + 1][x + 1] = board[y][x + 1];
		board[y][x + 1] = aux;
	}
	
	private Collection<Group> initialPlay(int x, int y) {
		Set<Group> groups = new HashSet<Group>();
		Set<Group> preGroups = new HashSet<Group>();
		
		for( int[] move: sqrMoves )
			if( board[y + move[0]][x + move[1]].isPlayable() )
				preGroups.add(new Group(x + move[1], y + move[0]));
		
		for( Group g: preGroups )
			if( g.size() >= 4 || ( g.tile.isStone() && fallsIntoTube(g.getPoint()) ) )
				groups.add(g);
		
		return groups;
	}
	
	private boolean fallsIntoTube(Point p) {
		return p.y == height() - 1 && !tubes[p.x].isEmpty();
	}
	
	public void play(int x, int y) throws IllegalPlayException {
		if( !isValid(x, y) )
			throw new IllegalPlayException();
		
		lives--;
		rotate(x,y);
		
		Collection<Group> groups = initialPlay(x, y);
		int score = 0;
		int multiplier = 0;
		while( !groups.isEmpty() ){
			score += score(groups, multiplier);
			score += deleteTubes();
			delete(groups);
			
			Set<Integer> cols = getCols(groups);
			gravity(cols);
			coverEmptySpaces();
			groups = new HashSet<Group>();
			groups = keepPlaying(cols);
			multiplier++;
		}
	
		addScore(score);
	}
	
	private Collection<Group> keepPlaying(Set<Integer> cols) {
		Set<Group> groups = new HashSet<Group>();
		Set<Group> preGroups = new HashSet<Group>();
		
		for( int col: cols )
			for( int y = 0; y < height(); y++ )
				if( board[y][col].isPlayable() )
					preGroups.add(new Group(col,y));
		
		for( Group g: preGroups )
			if( g.size() >= 4 || ( g.tile.isStone() && fallsIntoTube(g.getPoint()) ) )
				groups.add(g);
		
		return groups;
	}
	
	public boolean isValid(int x, int y) {
		return y < board.length - 1 && x < board[0].length - 1;
	}
	
	private int score(Collection<Group> groups, int multiplier) {
		int baseScore = 100 + 80*multiplier;
		int combos = 0;
		int gems = 0;
		
		for( Group group: groups )
			if( group.tile.isColor() ) {
				combos++;
				gems += group.size();
			}
		
		return gems*baseScore*combos;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	private void delete(Collection<Group> groups) {
		for(Group group: groups )
			delete(group);
	}
	
	private void delete(Group group) {
		for(Point p: group.points)
			board[p.y][p.x] = Tile.EMPTY;
	}
	
	private int deleteTubes() {
		int score = 0;
		for( int x = 0; x < width(); x++ )
			if( board[height()-1][x].isStone() && !tubes[x].isEmpty() ) {
				tubes[x].evaluate(this);
				tubes[x] = new EmptyTube();
				//int pos = randomTubePosition();
				//tubes[pos] = randomTube();
			}
		
		return score;
	}
	
	private Set<Integer> getCols(Collection<Group> groups) {
		Set<Integer> cols = new HashSet<Integer>();
		for( Group g: groups )
			cols.addAll(g.cols);
		return cols;
	}
	
	private void gravity(Set<Integer> cols) {
		for(Integer col: cols)
			gravity(col);
	}
	
	private void gravity(int col) {
		int spaces = 0;
		
		for( int y = height() - 1; y >= 0; y-- ) {
			Tile tile = getTile(col, y);

			if( tile.isEmpty() )
				spaces++;
			else if( spaces > 0 )
				drop(col, y, spaces);
		}
	}
	
	private void drop(int x, int y, int spaces) {
		board[y + spaces][x] = getTile(x, y);
		board[y][x] = Tile.EMPTY;
	}
	
	private void coverEmptySpaces() {
		for( int x = 0; x < width(); x++)
			for(int y = 0; y < height(); y++ )
				if( board[y][x].isEmpty() )
					board[y][x] = Tile.UNKNOWN;
	}
	
	public void addLives(int lives) {
		this.lives += lives;
	}
	
	public boolean isOver() {
		return lives == 0;
	}
	
	public Tile randomTile() {
		int rand = (int)(Math.random()*37);
		if( rand < 9 )
			return Tile.BLUE;
		if( rand < 18 )
			return Tile.RED;
		if( rand < 27 )
			return Tile.GREEN;
		if( rand < 36 )
			return Tile.GRAY;
		return Tile.STONE;
	}
	
	public int randomTubePosition() {
		int x = -1;
		while( x == -1 ) {
			int rand = (int)(Math.random()*width());
			if( tubes[rand].isEmpty() )
				x = rand;
		}
		return x;
	}
	
	public Tube randomTube() {
		double rand = Math.random();
		if( rand < 0.5 ) {
			if( rand < 0.125 )
				return new LifeTube(3);
			else if( rand < 0.1875 )
				return new LifeTube(4);
			else if( rand < 0.25 )
				return new LifeTube(7);
			else 
				return new LifeTube(2);
		}
		else {
			if( rand >= 0.9375 )
				return new ValueTube(7000);
			else if( rand >= 0.75 )
				return new ValueTube(3000);
			else
				return new ValueTube(1500);		
		}
	}
	
	public int avgStoneDistance() {
		List<Point> stones = new ArrayList<Point>();
		for( int x = 0; x < width(); x++ )
			for( int y = 0; y < height(); y++ )
				if( board[y][x].isStone() )
					stones.add(new Point(x,y));
		
		int avg = 0;
		for( Point p: stones ) {				
			int xDist = Integer.MAX_VALUE;
			for( int x = 0; x < width(); x++ )
				if( !tubes[x].isEmpty() && p.y == height() - 1 )
					xDist = Math.min(xDist, Math.abs(p.x - x) + 1);
				else if( !tubes[x].isEmpty() )
					xDist = Math.min(xDist, Math.abs(p.x - x));
			avg += xDist + ((height() - 1) - p.y);
		}
		if( avg == 0 )
			return 0;
		return avg/stones.size();			
	}
	
	public Game clone() {
		Tile[][] boardCopy = new Tile[height()][width()];
		
		for(int i = 0; i < height(); i++)
			boardCopy[i] = board[i].clone();
		
		Tube[] tubesCopy = new Tube[width()];
		
		for(int i = 0; i < width(); i++) {
			tubesCopy[i] = tubes[i].clone();
		}
		
		return new Game(boardCopy, tubesCopy, score, lives);
	}
	
	public Tile getTile(int x, int y) {
		if ( y < 0 || y >= height() || x < 0 || x >= width() )
			return null;
		
		return board[y][x];
	}
	
	public Group getGroup(int x, int y) {
		return new Group(x, y);
	}

	public int width() {
		return board[0].length;
	}

	public int height() {
		return board.length;
	}
	
	public Tube[] getTubes() {
		return tubes;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLives() {
		return lives;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(board);
		return result;
	}
	
	public class Group {

		private Set<Point> points = new HashSet<Point>();
		private Set<Integer> cols = new HashSet<Integer>();
		protected Point firstPoint;
		protected Tile tile;
		
		protected Group() {			
		}
		
		public Group(int x, int y) {
			tile = getTile(x, y);
			this.firstPoint = new Point(x, y);
			addPoint(firstPoint);
			if( !tile.isStone() )
				findAdj(firstPoint);
		}
		
		private void findAdj(Point p) {
			for( int[] move: moves ){
				Point adj = new Point(p.x + move[0], p.y + move[1]);
				Tile adjTile = getTile(adj.x, adj.y);
				
				if( adjTile != null && adjTile.equals(tile) && !points.contains(adj) ) {
					addPoint(adj);
					findAdj(adj);
				}
			}
		}
		
		private void addPoint(Point p) {
			points.add(p);
			cols.add(p.x);
		}
		
		public int size() {
			return points.size();
		}
		
		public Point getPoint() {
			return firstPoint;
		}

		private Game getOuterType() {
			return Game.this;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((points == null) ? 0 : points.hashCode());
			result = prime * result;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Group other = (Group) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (tile != other.tile)
				return false;
			if (points == null) {
				if (other.points != null)
					return false;
			} else if (!points.equals(other.points))
				return false;
			return true;
		}
		
		public String toString() {
			return "Group: " + firstPoint + " color: " + tile;
		}
	}	
}