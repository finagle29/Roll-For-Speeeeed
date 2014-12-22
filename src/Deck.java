import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Deck extends ArrayList<Card> {
	
	public Deck( boolean isFilled ){
		new ArrayList<Card>(52);
		if (isFilled) {
			for ( int val = 1; val <= 13; val++ ) {
				for ( int suit = 0; suit <= 3; suit++ ) {
					this.add( new Card( val, suit ) ); 
				}
			}
		}
	}
	
	public Deck() {
		this(false);
	}
	
	public Card deal() {
		Card c = this.get(0);
		this.remove(0);
		return c;
	}
	
	public void shuffle() {
		Collections.shuffle(this);
	}
	
	/**
	 * deep copy
	 */
	public Deck clone() {
		Deck d = new Deck();
		for ( Card c : this ) {
			d.add(c.clone());
		}
		return d;
	}
	
	public String toString() {
		String s = "";
		for ( Card c : this ) {
			s = s + c.toString() + " ";
		}
		return s;
	}
	
	public void display() {
		System.out.println(this.toString());
	}

}
