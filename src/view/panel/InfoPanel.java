package view.panel;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import model.Game;
import view.ImageManager;
import view.Main;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ScorePanel scorePanel;
	private int width;
	private int height;
	
	public InfoPanel(Main main, Game game, ImageManager imgManager, int xPosition, int width, int height) {
		this.width = width;
		this.height = height;
		
		setBackground(new Color(0x123456));
		setBounds(xPosition, 0, width, height);
		scorePanel = new ScorePanel(main, game);
		scorePanel.setVisible(true);
		add(scorePanel);
		refresh();
	}
	
	public void refresh(){
		paintImmediately(0, 0, width, height);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}	
}

