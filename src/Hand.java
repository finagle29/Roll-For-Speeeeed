import java.util.ArrayList;
import java.util.Iterator;

public class Hand extends Deck {

	public Hand( int size ) {
		new ArrayList<Card>( size );
	}
	
	public void display() {
		System.out.print( this.toString() );
	}
	
	public String toString() {
		String s = "";
		for ( Card c : this ) {
			s = s + c.toString() + "\t";
		}
		return s;
	}
	
	public void fill( Deck d, int numCards ) {
		while( numCards > 0 ) {
			this.add(d.deal());
			numCards--;
		}
	}

}
