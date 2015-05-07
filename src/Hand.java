import java.util.ArrayList;

import javax.swing.JLabel;

class Hand extends Deck {

	Hand(int size) {
		new ArrayList<Card>(size);
	}


	public void display() {
		JLabel[] labels = new JLabel[this.size()];
		for (int i = 0; i<this.size(); i++) {
			labels[i] = new JLabel(this.get(i).toString());
		}
		notifyListeners(labels);
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Card c : this)
			s = s + c.toString() + "\t";
		return s;
	}

	void fill(Deck d, int numCards) {
		while (numCards > 0) {
			this.add(d.deal());
			numCards--;
		}
	}

}
