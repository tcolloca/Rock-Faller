package view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.Game;
import model.Tube;
import view.Main;
import view.button.Player2Button;

public class ScorePanel extends JPanel { 
	private static final long serialVersionUID = 1L;
	
	private Game game;
	
	public ScorePanel(Main main, Game game) {
		this.game = game;
		setBackground(Color.WHITE);
		setLayout(null);
		this.setPreferredSize(new Dimension(200, 150));
		Player2Button button = new Player2Button(main);
		add(button);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final int fontSize = 13;
		Font plain = new Font("Arial", Font.PLAIN, fontSize);
		g.setFont(plain);
		g.drawString("Player's score: " + game.getScore(), 45, 10);
		g.drawString("Lives: " + game.getLives(), 45, 25);
		
		int i = 40;
		for( Tube tube: game.getTubes() ) {
			g.drawString("Tube: " + tube.getValue(), 45, i);
			i += 15;
		}
	}
}
