import java.util.ArrayList;

class Hand extends Deck {

	private static final long serialVersionUID = 8263442648262071244L;

	Hand(int size) {
		new ArrayList<Card>(size);
	}

	@Override
	public void display() {
		System.out.print(this.toString());
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
