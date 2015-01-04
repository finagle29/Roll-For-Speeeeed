import java.util.ArrayList;
import java.util.Collections;

class Deck extends ArrayList<Card> {

	private static final long serialVersionUID = 8521460286140110101L;

	Deck(boolean isFilled) {
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
		Card c = this.get(0);
		this.remove(0);
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
		System.out.println(this.toString());
	}

}
