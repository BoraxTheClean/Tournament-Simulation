package BanGet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Player.Deck;

public class BanUtils {
	public static Deck bestAgainst(List<Deck> ourDecks, List<Deck> theirDecks) {
		Deck ban = null;
		float max = 0;
		
		for(Deck villianDeck:theirDecks){
			float temp=0;
			for(Deck heroDeck:ourDecks){
				temp+=villianDeck.getWinPercentage(heroDeck);
			}
			if(temp>max){
				max=temp;
				ban=villianDeck;
			}
			
		}
		return ban;
	}
	
	public static List<Deck> remove(List<Deck> decks, Deck banned) {
		List<Deck> newDecks = new ArrayList<Deck>(decks);
		newDecks.remove(banned);
		return newDecks;
	}
	
	public static Deck sample(List<Deck> decks, double[] dist) {
		double current = 0.;
		double next = 0.;
		double random = new Random().nextDouble();
		for (int i=0; i<dist.length; i++) {
			next = next + dist[i];
			if (random>=current&&random<next) {
				return decks.get(i);
			}
			
		}
		throw new RuntimeException("Nash doesn't sum to one!");
	}
}
