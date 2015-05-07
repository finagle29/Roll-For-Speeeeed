import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;

class Deck extends ArrayList<Card> {
	
	public ArrayList<gameWindow> observers;
	
	public void registerObserver( gameWindow o ) {
		observers.add(o);
	}
	
	public void notifyListeners( JLabel[] labels ) {
		for ( gameWindow o : observers ) {
			((gameWindow) o).notif( labels );
		}
	}
	
	Deck(boolean isFilled) {
		observers = new ArrayList<gameWindow>();
		new ArrayList<Card>(52);
		if (isFilled)
			for (int val = 1; val <= 13; val++)
				for (int suit = 0; suit <= 3; suit++)
					this.add(new Card(val, suit));
	}

	public Deck() {
		this(false);
	}

	Card deal() {
		Card c = get(0);
		remove(c);
		return c;
	}

	void shuffle() {
		Collections.shuffle(this);
	}

	/**
	 * deep copy
	 */
	@Override
	public Deck clone() {
		Deck d = new Deck();
		for (Card c : this)
			d.add(c.clone());
		return d;
	}

	@Override
	public String toString() {
		String s = "";
		for (Card c : this)
			s = s + c.toString() + " ";
		return s;
	}

	public void display() {
		JLabel[] labels = new JLabel[this.size()];
		for (int i = 0; i<this.size(); i++) {
			labels[i]= new JLabel(this.get(i).toString());
		}
		notifyListeners( labels );
	}

}
