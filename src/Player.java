import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Player {

	public String name;
	public Card pc;
	public Hand hand;
	public int HP;
	public int maxHP = 0;
	public int baseSp = 0;
	public int turnSp = 0;
	public int speed = 0;
	
	/**
	 * This constructor is used for initial setup
	 * @param name the name of the player
	 * @param p the pcDeck used for the game
	 * @param d the Deck used for the game
	 * @param dice the Die array used for the game
	 * @param in the Scanner used for the game
	 */
	public Player( String name, pcDeck p, Deck d , Die[] dice, Scanner in ) {
		this.name = name;
		hand = new Hand(0);
		hand.add(p.deal());
		hand.add(p.deal());
		
		System.out.println("Choose a player card (1 or 2): ");
		hand.display();
		int i = in.nextInt();
		while (i!=1 && i!=2) {
			System.out.println("Please choose card 1 or card 2 as your player card.");
			hand.display();
			i = in.nextInt();
		}
		pc = hand.get(i-1);
		hand.clear();
		hand.fill(d, (pc.suit==3)?5:3);
		if (pc.suit == 0) {
			Set<Card> s = new HashSet<Card>(hand);
			switch (hand.size()-s.size()) {
			case 1:
				hand.add(d.deal());
				break;
			case 2:
				hand.fill(d,3);
				break;
			}
		}
		roll4baseSp(dice, in);
		roll4hp(dice, in);
		
	}
	
	/**
	 * This constructor is used for resets.
	 * @param p the pcDeck used for the game
	 * @param d the Deck used for the game 
	 * @param dice the Die array used for the game
	 * @param h the player's current hand
	 * @param in the Scanner used for the game 
	 */
	public Player( pcDeck p, Deck d, Die[] dice, Hand h, Scanner in ) {
		hand.add(p.deal());
		hand.add(p.deal());
		
		System.out.println("Choose a player card (1 or 2): ");
		hand.display();
		int i = in.nextInt();
		while (i!=1 && i!=2) {
			System.out.println("Please choose card 1 or card 2 as your player card.");
			hand.display();
			i = in.nextInt();
		}
		pc = hand.get(i-1);
		hand.clear();
		if (((pc.suit==3)?5:3) > h.size()) {
			h.fill(d, 2);
		}
		
		roll4baseSp(dice, in);
		roll4hp(dice, in);
	}
	
	public void roll4hp( Die[] dice , Scanner in ) {
		for ( int c = 0; c < ( ( pc.suit == Card.HEARTS ) ? 6 : 5 ); c++ ) {
			maxHP += 2*dice[c].roll();
		}
		HP = maxHP;
		System.out.println("HP: " + HP + "/" + maxHP);
		if ( pc.value == Card.QUEEN || pc.value == Card.KING ) {
			System.out.println("Would you like to reroll (y/n)?");
			char ans = in.next().charAt(0);
			if (ans == 'y' || ans == 'Y') {
				maxHP = 0;
				for ( int c = 0; c < ( ( pc.suit == Card.HEARTS ) ? 6 : 5 ); c++ ) {
					maxHP += 2*dice[c].roll();
				}
				HP = maxHP;
				System.out.println("HP: " + HP + "/" + maxHP);
			}
		}
	}
	
	public void roll4baseSp( Die[] dice, Scanner in ) {
		System.out.println("Rolling for speeeeed...");
		for ( int c = 0; c < ( ( pc.suit == Card.DIAMONDS ) ? 5 : 4 ); c++ ) {
			baseSp += dice[c].roll();
		}
		System.out.println("Your current base speed is " + baseSp);
		if ( pc.value == Card.QUEEN || pc.value == Card.JACK ) {
			System.out.println("Would you like to reroll (y/n)?");
			char ans = in.next().charAt(0);
			if (ans == 'y' || ans == 'Y') {
				baseSp = 0;
				System.out.println("Rolling for speeeeed...");
				for ( int c = 0; c < ( ( pc.suit == Card.DIAMONDS ) ? 5 : 4 ); c++ ) {
					baseSp += dice[c].roll();
				}
				System.out.println("Your current base speed is now " + baseSp);
			}
		}
	}
	
	public void rollForSpeeeeed( Die[] dice ) {
		turnSp = 0;
		System.out.println("Roll for Speeeed!");
		for ( int c = 0; c < 2; c++ ) {
			turnSp += dice[c].roll();
		}
		speed = turnSp + baseSp;
	}
	
	public String toString() {
		return this.name + "\t" + this.pc.toString() + "\nHP: " + this.HP + "/" + this.maxHP + "\nspeed: " + this.speed + "\n" + this.hand.toString();
	}
	
	public void display() {
		System.out.println(this.toString());
	}
	
	public void takeTurn( Player opponent, Scanner in ) {
		System.out.println("Your hand:");
		hand.display();
		System.out.println("Attack or Effect (a/e)");
		char c = in.next().charAt(0);
		while ( c != 'a' && c != 'e' && c != 'A' && c != 'E' ) {
			hand.display();
			System.out.println("Please choose to either [A]ttack or [E]ffect.");
			c = in.next().charAt(0);
		}
		switch (c) {
		case 'a':
		case 'A':
			this.attack( opponent, in );
			break;
		default:
			this.effect( opponent, in );
		}
	}
	
	public void attack( Player opponent, Scanner in ) {
		this.hand.shuffle();
		System.out.println(opponent.name + ", choose a card from " + this.name + "'s hand (1-" + this.hand.size());
		
	}
	
	public void effect( Player opponent, Scanner in ) {
		
	}

}
