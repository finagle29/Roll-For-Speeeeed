import java.util.ArrayList;


public class Deck {

	ArrayList<Card> deck;
	
	public Deck(){
		this.deck = new ArrayList<Card>(52);
		for ( int val = 1; val <= 13; val++ ) {
			for ( int suit = 0; suit <= 3; suit++ ) {
				deck.add( new Card( val, suit ) ); 
			}
		}
	}
	
	public Card deal() {
		Card c = deck.get(0);
		deck.remove(0);
		return c;
	}
	
	public void shuffle() {
		
	}
	
	/**
	 * deep copy
	 */
	public Deck clone() {
		Deck d = new Deck();
		d.deck.removeAll(d.deck);
		for ( Card c : this.deck ) {
			d.deck.add(c.clone());
		}
		return d;
	}
}
