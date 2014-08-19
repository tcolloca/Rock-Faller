package view;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import model.Game.Tile;

public class ImageManager {
	
	private Map<Tile, Image> images = new HashMap<Tile, Image>();
	
	public ImageManager() {
		initImages();
	}

	private void initImages() {
		try{
			images.put(Tile.RED, ImageUtils.loadImage("resources/redTile.png"));
			images.put(Tile.BLUE, ImageUtils.loadImage("resources/blueTile.png"));
			images.put(Tile.GREEN, ImageUtils.loadImage("resources/greenTile.png"));
			images.put(Tile.GRAY, ImageUtils.loadImage("resources/cyanTile.png"));
			images.put(Tile.STONE, ImageUtils.loadImage("resources/orangeTile.png"));
			images.put(Tile.UNKNOWN, ImageUtils.loadImage("resources/violetTile.png"));
			images.put(Tile.EMPTY, ImageUtils.loadImage("resources/emptyTile.png"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Image get(Tile tile) {
		return images.get(tile);		
	}
	
	public Image get(String key) {
		return images.get(key);
	}
}