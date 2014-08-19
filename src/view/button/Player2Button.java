package view.button;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import model.max.Max;
import view.Main;

public class Player2Button extends JButton {
	
	private static final long serialVersionUID = 1L;
	
	private Max max;

	public Player2Button(final Main main) {
		super("Play Player2");
		this.max = new Max(main.getGame());
		setAlignmentY(0.5f);
		setBounds(35, 100, 130, 30);
		setVisible(true);
		
		 addMouseListener(new MouseAdapter() {	
		 
				public void mouseClicked(MouseEvent e) {
					max.getBestPlay(false);
					
					Point p = max.getBestPlay(true);
					try {
						max.play(p.x, p.y);
					} catch(Exception ex) {
					}
					main.refresh();
					System.out.println(p);
		    	}
			});
	}
}
