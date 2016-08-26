package Player;
import java.util.Random;
import java.util.*;
import BanGet.*;
public class Player implements Comparable<Player>{
	public String name;
	private List<Deck> decks;
	public int id; 
	public boolean isActive=true; //Used for Last hero standing
	private boolean[] usedDecks; //False indicates a deck is unused.
	public List<Match> history;
	//Tiebreakers are: 1st average amount of points opponents have
	//2nd the number of games you won
	public int rounds;
	public int gamesWon;
	public int points;
	public double averageOpponentPoints;
	public int opponentPoints;
	public	Player(String name, List<Deck> decks,int id){
		this.name=name;
		this.decks=decks;
		this.id=id;
		usedDecks=new boolean[decks.size()];
		history = new LinkedList<Match>();
	}
	public void computeTiebreaker(){
		for(Match match: history){
			for(Game g:match.games){
				if(g.winner.equals(this))
					gamesWon++;
			}
		}
		//Never winner :-(
		if(gamesWon==0)
			averageOpponentPoints=0;
		else
			averageOpponentPoints=opponentPoints/gamesWon;

	}
	public void resetTiebreaker(){
		history=new LinkedList<Match>();
		gamesWon=0;
		points=0;
		averageOpponentPoints=0;
		opponentPoints=0;
	}
	public int compareTo(Player p){
		if(this.equals(p))
			return 0;
		if(p.points==this.points){
			if(p.averageOpponentPoints==this.averageOpponentPoints){
				return this.opponentPoints-p.opponentPoints;	
			}
			else
				return this.opponentPoints-p.opponentPoints;
		}else
			return this.points-p.points;
	}
	//Just for testing purposes.
	public Player(String name){
		this.name=name;
	}
	public boolean hasDecks(){
		for(boolean b: usedDecks){
			if(!b)
				return true;
		}
		return false;
	}
	public Deck getUnusedDeck(){
		if(!this.hasDecks())
			return null; //You messed up, no more decks.
		
		Random rng=new Random();
		int index=rng.nextInt(decks.size());
		while(usedDecks[index])
			index=rng.nextInt(decks.size());
		return decks.get(index);
	}
	public boolean setDeckToUsed(Deck deck){
		if(usedDecks[decks.lastIndexOf(deck)])
			return false; //Deck is already used
		
		usedDecks[decks.lastIndexOf(deck)]=true;
		return true;
	}
	public void resetDecks(){
		usedDecks=new boolean[decks.size()];
	}
	public void addHistory(Match match){
		history.add(match);
	}
	public void resetHistory(){
		history=new LinkedList<Match>();
	}
	public boolean equals(Player p){
		return p.id==this.id;
	}
	public List<Deck> getDecks(){
		return decks;
	}
	public int decksLeft(){
		int ret=0;
		for(boolean b:usedDecks){
			if(!b)
				ret++;
		}
		return ret;
	}
	public List<Deck> getRemainingDecks(){
		List<Deck> decks=new LinkedList<>();
		for(int i=0;i<usedDecks.length;i++){
			if(!usedDecks[i])
				decks.add(this.decks.get(i));
		}
		return decks;
	}
	public Deck getBan(Player opp,BanPicker b){
		return b.getBan(this,opp);
	}
}
