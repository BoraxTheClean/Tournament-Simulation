package Player;
public class Game{
	public Player winner;
	public Player loser;
	public Deck winnerDeck;
	public Deck loserDeck;
	
	public Game(Player winner, Player loser, Deck winnerDeck, Deck loserDeck){
		this.winner = winner;
		this.loser = loser;
		this.winnerDeck = winnerDeck;
		this.loserDeck = loserDeck;
	}

}
