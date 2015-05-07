import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class gameWindow extends JPanel {

	Scanner in;
	Deck deck, discard;
	pcDeck pcdeck;
	Die[] dice;
	Player p1, p2;
	
	public void notif( JLabel[] labels ) {
		removeAll();
		for ( JLabel l : labels )
			this.add(l);
		validate();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public gameWindow() {
		in = new Scanner(System.in);
		deck = new Deck(true);
		pcdeck = new pcDeck();
		discard = new Deck();
		deck.removeAll(pcdeck);
		deck.shuffle();
		pcdeck.shuffle();
		dice = new Die[6];

		for (int i = 0; i < 6; i++)
			dice[i] = new Die();
	}
	
	public void setup() {
		p1 = new Player();
		p1.hand.registerObserver(this);
		p1.setup(JOptionPane.showInputDialog("What is the name of the player who will be set up first?"), pcdeck, deck, dice, in);

		p2 = new Player();
		p2.hand.registerObserver(this);
		p2.setup(JOptionPane.showInputDialog("What is the name of the other player?"), pcdeck, deck, dice, in);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500,500);
	}
}
