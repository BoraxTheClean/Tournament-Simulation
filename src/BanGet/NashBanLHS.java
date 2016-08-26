package BanGet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Format.Conquest;
import Format.Format;
import Format.LastHeroStanding;
import Player.Deck;
import Player.Player;

public class NashBanLHS extends BanPicker {
	
	private Map<String, double[]> nashCache;
	private int simulations=50000;
	private Map<String, double[]> pickCache;
	private Map<String, Double> equityCache;
	private Map<String, Double> preEquityCache;
	public NashBanLHS() {
		nashCache = new HashMap<String, double[]>();
		pickCache = new HashMap<>();
		equityCache = new HashMap<String, Double>();
		preEquityCache = new HashMap<String, Double>();
	}
	
	public static String decksToString(List<Deck> decks) {
		String deckString = "";
		for (Deck deck:decks) {
			deckString = deckString + Integer.toString(deck.id) + ",";
		}
		return deckString;
	}
	
	public static String decksToNames(List<Deck> decks) {
		List<String> deckNames = new ArrayList<String>();
		for (Deck deck: decks) {
			deckNames.add(deck.name);
		}
		String deckString = "";
		for (String deckName:deckNames) {
			deckString = deckString + deckName + ",";
		}
		return deckString;
	}
	
	public static String decksToSortedNames(List<Deck> decks) {
		List<String> deckNames = new ArrayList<String>();
		for (Deck deck: decks) {
			deckNames.add(deck.name);
		}
		Collections.sort(deckNames);
		String deckString = "";
		for (String deckName:deckNames) {
			deckString = deckString + deckName + ",";
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
		Format lhs = new LastHeroStanding();
		for (int i=0; i<4;i++) {
			for (int j=0;j<4;j++) {
				Deck banOne = oneDecks.get(i);
				Deck banTwo = twoDecks.get(j);
				Player bannedOne = new Player(one.name, BanUtils.remove(oneDecks, banOne), one.id);
				Player bannedTwo = new Player(two.name, BanUtils.remove(twoDecks, banTwo), two.id);
				double oneWinrate = equityPostBan(bannedOne.getDecks(), bannedTwo.getDecks());
				payoff[j][i] = oneWinrate;
			}
		}
		TwoPersonZeroSumGame game = new TwoPersonZeroSumGame(payoff);
		double[] oneNash = game.column();
		nashCache.put(decksToString(oneDecks)+"."+decksToString(twoDecks), oneNash);
		preEquityCache.put(decksToString(oneDecks)+"."+decksToString(twoDecks), game.value());
		//The stuff below prints the matchup data.
		System.out.println("Player one: " + decksToNames(oneDecks));
		System.out.println("Player two: " + decksToNames(twoDecks));
		for (int i=0; i<payoff.length; i++) {
			System.out.println(Arrays.toString(payoff[i]));
		}
		System.out.println("Nash: " + Arrays.toString(oneNash));
		System.out.println("Converse: " + Arrays.toString(game.row()));
		Deck ban = BanUtils.sample(twoDecks, oneNash);
		System.out.println("Ban: " + ban.name);
		System.out.println("Equity: " + game.value());
		return ban;
	}
	
	public TwoPersonZeroSumGame nashPostBan(List<Deck> oneDecks, List<Deck> twoDecks) {
		double[][] payoff=new double[oneDecks.size()][twoDecks.size()];
		LastHeroStanding lhs=new LastHeroStanding();
		for(int i=0;i<payoff.length;i++){
			for(int j=0;j<payoff[i].length;j++){
				payoff[j][i] = lhs.getEquity(oneDecks,twoDecks,oneDecks.get(j),twoDecks.get(i));
			}
		}
		TwoPersonZeroSumGame game = new TwoPersonZeroSumGame(payoff);
		return game;
		
	}
	//Start of LHS match, no one has picked a deck, both players have banned
	public Deck nashPick(Player one, Player two){
		List<Deck> oneDecks = one.getRemainingDecks();
		List<Deck> twoDecks = two.getRemainingDecks();
		//Memoization to go here
		if (pickCache.containsKey(decksToString(oneDecks)+"."+decksToString(twoDecks))) {
			double[] oneNash = pickCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
			return BanUtils.sample(oneDecks, oneNash);
		}
		TwoPersonZeroSumGame game = nashPostBan(oneDecks, twoDecks);

		double[] oneNash = game.column();
		pickCache.put(decksToString(oneDecks)+"."+decksToString(twoDecks), oneNash);
		
		Deck play = BanUtils.sample(oneDecks, oneNash);
		return play;
		}
	
	public double equityPostBan(List<Deck> oneDecks, List<Deck> twoDecks) {
		if (equityCache.containsKey(decksToString(oneDecks)+"."+decksToString(twoDecks))) {
			return equityCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
		}
		TwoPersonZeroSumGame game = nashPostBan(oneDecks, twoDecks);
		double equity = game.value();
		equityCache.put(decksToString(oneDecks)+"."+decksToString(twoDecks), equity);
		return equity;
		
	}

	@Override
	public double getEquityBeforeBan(Player one, Player two) {
		List<Deck> oneDecks = one.getDecks();
		List<Deck> twoDecks = two.getDecks();
		if (nashCache.containsKey(decksToString(oneDecks)+"."+decksToString(twoDecks))) {
			double oneEquity = preEquityCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
			return oneEquity;
		}
		getBan(one, two);
		return preEquityCache.get(decksToString(oneDecks)+"."+decksToString(twoDecks));
	}

}
