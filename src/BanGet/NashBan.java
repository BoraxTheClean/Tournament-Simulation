package BanGet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Format.Conquest;
import Player.Deck;
import Player.Player;

public class NashBan extends BanPicker {
	
	private Map<String, double[]> nashCache;
	private Map<String, Double> equityCache;
	private int simulations=1000;
	
	public NashBan() {
		nashCache = new HashMap<String, double[]>();
		equityCache = new HashMap<String, Double>();
	}
	
	private String decksToString(List<Deck> decks) {
		String deckString = "";
		for (Deck deck:decks) {
			deckString = deckString + Integer.toString(deck.id) + ",";
		}
		return deckString;
	}
	
	private String decksToNames(List<Deck> decks) {
		String deckString = "";
		for (Deck deck:decks) {
			deckString = deckString + deck.name + ",";
		}
		return deckString;
	}


	@Override
	public Deck getBan(Player one, Player two) {
		List<Deck> oneDecks = one.getDecks();
		List<Deck> twoDecks = two.getDecks();
		if (nashCache.containsKey(decksToString(oneDecks)+"."+decksToString(twoDecks))) {
			double[] oneNash = nashCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
			return BanUtils.sample(twoDecks, oneNash);
		}
		double[][]payoff = {{0.,0.,0.,0.},{0.,0.,0.,0.},{0.,0.,0.,0.},{0.,0.,0.,0.}};
		Conquest conquest = new Conquest();
		for (int i=0; i<4;i++) {
			for (int j=0;j<4;j++) {
				Deck banOne = oneDecks.get(i);
				Deck banTwo = twoDecks.get(j);
				Player bannedOne = new Player(one.name, BanUtils.remove(oneDecks, banOne), one.id);
				Player bannedTwo = new Player(two.name, BanUtils.remove(twoDecks, banTwo), two.id);
				int oneWins=0;
				for (int k=0; k<simulations;k++) {
					Player winner = conquest.play(bannedOne, bannedTwo);
					if (winner.id==bannedOne.id) {
						oneWins++;
					}
				}
				double oneWinrate = (double) oneWins / (double) simulations;
				payoff[j][i] = oneWinrate;
			}
		}
		TwoPersonZeroSumGame game = new TwoPersonZeroSumGame(payoff);
		double[] oneNash = game.column();
		nashCache.put(decksToString(oneDecks)+"."+decksToString(twoDecks), oneNash);
		equityCache.put(decksToString(oneDecks)+"."+decksToString(twoDecks), game.value());
		return BanUtils.sample(twoDecks, oneNash);
	}

	@Override
	public double getEquityBeforeBan(Player one, Player two) {
		List<Deck> oneDecks = one.getDecks();
		List<Deck> twoDecks = two.getDecks();
		if (nashCache.containsKey(decksToString(oneDecks)+"."+decksToString(twoDecks))) {
			double oneEquity = equityCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
			return oneEquity;
		}
		getBan(one, two);
		return equityCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
	}

}
