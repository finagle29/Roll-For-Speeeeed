import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class roll4spd {

	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "Roll For Speeeeed v0.1.1 alpha GUI\ngame design: E Henley\nlead programmer: milan");

		JFrame app = new JFrame("Roll For Speeeeed");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow thewindow = new gameWindow();
		app.add(thewindow);
		app.pack();
		app.setFocusable(true);
		app.setVisible(true);
		thewindow.setup();
		
		while ((thewindow.p1.HP > 0) && (thewindow.p2.HP > 0)) {
			// play ball
			thewindow.p1.rollForSpeeeeed(thewindow.dice);
			thewindow.p1.display();
			thewindow.p2.rollForSpeeeeed(thewindow.dice);
			thewindow.p2.display();
			if (thewindow.p1.speed > thewindow.p2.speed) {
				thewindow.p1.takeTurn(thewindow.p2, thewindow.dice, thewindow.in, thewindow.pcdeck, thewindow.deck, thewindow.discard);
				thewindow.p2.takeTurn(thewindow.p1, thewindow.dice, thewindow.in, thewindow.pcdeck, thewindow.deck, thewindow.discard);
			} else if (thewindow.p2.speed > thewindow.p1.speed) {
				thewindow.p2.takeTurn(thewindow.p1, thewindow.dice, thewindow.in, thewindow.pcdeck, thewindow.deck, thewindow.discard);
				thewindow.p1.takeTurn(thewindow.p2, thewindow.dice, thewindow.in, thewindow.pcdeck, thewindow.deck, thewindow.discard);
			}

		}

	}
}
