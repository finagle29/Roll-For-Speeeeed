import java.util.Scanner;

public class roll4spd {

	public static void main(String[] args) {
		System.out.println( "Roll For Speeeeed v0.0.1" );
		System.out.println( "game design: E Henley" );
		System.out.println( "lead programmer: milan\n" );
		
		Scanner in = new Scanner( System.in );
		Deck deck = new Deck(true);
		pcDeck pcdeck = new pcDeck();
		Deck discard = new Deck();
		deck.removeAll(pcdeck);
		deck.shuffle();
		pcdeck.shuffle();
		Die[] dice = new Die[6];
		
		for ( int i = 0; i < 6; i++ )
			dice[i] = new Die();
		
		System.out.print( "Enter the name of the player who will be set up first: " );
		Player p1 = new Player( in.next(), pcdeck, deck, dice, in );
		
		System.out.print( "Enter the name of the other player: " );
		Player p2 = new Player( in.next(), pcdeck, deck, dice, in );
		
		while ( p1.HP > 0 && p2.HP > 0 ) {
			// play ball
			p1.rollForSpeeeeed(dice);
			p1.display();
			p2.rollForSpeeeeed(dice);
			p2.display();
			if ( p1.speed > p2.speed ) {
				p1.takeTurn( p2, dice, in, pcdeck, deck, discard );
				p2.takeTurn( p1, dice, in, pcdeck, deck, discard );
			} else if ( p2.speed > p1.speed ) {
				p2.takeTurn( p1, dice, in, pcdeck, deck, discard );
				p1.takeTurn( p2, dice, in, pcdeck, deck, discard );
			}
			
			
			
			
		}

	}
}
