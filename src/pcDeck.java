
public class pcDeck extends Deck {

	public pcDeck() {
		for ( int val = 11; val <= 13; val++ ) {
			for ( int suit = 0; suit <= 3; suit++ ) {
				this.add( new Card( val, suit ) ); 
			}
		}
	}

}
