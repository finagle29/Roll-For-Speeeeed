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
	public boolean shielded = false;
	
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
	
	private void makeEqualTo( Player copy ) {
		pc = copy.pc.clone();
		hand = (Hand) copy.hand.clone();
		maxHP = copy.maxHP;
		baseSp = copy.baseSp;
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
	
	public void takeTurn( Player opponent, Die[] dice, Scanner in, pcDeck pcdeck, Deck deck, Deck discard ) {
		returnState r;
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
			r = this.attack( opponent, dice, in, pcdeck, deck, discard );
			break;
		default:
			r = this.effect( opponent, in );
		}
		if (r.discardNeeded) {
			discard.add(hand.get(r.choice));
			hand.remove(r.choice);
			if (deck.isEmpty()) {
				deck.addAll(discard);
				discard.clear();
				deck.shuffle();
			}
			hand.add(deck.deal());
		}
	}
	
	private returnState attack( Player opponent, Die[] dice, Scanner in, pcDeck pcdeck, Deck deck, Deck discard ) {
		this.hand.shuffle();
		System.out.println(opponent.name + ", choose a card from " + this.name + "'s hand (1-" + this.hand.size() + ")");
		int choice = in.nextInt() - 1;
		while (choice < 0 || choice >= this.hand.size()) {
			System.out.println("You must choose a valid card index (between 1 and " + this.hand.size() + ")");
			choice = in.nextInt() - 1;
		}
		System.out.println(hand.get(choice).toString());
		switch (hand.get(choice).value) {
		case Card.ACE:
		case 3:
		case 4:
		case 5:
		case 7:
			damage(hand.get(choice).value, opponent, dice, in);
			break;
		case 8:
		case 9:
			if (pc.value == Card.JACK)
				damage(Card.ACE, opponent, dice, in);
			else
				damage(hand.get(choice).value, opponent, dice, in);
			break;
		case 6:
			if (pc.value == Card.KING)
				damage(Card.ACE, opponent, dice, in);
			else
				damage(hand.get(choice).value, opponent, dice, in);
			break;
		case 2:
			switch (pc.suit) {
			case Card.CLUBS:
				shielded = true;
				break;
			case Card.DIAMONDS:
				System.out.print("Rolling to drain the average of 2d6... ");
				int r1 = dice[0].roll();
				int r2 = dice[1].roll();
				System.out.println(r1 + " " + r2);
				int drain = (r1 + r2)/2 + (r1 + r2)%2;
				System.out.println(drain + " was drained!");
				opponent.HP -= drain;
				this.HP += drain;
				break;
			case Card.HEARTS:
				Card[] handArray = (Card[]) hand.toArray();
				for ( Card c : handArray ) {
					deck.add(c);
					hand.remove(c);
				}
				handArray = (Card[]) opponent.hand.toArray();
				for ( Card c : handArray ) {
					deck.add(c);
					opponent.hand.remove(c);
				}
				deck.shuffle();
				hand.fill(deck, 4);
				opponent.hand.fill(deck, 4);
				return new returnState(choice, false);
			case Card.SPADES:
				System.out.print("Rolling d6 to increase speed... ");
				r1 = dice[0].roll();
				System.out.println(r1);
				baseSp += r1;
			}
			break;
		case 10:
			System.out.println("Who shall you reset? " + name + " or " + opponent.name + "?");// choose who to reset
			String choyce = in.next();
			while (!choyce.equalsIgnoreCase(name) && !choyce.equalsIgnoreCase(opponent.name)) {
				System.out.println("You must choose one of the two players currently in play!");
				choyce = in.next();
			}
			if (choyce.equalsIgnoreCase(name)) {
				Player tempPlayer = new Player(pcdeck, deck, dice, hand, in);
				this.makeEqualTo(tempPlayer);
				return new returnState(choice, false);
			} else {
				System.out.print("Rolling for hit... ");
				int roll = dice[0].roll();
				System.out.println(roll);
				if (roll*10 > speed) {
					Player tempPlayer = new Player(pcdeck, deck, dice, opponent.hand, in);
					opponent.makeEqualTo(tempPlayer);
				} else {
					System.out.println("Miss");
				}
			}
			break;
		}
		return new returnState(choice, true);
	}
	
	private void damage( int chupa, Player opponent, Die[] dice, Scanner in ) {
		if (opponent.shielded) {
			System.out.println(opponent.name + " blocked the attack!");
			opponent.shielded = false;
		} else {
			if (chupa >= 3 && chupa <= 9) {
				System.out.print("Rolling for hit... ");
				int roll = dice[0].roll();
				System.out.println(roll);
				if (roll*10 > speed) {
					System.out.println(opponent.name + " takes " + chupa + " damage");
					if (opponent.HP > chupa)
						opponent.HP -= chupa;
					else
						opponent.HP = 0;
				} else {
					System.out.println("Miss");
				}
			} else if (chupa == Card.ACE) {
				System.out.print("Rolling 2d6 for damage... ");
				int r1 = dice[0].roll();
				int r2 = dice[1].roll();
				System.out.println(r1 + " " + r2);
				if (opponent.HP > (r1+r2) )
					opponent.HP -= (r1 + r2);
				else
					opponent.HP = 0;
			}
		}
	}
	
	private returnState effect( Player opponent, Scanner in ) {
		return new returnState(0,false); // write code here later
	}

	private class returnState {
		int choice;
		boolean discardNeeded;
		
		returnState( int c, boolean d ) {
			choice = c;
			discardNeeded = d;
		}
		
	}
}
