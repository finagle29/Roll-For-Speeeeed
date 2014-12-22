import java.util.Random;


public class Die {

	int sides;
	static Random r = new Random();
	
	public Die( int n ) {
		
		if ( n > 0 )
			sides = n;
		else
			throw new IllegalArgumentException( "Dice must have at least one side!" );
	}
	
	public Die() {
		this( 6 );
	}
	
	public int roll() {
		return 1 + r.nextInt(sides);
	}
	
}
