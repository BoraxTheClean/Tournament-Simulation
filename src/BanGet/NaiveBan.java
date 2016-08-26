package BanGet;
import Player.*;
import java.util.*;
public class NaiveBan extends BanPicker{
	public Deck getBan(Player hero, Player villian){
		Deck ban=null;
		float max=0;
		for(Deck villianDeck:villian.getDecks()){
			float temp=0;
			for(Deck heroDeck:hero.getDecks()){
				temp+=villianDeck.getWinPercentage(heroDeck);
			}
			if(temp>max){
				max=temp;
				ban=villianDeck;
			}
		}
		return ban;
	}

	@Override
	public double getEquityBeforeBan(Player one, Player two) {
		return 0;
	}

}
