
public class Card {

	static final int CLUBS = 0;
	static final int DIAMONDS = 1;
	static final int HEARTS = 2;
	static final int SPADES = 3;
	
	static final int ACE = 1;
	static final int JACK = 11;
	static final int QUEEN = 12;
	static final int KING = 13;
	
	final int suit;
	final int value;
	
	public Card( int thisValue, int thisSuit ) {
		this.suit = thisSuit;
		this.value = thisValue;
	}
	
	public String valueToString() {
		switch (value) {
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			return Integer.toString(value);
		case ACE:
			return "Ace";
		case JACK:
			return "Jack";
		case QUEEN:
			return "Queen";
		case KING:
			return "King";
		}
		return null;
	}
	
	public String suitToString() {
		switch (suit) {
		case CLUBS:
			return "Clubs";
		case DIAMONDS:
			return "Diamonds";
		case HEARTS:
			return "Hearts";
		case SPADES:
			return "Spades";
		}
		return null;
	}
	
	public String toString() {
		return this.valueToString() + " of " + this.suitToString();
	}
	
}
