package Format;

import java.util.*;

import BanGet.BanPicker;
import BanGet.BanUtils;
import BanGet.MockBanPicker;
import BanGet.NashBanLHS;
import Player.*;

public class LastHeroStanding implements Format{
	private BanPicker bp=null;

	public Player play(Player playerOne,Player playerTwo){		
		Player winner;
		Player loser;
		if(bp!=null){
			getBans(playerOne,playerTwo);
		}
		NashBanLHS nb=new NashBanLHS();
		Deck playerOneDeck = nb.nashPick(playerOne, playerTwo);
		Deck playerTwoDeck = nb.nashPick(playerTwo, playerOne);
		List<Game> games = new LinkedList<Game>();

		while(playerOne.hasDecks() && playerTwo.hasDecks()){
			if(playerOne.isActive){
				//Get NashPick Deck
				playerOneDeck = pickNextDeck(playerOne.getRemainingDecks(),playerTwo.getRemainingDecks(),playerTwoDeck);
			}
			if(playerTwo.isActive){
				//Get NashPick Deck
				playerTwoDeck = pickNextDeck(playerTwo.getRemainingDecks(),playerOne.getRemainingDecks(),playerOneDeck);
			}

			float playerOneWin = playerOneDeck.getWinPercentage(playerTwoDeck);
			Random rng = new Random();
			float playerTwoWin = rng.nextFloat();

			if(playerOneWin > playerTwoWin){
				playerTwo.setDeckToUsed(playerTwoDeck);
				playerOne.isActive = false;
				playerTwo.isActive = true;
				Game game = new Game(playerOne, playerTwo, playerOneDeck, playerTwoDeck);
				games.add(game);
			}
			else{
				playerOne.setDeckToUsed(playerOneDeck);
				playerOne.isActive = true;
				playerTwo.isActive = false;
				Game game = new Game(playerTwo, playerOne, playerTwoDeck, playerOneDeck);
				games.add(game);
			}
		}

		if(playerTwo.hasDecks()){
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
	@Override
	public void setBP(BanPicker thisBP) {
		this.bp=thisBP;

	}  
	public static Deck pickNextDeck(List<Deck> active, List<Deck> winner, Deck winnerDeck){
		double equity;
		List<Deck> decks=active;
		int max=-1;
		double ceiling=Double.MIN_VALUE;
		for(int i=0;i<decks.size();i++){
			Deck current=decks.get(i);
			equity=getEquity(active,winner,current,winnerDeck);
			if(ceiling<equity){
				ceiling=equity;
				max=i;
			}
		}
		return decks.get(max);

	}
	public static double getEquity(List<Deck> active, List<Deck> winner, Deck activeDeck, Deck winnerDeck){
		try {

			if(winner.size()==1) {
				double winChance = 1.0;
				for (Deck deck: active) {
					winChance = winChance*winnerDeck.getWinPercentage(deck);
				}
				return 1.0 - winChance;
			}
			if(active.size()==1) {
				double activeChance = 1.0;
				for (Deck deck: winner) {
					activeChance = activeChance * activeDeck.getWinPercentage(deck);
				}
				return activeChance;

			}
			List<Deck> winnerAfterLoss = BanUtils.remove(winner, winnerDeck);
			double activeWins=activeDeck.getWinPercentage(winnerDeck)*(1-getEquity(winnerAfterLoss, active,pickNextDeck(winnerAfterLoss,active,activeDeck), activeDeck));


			List<Deck> activeAfterLoss = BanUtils.remove(active, activeDeck);
			double winnerWins=winnerDeck.getWinPercentage(activeDeck)*getEquity(activeAfterLoss,winner,pickNextDeck(activeAfterLoss,winner,winnerDeck),winnerDeck);

			return activeWins+winnerWins;
		}
		catch (NullPointerException e) {
			System.out.println(activeDeck.name + " versus " + winnerDeck.name);
			System.out.println("player1");
			for (Deck d1: active) {
				System.out.println(d1.name);
			}
			System.out.println("player2");
			for (Deck d2: winner) {
				System.out.println(d2.name);
			}
			throw new RuntimeException(e);
		}
	}
	public double getEquityBeforeBan(Player playerOne, Player playerTwo) {
		if (bp==null) {
			throw new RuntimeException();
		}
		return bp.getEquityBeforeBan(playerOne, playerTwo);
	}

}
