package Format;
import java.util.*;
import Player.*;
import BanGet.*;
public class Conquest implements Format{
	private BanPicker bp=null;
	public void setBP(BanPicker thisBP){
		bp=thisBP;
	}
	
	public double getEquityBeforeBan(Player playerOne, Player playerTwo) {
		if (bp==null) {
			throw new RuntimeException();
		}
		return bp.getEquityBeforeBan(playerOne, playerTwo);
	}
	
	public Player play(Player playerOne,Player playerTwo){		
		Player winner;
		Player loser;
		if(bp!=null){
			getBans(playerOne,playerTwo);
		}
		List<Game> games = new LinkedList<Game>();
 
		while(playerOne.hasDecks() && playerTwo.hasDecks()){
			Deck playerOneDeck = playerOne.getUnusedDeck();
			Deck playerTwoDeck = playerTwo.getUnusedDeck();
			
			float playerOneWin = playerOneDeck.getWinPercentage(playerTwoDeck);
			Random rng = new Random();
			float playerTwoWin = rng.nextFloat();
			
			if(playerOneWin > playerTwoWin){
				playerOne.setDeckToUsed(playerOneDeck);
				Game game = new Game(playerOne, playerTwo, playerOneDeck, playerTwoDeck);
				games.add(game);
			}
			else{
				playerTwo.setDeckToUsed(playerTwoDeck);
				Game game = new Game(playerTwo, playerOne, playerTwoDeck, playerOneDeck);
				games.add(game);
			}
		}
		
		if(playerOne.hasDecks()){
			playerOne.resetDecks();
			playerTwo.resetDecks();
			winner = playerTwo;
			loser = playerOne;
		}
		else{
			playerOne.resetDecks();
			playerTwo.resetDecks();
			winner = playerOne;
			loser = playerTwo;
		}
		
		Match match = new Match(winner, loser, games);
		playerOne.addHistory(match);
		playerTwo.addHistory(match);
		loser.opponentPoints+=3;
		loser.rounds++;
		winner.rounds++;
		winner.points+=3;
		return winner;
	}
	public void getBans(Player p1,Player p2){
        p1.setDeckToUsed(p2.getBan(p1, bp));
        p2.setDeckToUsed(p1.getBan(p2,bp));
	}

}
