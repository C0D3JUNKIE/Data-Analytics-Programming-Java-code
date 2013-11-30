import java.util.Comparator;
import java.util.Arrays;
import java.util.Random;

enum Suit {
	HEART, CLUB, SPADE, DIAMOND
}

enum Value {
	TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

class Card {
	Suit suit;
	Value value;
	  
	Card( Suit s, Value v) {
		suit = s;
		value = v;
	}
}

class CardCompare implements Comparator<Card> {
	 private CardCompare() { };
	 static public final CardCompare compareObject = new CardCompare();
	 public int compare(Card s1, Card s2) {
		  	return s2.value.ordinal() - s1.value.ordinal();
	 }
}

class Hand5 {
	Card cards[] = new Card[5];
	
	public Hand5() {
	}
	
	public Hand5( Card a, Card b, Card c, Card d, Card e) {
		cards[0] = a;
	    cards[1] = b;
	    cards[2] = c;
	    cards[3] = d;
	    cards[4] = e;
	    Arrays.sort(cards,
	            // descending sort
	            CardCompare.compareObject
	    );
	}
	
	boolean isStraightFlush() {
	    return isFlush() && isStraight();
	}
	
	boolean is4OfAKind() {
	    return (cards[0].value == cards[1].value && cards[1].value == cards[2].value 
	    		&& cards[2].value == cards[3].value)
	    		|| (cards[1].value == cards[2].value && cards[2].value == cards[3].value 
	    	    && cards[3].value == cards[4].value);
	}
	
	boolean isFullHouse() {
		return cards[0].value == cards[1].value && cards[1].value == cards[2].value
			   && cards[3].value == cards[4].value
			   || cards[2].value == cards[3].value && cards[3].value == cards[4].value
			   && cards[0].value == cards[1].value;
	}
	
	boolean is3OfAKind() {
		return (cards[0].value == cards[1].value && cards[1].value == cards[2].value
			   || cards[1].value == cards[2].value && cards[2].value == cards[3].value
			   || cards[2].value == cards[3].value && cards[3].value == cards[4].value);
	}
	
	boolean isFlush() {
	    return cards[0].suit == cards[1].suit && cards[1].suit == cards[2].suit && 
	    	    cards[2].suit == cards[3].suit && cards[3].suit == cards[4].suit;
	}
	
	boolean isStraight() {
	    return (cards[0].value.ordinal() == cards[1].value.ordinal() + 1
	          && cards[1].value.ordinal() == cards[2].value.ordinal() + 1 
	          && cards[2].value.ordinal() == cards[3].value.ordinal() + 1 
	          && cards[3].value.ordinal() == cards[4].value.ordinal() + 1)
	        || (cards[0].value == Value.ACE && cards[1].value == Value.FIVE 
	        && cards[2].value == Value.FOUR && cards[3].value == Value.THREE
	        && cards[4].value == Value.TWO);
	}
	
	boolean isTwoPair() {
	    return cards[0].value == cards[1].value
	    	   && cards[2].value == cards[3].value
	           || cards[0].value == cards[1].value
	           && cards[3].value == cards[4].value
	           || cards[1].value == cards[2].value
	           && cards[3].value == cards[4].value;
	}
	
	boolean isOnePair() {
		return (cards[0].value == cards[1].value || cards[1].value == cards[2].value 
			   || cards[2].value == cards[3].value || cards[3].value == cards[4].value);
	}
}

class Hand5Comparator implements Comparator<Hand5> {

  //TODO: implement to return negative, 0, or positive if h1 is less than,
  // equal to, or greater than h0. (This is the reverse of the
  // usual convention, but it has the benefit that when used with Arrays.sort,
  // it gives the best hand in index 0.)
	
	private static int highCheck( boolean b0, boolean b1, Hand5 h0, Hand5 h1, int i) {
	    if ( !b0 && b1 ) {
	        return 1;
	    } else if ( b0 && !b1 ) {
	        return -1;
	    } else if (b0 && b1) {
	        int v0 = h0.cards[i].value.ordinal();
	        int v1 = h1.cards[i].value.ordinal();
	        return v1 - v0;
	    }
	    return 0;
	}
	
	private static int compareStraightFlush(Hand5 h0, Hand5 h1) {
	    boolean b0 = h0.isStraightFlush();
	    boolean b1 = h1.isStraightFlush();
	    if(h0.isStraightFlush() && h0.cards[1].value == Value.FIVE 
	    		&& h1.isStraightFlush() && h1.cards[1].value != Value.FIVE ) {
	    	return 1;
	    }else if (h0.isStraightFlush() && h0.cards[1].value != Value.FIVE 
	    		&& h1.isStraightFlush() && h1.cards[1].value == Value.FIVE ) {
	    	return -1;
	    }else if (h0.isStraightFlush() && h0.cards[1].value == Value.FIVE 
	    		&& h1.isStraightFlush() && h1.cards[1].value == Value.FIVE ) {
	    	return 0;
	    }else {
	    	return highCheck( b0, b1, h0, h1, 0 );
	    }
	}
	
	private static int compare4OfAKind(Hand5 h0, Hand5 h1) {
		boolean b0 = h0.is4OfAKind();
		boolean b1 = h1.is4OfAKind();
		return highCheck( b0, b1, h0, h1, 1);
	}
	
	private static int compareFullHouse(Hand5 h0, Hand5 h1) {
	    boolean b0 = h0.isFullHouse();
	    boolean b1 = h1.isFullHouse();
	    return highCheck( b0, b1, h0, h1, 2);
	}
	
	private static int compareFlush(Hand5 h0, Hand5 h1) {
	     boolean b0 = h0.isFlush();
	     boolean b1 = h1.isFlush();
	     if ( !b0 && b1 ) {
	    	 return 1;
	     } else if ( b0 && !b1 ) {
	    	 return -1;
	     } else if ( b0 && b1 ) {
	    	 return compareOne(h0, h1);
	     } else {
	    	 return 0;
	     }
	}
	
	private static int compareStraight(Hand5 h0, Hand5 h1) {
	     boolean b0 = h0.isStraight();
	     boolean b1 = h1.isStraight();
	     if ( !b0 && b1 ) {
	    	 return 1;
	     } else if ( b0 && !b1 ) {
	    	 return -1;
	     } else if ( b0 && b1 ) {
	       // special ace, 2, 3 straight; watch out for confusion with 2,3,4
	    	 if (h0.cards[0].value == Value.ACE && h0.cards[1].value == Value.FIVE){
	    		 if (h1.cards[0].value == Value.ACE && h1.cards[1].value == Value.FIVE){
	    			 return 0;
	    		 } else {
	    			 return 1;
	    		 }
	    	 } else if (h1.cards[0].value == Value.ACE && h1.cards[1].value == Value.FIVE) {
	    		 return -1;
	    	 }
	    	 return compareOne(h0, h1);
	    	 }
	     return 0;
	 }
	
	private static int compare3OfAKind(Hand5 h0, Hand5 h1) {
		 boolean b0 = h0.is3OfAKind();
	     boolean b1 = h1.is3OfAKind();
	     if ( !b0 && b1 ) {
	         return 1;
	     } else if ( b0 && !b1 ) {
	    	 return -1;
	     } else if ( b0 && b1 ) {
	    	 return h1.cards[2].value.ordinal() - h0.cards[2].value.ordinal();
	     }
	     return 0;
	}
	
	private static int compareTwoPair(Hand5 h0, Hand5 h1) {
		 boolean b0 = h0.isTwoPair();
	     boolean b1 = h1.isTwoPair();
	     if ( !b0 && b1 ) {
	         return 1;
	     } else if ( b0 && !b1 ) {
	    	 return -1;
	     } else if ( b0 && b1 ) {
	    	 Value h0Largest = Value.TWO;
	    	 Value h0Second = Value.TWO;
	    	 Value h1Largest = Value.TWO;
	    	 Value h1Second = Value.TWO;
	    	 Value h0Single = Value.TWO;
	    	 Value h1Single = Value.TWO;
	    	 for(int i = 0; i<2; i++){
	    		 if(h0.cards[i].value == h0.cards[i+1].value){
	    			 h0Largest = h0.cards[i].value;
	    			 break;
	    		 }
	    	 }
	    	 for(int i = 0; i<2; i++){
	    		 if(h1.cards[i].value == h1.cards[i+1].value){
	    			 h1Largest = h1.cards[i].value;
	    			 break;
	    		 }
	    	 }
	    	 for(int i = 2; i<4; i++){
	    		 if(h0.cards[i].value == h0.cards[i+1].value){
	    			 h0Second = h0.cards[i].value;
	    			 break;
	    		 }
	    	 }
	    	 for(int i = 2; i<4; i++){
	    		 if(h1.cards[i].value == h1.cards[i+1].value){
	    			 h1Second = h1.cards[i].value;
	    			 break;
	    		 }
	    	 }
	    	 if (h0.cards[0].value != h0.cards[1].value){
	    		 h0Single = h0.cards[0].value;
	    	 } else if (h0.cards[2].value != h0.cards[3].value){
	    		 h0Single = h0.cards[2].value;
	    	 } else {
	    		 h0Single = h0.cards[4].value;
	    	 }
	    	 if (h1.cards[0].value != h1.cards[1].value){
	    		 h1Single = h1.cards[0].value;
	    	 } else if (h1.cards[2].value != h1.cards[3].value){
	    		 h1Single = h1.cards[2].value;
	    	 } else {
	    		 h1Single = h1.cards[4].value;
	    	 }
	    	 if (h0Largest.ordinal() != h1Largest.ordinal()){
	    		 return h1Largest.ordinal() - h0Largest.ordinal();
	    	 } else {
	    		 if (h0Second.ordinal() != h1Second.ordinal()){
	    			 return h1Second.ordinal() - h0Second.ordinal();
	    		 } else {
	    			 if (h0Single.ordinal() != h1Single.ordinal()){
	    				 return h1Single.ordinal() - h0Single.ordinal();
	    			 } 
	    		}
	    	}
	    }
	     return 0;
	}
	
	private static int compareOnePair(Hand5 h0, Hand5 h1){
		 boolean b0 = h0.isOnePair();
	     boolean b1 = h1.isOnePair();
	     Value h0p = Value.TWO;
	     Value h1p = Value.TWO;
	     if ( !b0 && b1 ) {
	         return 1;
	     } else if ( b0 && !b1 ) {
	    	 return -1;
	     } else if ( b0 && b1 ) {
	    	 for (int i=0; i<4; i++){
	    		 if (h0.cards[i].value == h0.cards[i+1].value){
	    			 h0p = h0.cards[i].value;
	    			 break;
	    		 } 
	    	 }
	    	 for (int i=0; i<4; i++){
	    		 if (h1.cards[i].value == h1.cards[i+1].value){
	    			 h1p = h1.cards[i].value;
	    			 break;
	    		 }
	    	 }
	    	 if (h0p != h1p){
	    		 return h1p.ordinal() - h0p.ordinal();
	    	 } else {
	    		 return compareOne(h0, h1);
	    	 }
	     }
	     return 0;
	}
	
	private static int compareOne(Hand5 h0, Hand5 h1) {
	    for( int i = 0; i < h0.cards.length; i++ ) {
	    	int v0 = h0.cards[i].value.ordinal();
	    	int v1 = h1.cards[i].value.ordinal();
	    	if ( v0 != v1 ) {
	    		return v1 - v0;
	        }
	    }
	    return 0;
	}
	
	public int compare(Hand5 h0, Hand5 h1) {
		if (compareEqualHands(h0,h1)) {
			return 0;
		} else {
			return compare1(h0, h1);
		}
	}
	
	public int compare1(Hand5 h0, Hand5 h1) {
		int cmp;
	    // use short circuit evaluation
	    if ( (0 != ( cmp = compareStraightFlush(h0,h1) ) )
	          || (0 != ( cmp = compare4OfAKind(h0,h1) ) )
	          || (0 != ( cmp = compareFullHouse(h0,h1) ) )
	          || (0 != ( cmp = compareFlush(h0,h1) ) )
	          || (0 != ( cmp = compareStraight(h0,h1) ) )
	          || (0 != ( cmp = compare3OfAKind(h0,h1) ) )
	          || (0 != ( cmp = compareTwoPair(h0,h1) ) )
	          || (0 != ( cmp = compareOnePair (h0,h1) ) )
	          || (0 != ( cmp = compareOne (h0,h1) ) ) ) {
	    }
	    return cmp;
    }

	public static boolean compareEqualHands(Hand5 h0, Hand5 h1) {
		for(int i=0; i<h0.cards.length;i++){
			if (h0.cards[i].value != h1.cards[i].value) {
				return false;
			}
		}
		if ( h0.isFlush() && h1.isFlush()) {
			return true;
		} else if ((!h0.isFlush())&&(h1.isFlush()) || (h0.isFlush())&&(!h1.isFlush())) {
			return false;
		}
		return true;
	}

  // creates a hand from the 7 cards provided, skipping cards i and j
  public static Hand5 pick5( Card[] sevenCards, int i, int j ) {
	  Hand5 result = new Hand5();
	  int k = 0;
	  for (int l=0; l<sevenCards.length; l++) {
		  if (l != i && l != j){
			  result.cards[k++] = sevenCards[l];
		  }
	  }
	  
	  Hand5 result1 = new Hand5(result.cards[0], result.cards[1],result.cards[2],
			  result.cards[3],result.cards[4]);
	
	  return result1;
   }
	  
 
  // returns 21 hands of 5 cards made up from the seven cards
  // provided
  public static Hand5[] compute7Choose5( Card[] sevenCards ) {
	  Hand5[] result = new Hand5[21]; // 7 choose 2 is 21
	  int k = 0;
	  for ( int i = 0 ; i < 7; i++ ) {
		  for ( int j = i+1; j < 7; j++ ) {
			  Hand5 aHand = pick5( sevenCards, i, j );
			  result[k++] = aHand;
		  }
	  }
	  return result;
  }

  public static Hand5 best5OutOf7( Card[] sevenCards ) {
	  if ( sevenCards.length != 7 ) {
		  System.out.println("Error: need 7 cards input to best5OutOf7");
		  return null;
	  }

	  Hand5[] all5Hands = compute7Choose5( sevenCards );
	  Arrays.sort(all5Hands, new Hand5Comparator());

	  return all5Hands[0];
  }
  


  public final static int NUMTRIALS = 100000;
  // returns the winner using a random deal for the remaining cards
  public static int[] simulate( Card[] community, Card[][] players ) {
	  Card[] rest = new Card[52-community.length-2*players.length];
	  Card[] allCommunity = new Card[5];
	  Card[] allExist = new Card[community.length+2*players.length];
	  //record all existing cards
	  int k =0;
	  for(Card c:community){
		  allExist[k++]=c;
	  }
	  for(int i=0;i<players.length;i++){
		  for(Card c:players[i]){
			  allExist[k++]=c;
		  }
	  }
	  
	  //find the rest cards
	  int l=0;
	  Suit[] suits = Suit.values();
	  Value[] values = Value.values();
	  for(int i=0;i<suits.length;i++){
		  for(int j=0;j<values.length;j++){
			  boolean b1 = true;
			  for(Card c:allExist){
				  if(c.suit == suits[i] && c.value == values[j]){
					  b1 = false;
					  break;
				  }
			  }
		  if(b1){
			  rest[l++]= new Card(suits[i],values[j]);
		  }
			  
		  }
	  }
	  
	  int m = 0;
	  for(Card c:community){
		  allCommunity[m++]=c;
	  }
	  
	  //generate enough random number
	  int[] randomNumber = new int[5-community.length];
	  Random r = new Random();
	  int aNumber;
	  int i1 =0;
	  while(i1<randomNumber.length){
		  boolean b = true;
		  aNumber = r.nextInt(52-community.length-2*players.length);
		  for(int j=0;j<randomNumber.length;j++){
		  	  if (randomNumber[j] == aNumber){
		  		  b = false;
		  		  break;
		  	  }
		  }
		  if(b){
			  randomNumber[i1++] = aNumber;
		  }
	  }
	  
	  //add enough cards into community so there will be five cards in the community
	  for (int j:randomNumber){
		  allCommunity[m++]=rest[j];
	  }
	  
	  //matrix represents each player with 7 cards
	  Card[][] allPlayersAllCards = new Card[players.length][7];
	  for(int i=0;i<players.length;i++){
		  int i2=0;
		  for(int j=0;j<allCommunity.length;j++){
			  allPlayersAllCards[i][i2++]=allCommunity[j];
		  }
		  for (int k1=0;k1<players[0].length;k1++){
			  allPlayersAllCards[i][i2++]=players[i][k1];
		  }
	  }
	  
	  Hand5[] allPlayersBestCards = new Hand5[players.length];
	  for(int i3=0;i3<players.length;i3++){
		  allPlayersBestCards[i3] = best5OutOf7(allPlayersAllCards[i3]);
	  }
	  
	  Hand5[] allPlayersBestCardsNew = allPlayersBestCards.clone();
	  
	  Arrays.sort(allPlayersBestCards, new Hand5Comparator());
	  Hand5 winners = allPlayersBestCards[0];
	  
	  //record which player is equal to the winner hand
	  int[] bestPlayer = new int[players.length];
	  for(int i=0;i<players.length;i++){
		  if (compareEqualHands(allPlayersBestCardsNew[i],winners)) {
			  bestPlayer[i] = 1;
		  }
	  }
	  
	  return bestPlayer;
  }

  // TODO: for students with more sophisticated programming skills
  // assume there are 0 -- 5 community cards. Each player has 2 cards
  // in the hand. return the probability of each player winning, using
  // the random simulation approach outlined in the lab document
  // Done!
  public static double[] probabilities( Card[] community, Card[]... players) {
	  double[] result = new double[players.length];
	  // TODO:writeme
	  for ( int i = 0 ; i < NUMTRIALS; i++ ) {
		  int winner[] = simulate( community, players );
      // TODO: biased to earlier players, does not take into account ties
      // Done!
		  for ( int j = 0; j < winner.length; j++ ) {
			  if (winner[j] == 1) {
				  result[j] += 1.0;
			  }
		  }
	  }
	  
	  double sum1 = 0.0;
	  for (int i = 0 ; i < result.length; i++) {
		  sum1 = sum1 + result[i];
	  }
	  
	  for ( int i = 0 ; i < result.length; i++ ) {
		  result[i] /= ((double) sum1);
	  }
	 
    return result;
  }
}