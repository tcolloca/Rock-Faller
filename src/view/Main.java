package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import model.Game;
import model.utils.GameFactory;

public class Main extends JFrame { // TODO: Todo's. Beautify code and add game(filename).

	private static final long serialVersionUID = 1L;
	private static final int mPanelRelX = 8;
	private static final int mPanelRelY = 30;
	
	private Game game;
	private MainPanel mainPanel;

	public Main(Game game) {
		super("Azulejos");

		this.game = game;			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		this.setContentPane(mainPanel = new MainPanel(this, game));
		this.setSize(mainPanel.getWidth(), mainPanel.getHeight() + 40);
		this.setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);

		addMouseListener(new MouseAdapter() {	

			public void mouseClicked(MouseEvent e) {
				Main main = Main.this;
				main.playPlayer1(e.getX(), e.getY());
			}
		});
	}
	
	private void playPlayer1(int x, int y) {
		Point p = mainPanel.getTilePosition(x - mPanelRelX, y - mPanelRelY);
		if( p != null ) {
			try {
				game.play(p.x, p.y);	
				mainPanel.refresh();
			} catch( Exception e) {
			}
		}
	}
	
	public Game getGame() {
		return game;
	}
	
	public void refresh() {
		mainPanel.refresh();
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, FileNotFoundException {	
		Game game = (new GameFactory("randomGame.txt")).getGame();		
		
		Main mainWindow = new Main(game);

		mainWindow.setVisible(true);
	}

}
