package view.panel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JPanel;

import model.Game;
import model.Game.Tile;
import view.ImageManager;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 2L;

	private Game game;
	private ImageManager imgManager;
	private int rows, columns;
	private int cellSize;
	private Image[][] images;


	public GamePanel(Game game, ImageManager imgManager, final int cellSize) {
		this.game = game;
		this.imgManager = imgManager;
		this.rows = game.height();
		this.columns = game.width();
		this.images = new Image[rows][columns];
		this.cellSize = cellSize;
		setSize(columns * cellSize, rows * cellSize);
	}

	public void put(Image image, int row, int column) {
		images[column][row] = image;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, columns * cellSize, rows * cellSize);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				if (images[i][j] != null) 
					g.drawImage(images[i][j], j * cellSize, i * cellSize, null);
	}
	
	public void refresh() {
		for(int x = 0; x < game.width(); x++)
			for(int y = 0; y < game.height(); y++) {
				Tile tile = game.getTile(x, y);
				put(imgManager.get(tile), x, y);
			}
		
		paintImmediately(0, 0, columns * cellSize, rows * cellSize);
	}

	public Point getTilePosition(int x, int y) {
		int tileX = (int) Math.ceil(x/cellSize);
		int tileY =(int) Math.ceil(y/cellSize);

		if( game.getTile(tileX, tileY) == null )
			return null;
		
		return new Point(tileX, tileY);
	}
}
